package io.github.rafaviv.yakubackend.subscription.domain.model.aggregates;

import io.github.rafaviv.yakubackend.shared.domain.model.entities.AuditableModel;
import io.github.rafaviv.yakubackend.subscription.domain.model.entities.Plan;
import io.github.rafaviv.yakubackend.subscription.domain.model.valueobjects.SubscriptionPeriod;
import io.github.rafaviv.yakubackend.subscription.domain.model.valueobjects.SubscriptionStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "subscriptions")
public class Subscription extends AuditableModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @ManyToOne
    @JoinColumn(name = "plan_id", nullable = false)
    private Plan plan;

    @Embedded
    private SubscriptionPeriod period;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SubscriptionStatus status;

    public Subscription(Long userId, Plan plan, SubscriptionPeriod period) {
        this.userId = userId;
        this.plan = plan;
        this.period = period;
        this.status = SubscriptionStatus.ACTIVE;
    }

    public void cancel() {
        this.status = SubscriptionStatus.CANCELLED;
    }

    public void expire() {
        this.status = SubscriptionStatus.EXPIRED;
    }
}
