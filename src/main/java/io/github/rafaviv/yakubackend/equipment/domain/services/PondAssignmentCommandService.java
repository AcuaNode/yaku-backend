package io.github.rafaviv.yakubackend.equipment.domain.services;

import io.github.rafaviv.yakubackend.equipment.domain.model.aggregates.PondAssignment;

import java.util.Optional;

public interface PondAssignmentCommandService {
    Optional<PondAssignment> assignFishFarmerToPond(Long pondId, Long fishFarmerId);
    void deassignFishFarmerFromPond(Long pondId, Long fishFarmerId);
}
