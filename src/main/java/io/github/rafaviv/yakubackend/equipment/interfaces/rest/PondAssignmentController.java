package io.github.rafaviv.yakubackend.equipment.interfaces.rest;

import io.github.rafaviv.yakubackend.equipment.domain.services.PondAssignmentCommandService;
import io.github.rafaviv.yakubackend.equipment.interfaces.rest.transform.AssignFishFarmerResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/ponds/{pondId}")
public class PondAssignmentController {

    private final PondAssignmentCommandService pondAssignmentCommandService;

    public PondAssignmentController(PondAssignmentCommandService pondAssignmentCommandService) {
        this.pondAssignmentCommandService = pondAssignmentCommandService;
    }

    @PostMapping("/assignments")
    public ResponseEntity<?> assignFishFarmer(@PathVariable Long pondId, @RequestBody AssignFishFarmerResource resource) {
        var assignment = pondAssignmentCommandService.assignFishFarmerToPond(pondId, resource.fishFarmerId());
        if (assignment.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(assignment.get());
    }

    @DeleteMapping("/deassignments/{operatorId}")
    public ResponseEntity<?> deassignFishFarmer(@PathVariable Long pondId, @PathVariable Long operatorId) {
        try {
            pondAssignmentCommandService.deassignFishFarmerFromPond(pondId, operatorId);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
