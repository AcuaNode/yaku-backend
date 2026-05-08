package io.github.rafaviv.yakubackend.telemetry.application.internal.commandservices;

import io.github.rafaviv.yakubackend.telemetry.domain.model.commands.GenerateAggregatesCommand;
import io.github.rafaviv.yakubackend.telemetry.domain.model.commands.ProcessIncomingReadingCommand;

public interface TelemetryCommandService {
    void handle(ProcessIncomingReadingCommand command);
    void handle(GenerateAggregatesCommand command);
}
