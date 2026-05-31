package io.github.rafaviv.yakubackend.notification.infrastructure.rest;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.MulticastMessage;
import com.google.firebase.messaging.BatchResponse;
import com.google.firebase.messaging.AndroidConfig;
import org.springframework.stereotype.Service;
import io.github.rafaviv.yakubackend.notification.domain.models.aggregates.Notification;
import io.github.rafaviv.yakubackend.notification.domain.services.PushNotificationService;
import java.util.List;

@Service
public class FcmClient implements PushNotificationService {

    @Override
    public void sendNotification(Notification notification, List<String> fcmTokens) {
        if (fcmTokens == null || fcmTokens.isEmpty()) {
            System.out.println("No se puede enviar push: El usuario " + notification.getRecipient().userId() + " no tiene tokens registrados.");
            return;
        }

        try {
            // Construimos la notificación push multicast de Firebase
            MulticastMessage message = MulticastMessage.builder()
                    .addAllTokens(fcmTokens)
                    .setNotification(com.google.firebase.messaging.Notification.builder()
                            .setTitle("Alerta de Poza: " + notification.getType())
                            .setBody(notification.getMessage())
                            .build())
                    .putData("notificationId", String.valueOf(notification.getId()))
                    .putData("type", notification.getType().name())
                    .setAndroidConfig(AndroidConfig.builder()
                            .setPriority(AndroidConfig.Priority.HIGH)
                            .build())
                    .build();

            // Enviamos de forma asíncrona a todos los dispositivos registrados
            BatchResponse response = FirebaseMessaging.getInstance().sendEachForMulticast(message);
            System.out.println("Enviadas exitosamente: " + response.getSuccessCount() + " notificaciones. Fallidas: " + response.getFailureCount());
        } catch (Exception e) {
            System.err.println("Fallo al enviar notificación multicast a Firebase: " + e.getMessage());
        }
    }
}
