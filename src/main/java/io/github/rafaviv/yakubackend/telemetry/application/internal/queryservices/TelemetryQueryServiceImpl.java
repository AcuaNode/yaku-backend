package io.github.rafaviv.yakubackend.telemetry.application.internal.queryservices;

import io.github.rafaviv.yakubackend.telemetry.domain.model.aggregates.MeasurementAggregate;
import io.github.rafaviv.yakubackend.telemetry.domain.model.aggregates.SensorReading;
import io.github.rafaviv.yakubackend.telemetry.domain.model.queries.GetHistoricalDataQuery;
import io.github.rafaviv.yakubackend.telemetry.domain.model.queries.GetPondStatusQuery;
import io.github.rafaviv.yakubackend.telemetry.infrastructure.persistence.jpa.repositories.MeasurementAggregateRepository;
import io.github.rafaviv.yakubackend.telemetry.infrastructure.persistence.jpa.repositories.SensorReadingRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TelemetryQueryServiceImpl implements TelemetryQueryService {

    private final SensorReadingRepository sensorReadingRepository;
    private final MeasurementAggregateRepository measurementAggregateRepository;

    public TelemetryQueryServiceImpl(SensorReadingRepository sensorReadingRepository,
                                     MeasurementAggregateRepository measurementAggregateRepository) {
        this.sensorReadingRepository = sensorReadingRepository;
        this.measurementAggregateRepository = measurementAggregateRepository;
    }

    @Override
    public List<SensorReading> handle(GetPondStatusQuery query) {
        return sensorReadingRepository.findByPondIdOrderByTimestampDesc(query.pondId());
    }

    @Override
    public List<MeasurementAggregate> handle(GetHistoricalDataQuery query) {
        LocalDateTime start = switch (query.timeFilter()) {
            case DAILY -> LocalDateTime.now().minusDays(1);
            case WEEKLY -> LocalDateTime.now().minusWeeks(1);
            case MONTHLY -> LocalDateTime.now().minusMonths(1);
            case YEARLY -> LocalDateTime.now().minusYears(1);
        };
        return measurementAggregateRepository.findByPondIdAndPeriodStartGreaterThanEqual(query.pondId(), start);
    }
}
