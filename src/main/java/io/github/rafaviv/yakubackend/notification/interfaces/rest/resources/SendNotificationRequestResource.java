package io.github.rafaviv.yakubackend.notification.interfaces.rest.resources;

import java.math.BigDecimal;

public record SendNotificationRequestResource(
    String type,
    String message,
    Long userId,
    String role,
    BigDecimal temperature,
    BigDecimal ph,
    String hardwareStatus
) {}
