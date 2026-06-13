package io.github.rafaviv.yakubackend.telemetry.application.internal.commandservices;

import io.github.rafaviv.yakubackend.telemetry.application.outboundservices.acl.ExternalEquipmentService;
import io.github.rafaviv.yakubackend.telemetry.domain.model.aggregates.SensorReading;
import io.github.rafaviv.yakubackend.telemetry.domain.model.commands.GenerateAggregatesCommand;
import io.github.rafaviv.yakubackend.telemetry.domain.model.commands.ProcessIncomingReadingCommand;
import io.github.rafaviv.yakubackend.telemetry.domain.model.events.AnomalyDetectedEvent;
import io.github.rafaviv.yakubackend.telemetry.domain.model.valueobjects.MeasurementValue;
import io.github.rafaviv.yakubackend.telemetry.infrastructure.persistence.jpa.repositories.SensorPondMappingRepository;
import io.github.rafaviv.yakubackend.telemetry.infrastructure.persistence.jpa.repositories.SensorReadingRepository;
import io.github.rafaviv.yakubackend.telemetry.infrastructure.persistence.jpa.repositories.ThresholdRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TelemetryCommandServiceImpl implements TelemetryCommandService {

    private static final Logger log = LoggerFactory.getLogger(TelemetryCommandServiceImpl.class);

    private final SensorReadingRepository sensorReadingRepository;
    private final ThresholdRepository thresholdRepository;
    private final SensorPondMappingRepository sensorPondMappingRepository;
    private final ExternalEquipmentService externalEquipmentService;
    private final ApplicationEventPublisher eventPublisher;

    public TelemetryCommandServiceImpl(SensorReadingRepository sensorReadingRepository,
                                       ThresholdRepository thresholdRepository,
                                       SensorPondMappingRepository sensorPondMappingRepository,
                                       ExternalEquipmentService externalEquipmentService,
                                       ApplicationEventPublisher eventPublisher) {
        this.sensorReadingRepository = sensorReadingRepository;
        this.thresholdRepository = thresholdRepository;
        this.sensorPondMappingRepository = sensorPondMappingRepository;
        this.externalEquipmentService = externalEquipmentService;
        this.eventPublisher = eventPublisher;
    }

    @Override
    @Transactional
    public void handle(ProcessIncomingReadingCommand command) {
        // Validate sensor belongs to the pond
        sensorPondMappingRepository.findBySensorId(command.sensorId())
                .ifPresentOrElse(mapping -> {
                    if (!mapping.getPondId().equals(command.pondId())) {
                        throw new IllegalArgumentException("Sensor does not belong to the specified pond");
                    }
                }, () -> {
                    throw new IllegalArgumentException("Sensor is not mapped to any pond");
                });

        // Save the raw reading
        MeasurementValue measurement = new MeasurementValue(command.value(), command.unit());
        SensorReading reading = new SensorReading(command.pondId(), command.sensorType(), measurement, command.timestamp());
        sensorReadingRepository.save(reading);

        try {
            // Resolucion de Identidad (Paso 1)
            String speciesName = externalEquipmentService.getSpeciesByPondId(command.pondId());

            // Carga de Reglas (Paso 3)
            thresholdRepository.findBySpecies(speciesName).ifPresentOrElse(threshold -> {
                boolean isAnomaly = false;
                Double minAllowed = null;
                Double maxAllowed = null;

                if ("TEMPERATURE".equalsIgnoreCase(command.sensorType().name())) {
                    minAllowed = threshold.getMinTemperature();
                    maxAllowed = threshold.getMaxTemperature();
                    if (threshold.isTemperatureViolation(command.value())) {
                        isAnomaly = true;
                    }
                } else if ("PH".equalsIgnoreCase(command.sensorType().name())) {
                    minAllowed = threshold.getMinPh();
                    maxAllowed = threshold.getMaxPh();
                    if (threshold.isPhViolation(command.value())) {
                        isAnomaly = true;
                    }
                } else if ("TURBIDITY".equalsIgnoreCase(command.sensorType().name())) {
                    minAllowed = threshold.getMinTurbidity();
                    maxAllowed = threshold.getMaxTurbidity();
                    if (threshold.isTurbidityViolation(command.value())) {
                        isAnomaly = true;
                    }
                }

                // Evaluacion de Negocio y Despacho Dinámico (Paso 4)
                if (isAnomaly && minAllowed != null && maxAllowed != null) {
                    String dangerType = "CRITICAL"; 
                    String message = String.format("[%s] Anomaly Detected: %s level is %f. Allowed range: [%f, %f] for species %s",
                            dangerType, command.sensorType(), command.value(), minAllowed, maxAllowed, speciesName);
                    
                    AnomalyDetectedEvent event = new AnomalyDetectedEvent(
                            command.pondId(),
                            command.sensorType(),
                            command.value(),
                            minAllowed,
                            maxAllowed,
                            message
                    );
                    // Publicar al servicio de notificaciones
                    eventPublisher.publishEvent(event);
                }
            }, () -> {
                log.warn("No thresholds configured for species: {}", speciesName);
            });

        } catch (Exception e) {
            // Manejo de Errores: loggear advertencia
            log.warn("Failed to evaluate telemetry rules for pond {}: {}", command.pondId(), e.getMessage());
        }
    }

    @Override
    @Transactional
    public void handle(GenerateAggregatesCommand command) {
        // Batch processing logic placeholder
    }

    @Override
    @Transactional
    public Long handle(io.github.rafaviv.yakubackend.telemetry.domain.model.commands.ConfigureThresholdCommand command) {
        var thresholdOptional = thresholdRepository.findBySpecies(command.species());
        if (thresholdOptional.isPresent()) {
            // Since there's no update method in Threshold yet, we could either add an update method or just replace it.
            // Let's create a new one and delete the old one or just update it if we add a method.
            // But wait, Threshold extends AbstractAggregateRoot, we can just delete and recreate or add an update method.
            // I'll delete the existing one and create a new one to keep it simple, since the ID will change but it's only looked up by species.
            thresholdRepository.delete(thresholdOptional.get());
        }

        var threshold = new io.github.rafaviv.yakubackend.telemetry.domain.model.aggregates.Threshold(
                command.species(),
                command.minTemperature(),
                command.maxTemperature(),
                command.minPh(),
                command.maxPh(),
                command.minTurbidity(),
                command.maxTurbidity()
        );
        thresholdRepository.save(threshold);
        return threshold.getId();
    }
}
