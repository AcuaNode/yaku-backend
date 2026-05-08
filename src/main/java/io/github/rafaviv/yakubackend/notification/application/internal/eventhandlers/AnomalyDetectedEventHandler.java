package io.github.rafaviv.yakubackend.notification.application.internal.eventhandlers;

import io.github.rafaviv.yakubackend.notification.domain.model.aggregates.Alert;
import io.github.rafaviv.yakubackend.notification.domain.model.valueobjects.NotificationChannel;
import io.github.rafaviv.yakubackend.notification.infrastructure.persistence.jpa.repositories.AlertRepository;
import io.github.rafaviv.yakubackend.telemetry.domain.model.events.AnomalyDetectedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
public class AnomalyDetectedEventHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(AnomalyDetectedEventHandler.class);

    private final AlertRepository alertRepository;

    public AnomalyDetectedEventHandler(AlertRepository alertRepository) {
        this.alertRepository = alertRepository;
    }

    @EventListener
    public void on(AnomalyDetectedEvent event) {
        LOGGER.info("Received Anomaly Detected Event for pond {}", event.pondId());
        
        // For simplicity, we default to EMAIL channel
        // In a real scenario, this could be resolved querying the Equipment/IAM context
        Alert alert = new Alert(event.pondId(), event.message(), NotificationChannel.EMAIL);
        
        // Simulate sending alert
        LOGGER.info("Sending alert via {}: {}", alert.getChannel(), alert.getMessage());
        alert.markAsSent();
        
        alertRepository.save(alert);
    }
}
