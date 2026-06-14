package io.github.rafaviv.yakubackend.telemetry.domain.model.commands;

public record ProcessGroupedTelemetryCommand(
        Long pondId,
        Double temperature,
        Double ph,
        Double turbidity
) {
}
