package io.github.rafaviv.yakubackend.notification.infrastructure.rest;

import org.springframework.stereotype.Service;
import io.github.rafaviv.yakubackend.notification.domain.models.aggregates.Notification;
import io.github.rafaviv.yakubackend.notification.domain.services.PushNotificationService;
import java.util.List;

@Service
public class FcmClient implements PushNotificationService {

    @Override
    public void sendNotification(Notification notification, List<String> fcmTokens) {
        if (fcmTokens == null || fcmTokens.isEmpty()) {
            System.out.println("[FCM MOCK] No se puede enviar: El usuario " + notification.getRecipient().userId() + " no tiene tokens registrados.");
            return;
        }

        for (String token : fcmTokens) {
            System.out.println("[FCM MOCK] Enviando notificación push a Token: " 
                + token + " | Mensaje: " + notification.getMessage());
        }
    }
}
