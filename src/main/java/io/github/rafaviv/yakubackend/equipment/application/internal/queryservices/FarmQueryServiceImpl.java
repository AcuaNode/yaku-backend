package io.github.rafaviv.yakubackend.equipment.application.internal.queryservices;

import io.github.rafaviv.yakubackend.equipment.domain.model.aggregates.Farm;
import io.github.rafaviv.yakubackend.equipment.domain.model.aggregates.Pond;
import io.github.rafaviv.yakubackend.equipment.domain.model.queries.GetFarmsByOwnerIdQuery;
import io.github.rafaviv.yakubackend.equipment.domain.services.FarmQueryService;
import io.github.rafaviv.yakubackend.equipment.infrastructure.persistence.jpa.repositories.FarmRepository;
import io.github.rafaviv.yakubackend.equipment.infrastructure.persistence.jpa.repositories.PondRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FarmQueryServiceImpl implements FarmQueryService {

    private final FarmRepository farmRepository;
    private final PondRepository pondRepository;

    public FarmQueryServiceImpl(FarmRepository farmRepository, PondRepository pondRepository) {
        this.farmRepository = farmRepository;
        this.pondRepository = pondRepository;
    }

    @Override
    public List<Farm> handle(GetFarmsByOwnerIdQuery query) {
        return farmRepository.findByOwnerId(query.ownerId());
    }

}
