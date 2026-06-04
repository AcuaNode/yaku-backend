package io.github.rafaviv.yakubackend.subscription.application.internal.commandservices;

import io.github.rafaviv.yakubackend.subscription.application.internal.exceptions.PlanNotFoundException;
import io.github.rafaviv.yakubackend.subscription.application.internal.exceptions.SubscriptionNotFoundException;
import io.github.rafaviv.yakubackend.subscription.domain.model.aggregates.Subscription;
import io.github.rafaviv.yakubackend.subscription.domain.model.entities.Plan;
import io.github.rafaviv.yakubackend.subscription.domain.model.valueobjects.PaymentProvider;
import io.github.rafaviv.yakubackend.subscription.infrastructure.persistence.jpa.repositories.PlanRepository;
import io.github.rafaviv.yakubackend.subscription.infrastructure.persistence.jpa.repositories.SubscriptionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Stripe Webhook Command Service
 * Orchestrates business logic when Stripe webhook events are received.
 */
@Service
public class StripeWebhookCommandServiceImpl {

    private final SubscriptionCommandService subscriptionCommandService;
    private final SubscriptionRepository subscriptionRepository;
    private final PlanRepository planRepository;

    public StripeWebhookCommandServiceImpl(SubscriptionCommandService subscriptionCommandService
    , SubscriptionRepository subscriptionRepository, PlanRepository planRepository) {
        this.subscriptionCommandService = subscriptionCommandService;
        this.subscriptionRepository = subscriptionRepository;
        this.planRepository = planRepository;

    }

    @Transactional
    public void subscribeUserToPlanWithStripe(Long userId, Long planId, String externalId) {

        Subscription subscription = subscriptionRepository.findByUserId(userId)
                .orElseThrow(() -> new SubscriptionNotFoundException(userId));

        Plan plan = planRepository.findById(planId)
                .orElseThrow(() -> new PlanNotFoundException(planId));

        subscription.subscribeToPlanWithProvider(plan, externalId, PaymentProvider.STRIPE);
    }


    @Transactional
    public void handleCheckoutSessionCompleted(String userIdStr, String planIdStr, String stripeSubscriptionId) {
        if (userIdStr != null && planIdStr != null) {
            Long userId = Long.valueOf(userIdStr);
            Long planId = Long.valueOf(planIdStr);

            // Llama al servicio de subscripción para registrarla en la base de datos
            subscriptionCommandService.subscribeUserToPlan(userId, planId);

            System.out.println("Checkout session completed and subscription activated for user " + userId);
        } else {
            System.err.println("Missing userId or planId in session completed event");
        }
    }

    @Transactional
    public void handleSubscriptionDeleted(String externalSubscriptionId) {
        // 1. Find subscription by externalSubscriptionId
        // 2. Call subscription.cancel()
        // 3. Save subscription
        System.out.println("Handling customer.subscription.deleted for " + externalSubscriptionId);
    }
}
