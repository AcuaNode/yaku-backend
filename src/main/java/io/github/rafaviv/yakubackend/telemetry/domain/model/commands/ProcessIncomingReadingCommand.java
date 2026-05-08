package io.github.rafaviv.yakubackend.telemetry.domain.model.commands;

import io.github.rafaviv.yakubackend.telemetry.domain.model.valueobjects.SensorType;

public record ProcessIncomingReadingCommand(
        Long sensorId,
        Long pondId,
        SensorType sensorType,
        Double value,
        String unit,
        java.time.LocalDateTime timestamp
) {
}
