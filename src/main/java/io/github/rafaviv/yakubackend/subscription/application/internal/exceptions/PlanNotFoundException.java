package io.github.rafaviv.yakubackend.subscription.application.internal.exceptions;

public class PlanNotFoundException extends RuntimeException {
  public PlanNotFoundException(String message) {
    super(message);
  }
}
