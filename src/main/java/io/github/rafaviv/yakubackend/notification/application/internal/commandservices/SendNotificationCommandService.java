package io.github.rafaviv.yakubackend.notification.application.internal.commandservices;

import org.springframework.stereotype.Service;
import io.github.rafaviv.yakubackend.notification.domain.models.aggregates.Notification;
import io.github.rafaviv.yakubackend.notification.domain.models.valueobjects.RecipientInfo;
import io.github.rafaviv.yakubackend.notification.domain.models.valueobjects.TriggerSnapshot;
import io.github.rafaviv.yakubackend.notification.domain.services.NotificationRepository;
import io.github.rafaviv.yakubackend.notification.domain.services.PushNotificationService;
import io.github.rafaviv.yakubackend.notification.domain.services.DeviceTokenRepository;
import io.github.rafaviv.yakubackend.notification.domain.models.aggregates.DeviceToken;
import io.github.rafaviv.yakubackend.notification.domain.models.commands.SendNotificationCommand;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SendNotificationCommandService {
    private final NotificationRepository notificationRepository;
    private final PushNotificationService pushNotificationService;
    private final DeviceTokenRepository deviceTokenRepository;

    public SendNotificationCommandService(NotificationRepository notificationRepository, 
                                          PushNotificationService pushNotificationService,
                                          DeviceTokenRepository deviceTokenRepository) {
        this.notificationRepository = notificationRepository;
        this.pushNotificationService = pushNotificationService;
        this.deviceTokenRepository = deviceTokenRepository;
    }

    public void handle(SendNotificationCommand command) {
        RecipientInfo recipient = new RecipientInfo(command.userId(), command.role());
        TriggerSnapshot triggerData = new TriggerSnapshot(command.temperature(), command.ph(), command.hardwareStatus());
        
        Notification notification = new Notification(command.type(), command.message(), recipient, triggerData);
        
        Notification savedNotification = notificationRepository.save(notification);
        
        // Obtener los tokens del usuario
        List<String> tokens = deviceTokenRepository.findByUserId(command.userId())
                .stream()
                .map(DeviceToken::getFcmToken)
                .collect(Collectors.toList());
        
        pushNotificationService.sendNotification(savedNotification, tokens);
    }
}
