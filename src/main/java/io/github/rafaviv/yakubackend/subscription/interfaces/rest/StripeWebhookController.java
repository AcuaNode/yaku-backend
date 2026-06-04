package io.github.rafaviv.yakubackend.subscription.interfaces.rest;

import io.github.rafaviv.yakubackend.subscription.infrastructure.adapters.stripe.StripeWebhookCommandServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.stripe.model.Event;
import com.stripe.net.Webhook;
import com.stripe.model.checkout.Session;
import org.springframework.beans.factory.annotation.Value;

@RestController
@RequestMapping("/api/v1/webhooks/stripe")
public class StripeWebhookController {

    private final StripeWebhookCommandServiceImpl stripeWebhookCommandService;

    @Value("${stripe.webhook.secret}")
    private String webhookSecret;

    public StripeWebhookController(StripeWebhookCommandServiceImpl stripeWebhookCommandService) {
        this.stripeWebhookCommandService = stripeWebhookCommandService;
    }

    @PostMapping
    public ResponseEntity<String> handleStripeWebhook(
            @RequestBody String payload,
            @RequestHeader("Stripe-Signature") String sigHeader) {

        try {
            // 1. Verify Stripe signature
            Event event = Webhook.constructEvent(payload, sigHeader, webhookSecret);

            // 2. Delegate to command service based on event.getType()
            if ("checkout.session.completed".equals(event.getType())) {
                Session session = (Session) event.getDataObjectDeserializer().getObject().orElse(null);
                if (session != null) {
                    String userIdStr = session.getClientReferenceId();
                    String planIdStr = session.getMetadata().get("planId");
                    String subscriptionId = session.getSubscription();
                    stripeWebhookCommandService.handleCheckoutSessionCompleted(userIdStr, planIdStr, subscriptionId);
                }
            }

            return ResponseEntity.ok().build();
        } catch (Exception e) {
            System.err.println("Webhook error: " + e.getMessage());
            return ResponseEntity.badRequest().body("Webhook error");
        }
    }
}
