package io.github.rafaviv.yakubackend.subscription.infrastructure.connector;

import com.stripe.Stripe;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import io.github.rafaviv.yakubackend.subscription.domain.ports.ExternalPaymentGateway;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import jakarta.annotation.PostConstruct;

@Component
public class StripePaymentConnector implements ExternalPaymentGateway {

    @Value("sk_test_51TaSZKEyn9JTtfs5vSxxdR8xCTvPhqpsO26dNHk9C30Bejv8PNjHERtKy0Rh5s0Oe0MpOvg51bx9TV2SDPctQkQs00fUAIB4Nq")
    private String secretKey;

    @PostConstruct
    public void init() {
        Stripe.apiKey = secretKey;
    }

    @Override
    public String generatePaymentIntent(Long amountInCents, String currency) {
        try {
            // Configura los parámetros requeridos por Stripe
            PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                    .setAmount(amountInCents) // Stripe procesa en centavos (ej: $20.00 = 2000)
                    .setCurrency(currency.toLowerCase()) // ej: "usd" o "pen"
                    .setAutomaticPaymentMethods(
                            PaymentIntentCreateParams.AutomaticPaymentMethods.builder()
                                    .setEnabled(true)
                                    .build()
                    )
                    .build();

            // Llama a la API externa de Stripe
            PaymentIntent paymentIntent = PaymentIntent.create(params);

            // Retorna el client_secret que necesita tu Frontend (React/Flutter)
            return paymentIntent.getClientSecret();

        } catch (Exception e) {
            throw new RuntimeException("Error en la pasarela de pagos externa al generar el intento de cobro", e);
        }
    }
}