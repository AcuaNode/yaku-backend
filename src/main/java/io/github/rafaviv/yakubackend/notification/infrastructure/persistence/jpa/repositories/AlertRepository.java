package io.github.rafaviv.yakubackend.notification.infrastructure.persistence.jpa.repositories;

import io.github.rafaviv.yakubackend.notification.domain.model.aggregates.Alert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlertRepository extends JpaRepository<Alert, Long> {
}
