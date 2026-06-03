package io.github.rafaviv.yakubackend.subscription.interfaces.rest;

import io.github.rafaviv.yakubackend.subscription.application.internal.commandservices.StripeWebhookCommandServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.stripe.model.Event;

@RestController
@RequestMapping("/api/v1/webhooks/stripe")
public class StripeWebhookController {

    private final StripeWebhookCommandServiceImpl stripeWebhookCommandService;

    public StripeWebhookController(StripeWebhookCommandServiceImpl stripeWebhookCommandService) {
        this.stripeWebhookCommandService = stripeWebhookCommandService;
    }

    @PostMapping
    public ResponseEntity<String> handleStripeWebhook(
            @RequestBody String payload,
            @RequestHeader("Stripe-Signature") String sigHeader) {

        // 1. Verify Stripe signature (using Stripe SDK: Webhook.constructEvent)
        //Event event = Webhook.constructEvent(payload, sigHeader, "whsec_...");

        // 2. Delegate to command service based on event.getType()
        // switch (event.getType()) {
        //     case "invoice.payment_succeeded":
        //         // parse payload
        //         stripeWebhookCommandService.handlePaymentSucceeded(externalId, customerId);
        //         break;
        //     case "customer.subscription.deleted":
        //         stripeWebhookCommandService.handleSubscriptionDeleted(externalId);
        //         break;
        // }

        System.out.println("Received Stripe webhook event.");
        return ResponseEntity.ok().build();
    }
}
