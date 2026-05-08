package io.github.rafaviv.yakubackend.telemetry.domain.model.events;

import io.github.rafaviv.yakubackend.telemetry.domain.model.valueobjects.SensorType;

public record AnomalyDetectedEvent(
        Long pondId,
        SensorType sensorType,
        Double value,
        Double minAllowed,
        Double maxAllowed,
        String message
) {
}
