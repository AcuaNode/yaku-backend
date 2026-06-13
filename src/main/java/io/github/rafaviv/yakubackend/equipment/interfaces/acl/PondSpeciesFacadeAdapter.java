package io.github.rafaviv.yakubackend.equipment.interfaces.acl;

import io.github.rafaviv.yakubackend.equipment.domain.model.aggregates.Pond;
import io.github.rafaviv.yakubackend.equipment.infrastructure.persistence.jpa.repositories.PondRepository;
import io.github.rafaviv.yakubackend.telemetry.application.outboundservices.acl.ExternalEquipmentService;
import org.springframework.stereotype.Component;

/**
 * Adapter that implements the ExternalEquipmentService port defined in the Telemetry context.
 * Serves as an ACL facade for Equipment context.
 */
@Component
public class PondSpeciesFacadeAdapter implements ExternalEquipmentService {

    private final PondRepository pondRepository;

    public PondSpeciesFacadeAdapter(PondRepository pondRepository) {
        this.pondRepository = pondRepository;
    }

    @Override
    public String getSpeciesByPondId(Long pondId) {
        Pond pond = pondRepository.findById(pondId)
                .orElseThrow(() -> new IllegalArgumentException("Pond with ID " + pondId + " not found"));
        
        if (pond.getSpecies() == null || pond.getSpecies().isBlank()) {
            throw new IllegalStateException("Pond with ID " + pondId + " does not have an assigned species");
        }
        
        return pond.getSpecies();
    }
}
