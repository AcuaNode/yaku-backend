package io.github.rafaviv.yakubackend.notification.interfaces.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.github.rafaviv.yakubackend.notification.domain.models.queries.ListNotificationsQuery;
import io.github.rafaviv.yakubackend.notification.application.internal.queryservices.ListNotificationsQueryService;
import io.github.rafaviv.yakubackend.notification.interfaces.rest.resources.NotificationResponseResource;
import io.github.rafaviv.yakubackend.notification.interfaces.rest.transform.NotificationResourceMapper;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/users/{userId}/notifications")
public class NotificationsController {

    private final ListNotificationsQueryService listNotificationsQueryHandler;

    public NotificationsController(ListNotificationsQueryService listNotificationsQueryHandler) {
        this.listNotificationsQueryHandler = listNotificationsQueryHandler;
    }

    @GetMapping
    public ResponseEntity<List<NotificationResponseResource>> getNotificationsByUserId(@PathVariable Long userId) {
        var query = new ListNotificationsQuery(userId);
        var notifications = listNotificationsQueryHandler.handle(query);
        
        var responseResources = notifications.stream()
                .map(NotificationResourceMapper::toResource)
                .collect(Collectors.toList());
                
        return ResponseEntity.ok(responseResources);
    }
}
