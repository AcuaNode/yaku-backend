package io.github.rafaviv.yakubackend.telemetry.infrastructure.persistence.jpa.repositories;

import io.github.rafaviv.yakubackend.telemetry.domain.model.aggregates.Threshold;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ThresholdRepository extends JpaRepository<Threshold, Long> {
    Optional<Threshold> findBySpecies(String species);
}
