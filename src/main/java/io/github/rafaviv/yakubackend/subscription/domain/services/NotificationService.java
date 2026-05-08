package io.github.rafaviv.yakubackend.subscription.domain.services;

public interface NotificationService {
    void sendNotification(Long userId, String message);
}
