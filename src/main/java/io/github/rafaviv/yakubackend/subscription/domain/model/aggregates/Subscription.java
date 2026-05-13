package io.github.rafaviv.yakubackend.subscription.domain.model.aggregates;

import io.github.rafaviv.yakubackend.shared.domain.model.entities.AuditableModel;
import io.github.rafaviv.yakubackend.subscription.domain.model.entities.Plan;
import io.github.rafaviv.yakubackend.subscription.domain.model.valueobjects.SubscriptionPeriod;
import io.github.rafaviv.yakubackend.subscription.domain.model.valueobjects.SubscriptionStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

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

    // Constructor existente
    public Subscription(Long userId, Plan plan, SubscriptionPeriod period) {
        this.userId = userId;
        this.plan = plan;
        this.period = period;
        this.status = SubscriptionStatus.ACTIVE;
    }

    /**
     * NUEVO: Constructor para el Test y para nuevos registros
     * Permite crear una suscripción base que luego será activada con un plan.
     */
    public Subscription(Long userId) {
        this.userId = userId;
        this.status = SubscriptionStatus.CANCELLED;
    }

    /**
     * NUEVO: Lógica de Negocio Encapsulada (Patrón: Tell, Don't Ask)
     * La entidad es responsable de calcular su propio periodo basándose en el plan.
     */
    public void subscribeToPlan(Plan plan) {
        this.plan = plan;
        this.status = SubscriptionStatus.ACTIVE;
        // Calculamos el periodo internamente usando la duración del plan
        this.period = new SubscriptionPeriod(
                LocalDate.now(),
                LocalDate.now().plusDays(plan.getDurationInDays())
        );
    }

    public void cancel() {
        this.status = SubscriptionStatus.CANCELLED;
    }

    public void expire() {
        this.status = SubscriptionStatus.EXPIRED;
    }
}