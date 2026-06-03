package io.github.rafaviv.yakubackend.subscription.domain.ports;

public interface ExternalPaymentGateway {
    String generatePaymentIntent(Long amountInCents, String currency);
}
