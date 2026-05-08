package io.github.rafaviv.yakubackend.telemetry.application.internal.commandservices;

import io.github.rafaviv.yakubackend.telemetry.domain.model.aggregates.SensorReading;
import io.github.rafaviv.yakubackend.telemetry.domain.model.commands.GenerateAggregatesCommand;
import io.github.rafaviv.yakubackend.telemetry.domain.model.commands.ProcessIncomingReadingCommand;
import io.github.rafaviv.yakubackend.telemetry.domain.model.events.AnomalyDetectedEvent;
import io.github.rafaviv.yakubackend.telemetry.domain.model.valueobjects.MeasurementValue;
import io.github.rafaviv.yakubackend.telemetry.infrastructure.persistence.jpa.repositories.SensorPondMappingRepository;
import io.github.rafaviv.yakubackend.telemetry.infrastructure.persistence.jpa.repositories.SensorReadingRepository;
import io.github.rafaviv.yakubackend.telemetry.infrastructure.persistence.jpa.repositories.SpeciesOptimalRangeRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TelemetryCommandServiceImpl implements TelemetryCommandService {

    private final SensorReadingRepository sensorReadingRepository;
    private final SpeciesOptimalRangeRepository speciesOptimalRangeRepository;
    private final SensorPondMappingRepository sensorPondMappingRepository;
    private final ApplicationEventPublisher eventPublisher;

    public TelemetryCommandServiceImpl(SensorReadingRepository sensorReadingRepository,
                                       SpeciesOptimalRangeRepository speciesOptimalRangeRepository,
                                       SensorPondMappingRepository sensorPondMappingRepository,
                                       ApplicationEventPublisher eventPublisher) {
        this.sensorReadingRepository = sensorReadingRepository;
        this.speciesOptimalRangeRepository = speciesOptimalRangeRepository;
        this.sensorPondMappingRepository = sensorPondMappingRepository;
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

        // Check for threshold violations using SpeciesOptimalRange
        speciesOptimalRangeRepository.findByPondIdAndSensorType(command.pondId(), command.sensorType())
                .ifPresent(range -> {
                    if (range.isViolation(command.value())) {
                        String message = String.format("Anomaly Detected: %s level in pond %d is %f. Allowed range: [%f, %f]",
                                command.sensorType(), command.pondId(), command.value(), range.getMinAllowed(), range.getMaxAllowed());
                        AnomalyDetectedEvent event = new AnomalyDetectedEvent(
                                command.pondId(),
                                command.sensorType(),
                                command.value(),
                                range.getMinAllowed(),
                                range.getMaxAllowed(),
                                message
                        );
                        // Publish event (will be picked up by Notification context)
                        eventPublisher.publishEvent(event);
                    }
                });
    }

    @Override
    @Transactional
    public void handle(GenerateAggregatesCommand command) {
        // Batch processing logic placeholder
    }
}
