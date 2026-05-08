package io.github.rafaviv.yakubackend.equipment.interfaces.acl;

import io.github.rafaviv.yakubackend.equipment.infrastructure.persistence.jpa.repositories.FarmRepository;
import org.springframework.stereotype.Service;

@Service
public class EquipmentContextFacadeImpl implements EquipmentContextFacade {

    private final FarmRepository farmRepository;

    public EquipmentContextFacadeImpl(FarmRepository farmRepository) {
        this.farmRepository = farmRepository;
    }

    @Override
    public boolean isValidAndUnusedFarmToken(String token) {
        return farmRepository.findByFarmToken(token).isPresent();
    }

    @Override
    public java.util.Optional<Long> findFarmIdByToken(String token) {
        return farmRepository.findByFarmToken(token).map(io.github.rafaviv.yakubackend.equipment.domain.model.aggregates.Farm::getId);
    }
}
