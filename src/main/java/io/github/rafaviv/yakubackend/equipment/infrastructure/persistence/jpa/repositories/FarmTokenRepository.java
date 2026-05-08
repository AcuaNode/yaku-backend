package io.github.rafaviv.yakubackend.equipment.infrastructure.persistence.jpa.repositories;

import io.github.rafaviv.yakubackend.equipment.domain.model.aggregates.FarmToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FarmTokenRepository extends JpaRepository<FarmToken, Long> {
    Optional<FarmToken> findByToken(String token);
}
