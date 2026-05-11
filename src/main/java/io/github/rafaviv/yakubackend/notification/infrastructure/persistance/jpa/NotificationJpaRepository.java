package io.github.rafaviv.yakubackend.notification.infrastructure.persistance.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationJpaRepository extends JpaRepository<NotificationEntity, Long> {
    List<NotificationEntity> findByRecipientUserIdOrderByCreatedAtDesc(Long recipientUserId);
}
