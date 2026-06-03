package io.github.rafaviv.yakubackend.subscription.application.internal.commandservices;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Stripe Webhook Command Service
 * Orchestrates business logic when Stripe webhook events are received.
 */
@Service
public class StripeWebhookCommandServiceImpl {

    // private final SubscriptionRepository subscriptionRepository;

    public StripeWebhookCommandServiceImpl() {
        // Initialization
    }

    @Transactional
    public void handlePaymentSucceeded(String externalSubscriptionId, String customerId) {
        // 1. Find subscription by externalSubscriptionId using repository
        // 2. Fetch the associated Plan
        // 3. Call subscription.subscribeToPlanWithProvider(plan, externalSubscriptionId, PaymentProvider.STRIPE)
        // 4. Save subscription
        System.out.println("Handling invoice.payment_succeeded for " + externalSubscriptionId);
    }

    @Transactional
    public void handleSubscriptionDeleted(String externalSubscriptionId) {
        // 1. Find subscription by externalSubscriptionId
        // 2. Call subscription.cancel()
        // 3. Save subscription
        System.out.println("Handling customer.subscription.deleted for " + externalSubscriptionId);
    }
}
