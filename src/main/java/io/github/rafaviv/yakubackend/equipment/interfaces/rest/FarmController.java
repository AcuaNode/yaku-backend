package io.github.rafaviv.yakubackend.equipment.interfaces.rest;

import io.github.rafaviv.yakubackend.equipment.domain.model.commands.CreateFarmCommand;
import io.github.rafaviv.yakubackend.equipment.domain.model.queries.GetFarmsByOwnerIdQuery;
import io.github.rafaviv.yakubackend.equipment.domain.services.FarmCommandService;
import io.github.rafaviv.yakubackend.equipment.domain.services.FarmQueryService;
import io.github.rafaviv.yakubackend.equipment.interfaces.rest.resources.CreateFarmResource;
import io.github.rafaviv.yakubackend.equipment.interfaces.rest.resources.FarmResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/farms")
public class FarmController {

    private final FarmCommandService farmCommandService;
    private final FarmQueryService farmQueryService;

    public FarmController(FarmCommandService farmCommandService, FarmQueryService farmQueryService) {
        this.farmCommandService = farmCommandService;
        this.farmQueryService = farmQueryService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<FarmResource> createFarm(@RequestBody CreateFarmResource resource, org.springframework.security.core.Authentication authentication) {
        io.github.rafaviv.yakubackend.iam.infrastructure.authorization.sfs.model.UserDetailsImpl userDetails = 
            (io.github.rafaviv.yakubackend.iam.infrastructure.authorization.sfs.model.UserDetailsImpl) authentication.getPrincipal();
        Long adminId = userDetails.getId();
        
        CreateFarmCommand command = new CreateFarmCommand(resource.name(), adminId, resource.address());
        var farm = farmCommandService.handle(command);
        if (farm.isEmpty()) return ResponseEntity.badRequest().build();
        
        var createdFarm = farm.get();
        var farmResource = new FarmResource(createdFarm.getId(), createdFarm.getName(), createdFarm.getOwnerId(), createdFarm.getAddress());
        
        return new ResponseEntity<>(farmResource, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteFarm(@PathVariable Long id) {
        try {
            farmCommandService.deleteFarm(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }



    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<FarmResource>> getAllFarmsByOwner(org.springframework.security.core.Authentication authentication) {
        io.github.rafaviv.yakubackend.iam.infrastructure.authorization.sfs.model.UserDetailsImpl userDetails = 
            (io.github.rafaviv.yakubackend.iam.infrastructure.authorization.sfs.model.UserDetailsImpl) authentication.getPrincipal();
        Long adminId = userDetails.getId();
        
        var query = new GetFarmsByOwnerIdQuery(adminId);
        var farms = farmQueryService.handle(query);
        var resources = farms.stream()
                .map(farm -> new FarmResource(farm.getId(), farm.getName(), farm.getOwnerId(), farm.getAddress()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(resources);
    }

}
