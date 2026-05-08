package io.github.rafaviv.yakubackend.equipment.domain.services;

import io.github.rafaviv.yakubackend.equipment.domain.model.aggregates.Farm;
import io.github.rafaviv.yakubackend.equipment.domain.model.queries.GetFarmsByOwnerIdQuery;

import java.util.List;

public interface FarmQueryService {
    List<Farm> handle(GetFarmsByOwnerIdQuery query);
}
