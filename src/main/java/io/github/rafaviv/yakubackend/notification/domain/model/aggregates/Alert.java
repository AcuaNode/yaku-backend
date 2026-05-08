package io.github.rafaviv.yakubackend.notification.domain.model.aggregates;

import io.github.rafaviv.yakubackend.notification.domain.model.valueobjects.NotificationChannel;
import io.github.rafaviv.yakubackend.notification.domain.model.valueobjects.NotificationStatus;
import io.github.rafaviv.yakubackend.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "alerts")
@Getter
public class Alert extends AuditableAbstractAggregateRoot<Alert> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long pondId;

    @Column(nullable = false)
    private String message;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationChannel channel;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationStatus status;

    public Alert() {
    }

    public Alert(Long pondId, String message, NotificationChannel channel) {
        this.pondId = pondId;
        this.message = message;
        this.channel = channel;
        this.status = NotificationStatus.PENDING;
    }

    public void markAsSent() {
        this.status = NotificationStatus.SENT;
    }

    public void markAsFailed() {
        this.status = NotificationStatus.FAILED;
    }
}
