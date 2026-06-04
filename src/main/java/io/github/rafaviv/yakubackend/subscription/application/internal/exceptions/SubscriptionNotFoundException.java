package io.github.rafaviv.yakubackend.subscription.application.internal.exceptions;

public class SubscriptionNotFoundException extends RuntimeException {
  public SubscriptionNotFoundException(String message) {
    super(message);
  }
}
