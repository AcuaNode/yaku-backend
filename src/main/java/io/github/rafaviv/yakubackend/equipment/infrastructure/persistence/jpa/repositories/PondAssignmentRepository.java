package io.github.rafaviv.yakubackend.equipment.infrastructure.persistence.jpa.repositories;

import io.github.rafaviv.yakubackend.equipment.domain.model.aggregates.PondAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PondAssignmentRepository extends JpaRepository<PondAssignment, Long> {
    List<PondAssignment> findByPondId(Long pondId);
    Optional<PondAssignment> findByPondIdAndFishFarmerIdAndEndDateIsNull(Long pondId, Long fishFarmerId);
}
