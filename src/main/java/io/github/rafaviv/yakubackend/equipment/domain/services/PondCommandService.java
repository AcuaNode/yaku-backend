package io.github.rafaviv.yakubackend.equipment.domain.services;

import io.github.rafaviv.yakubackend.equipment.domain.model.aggregates.Pond;

import java.util.Optional;

public interface PondCommandService {
    Optional<Pond> createPond(Long farmId, String name, String species, Double volume);
    void deletePond(Long pondId);
    Optional<Pond> updatePond(Long pondId, String name, String species, Double volume);
}
