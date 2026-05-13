    package io.github.rafaviv.yakubackend.subscription.domain.model.aggregates;

    import io.github.rafaviv.yakubackend.subscription.domain.model.entities.Plan;
    import io.github.rafaviv.yakubackend.subscription.domain.model.valueobjects.Currency;
    import io.github.rafaviv.yakubackend.subscription.domain.model.valueobjects.SubscriptionStatus;
    import org.junit.jupiter.api.DisplayName;
    import org.junit.jupiter.api.Test;

    import java.time.LocalDate;

    import static org.junit.jupiter.api.Assertions.*;

    class SubscriptionTest {

        @Test
        @DisplayName("Given a new user, When assigning a plan, Then subscription becomes active with correct period")
        void subscribeToPlan_Successfully() {
            // Arrange
            Long userId = 1L;
            Plan premiumPlan = new Plan("PREMIUM", 19.99, Currency.USD, 10, 30); // 30 days duration
            Subscription subscription = new Subscription(userId); // Initial status is CANCELLED

            // Act
            subscription.subscribeToPlan(premiumPlan);

            // Assert
            assertEquals(SubscriptionStatus.ACTIVE, subscription.getStatus(), "Status should be ACTIVE");
            assertEquals(premiumPlan, subscription.getPlan(), "Plan should be PREMIUM");
            assertNotNull(subscription.getPeriod(), "Period must be initialized");

            // Assert Business Rule: End date should be today + 30 days
            LocalDate expectedEndDate = LocalDate.now().plusDays(30);
            assertEquals(expectedEndDate, subscription.getPeriod().endDate(), "End date calculation is incorrect");
        }

        @Test
        @DisplayName("Given an active subscription, When cancelled, Then status becomes CANCELLED")
        void cancelSubscription_Successfully() {
            // Arrange
            Subscription subscription = new Subscription(1L);
            subscription.subscribeToPlan(new Plan("FREE", 0.0, Currency.USD, 1, 365));

            // Act
            subscription.cancel();

            // Assert
            assertEquals(SubscriptionStatus.CANCELLED, subscription.getStatus());
        }
    }