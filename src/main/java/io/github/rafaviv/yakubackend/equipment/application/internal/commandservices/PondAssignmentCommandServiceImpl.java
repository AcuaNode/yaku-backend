package io.github.rafaviv.yakubackend.equipment.application.internal.commandservices;

import io.github.rafaviv.yakubackend.equipment.domain.model.aggregates.Pond;
import io.github.rafaviv.yakubackend.equipment.domain.model.aggregates.PondAssignment;
import io.github.rafaviv.yakubackend.equipment.domain.model.events.FishFarmerAssignedEvent;
import io.github.rafaviv.yakubackend.equipment.domain.services.PondAssignmentCommandService;
import io.github.rafaviv.yakubackend.equipment.infrastructure.events.SpringDomainEventPublisher;
import io.github.rafaviv.yakubackend.equipment.infrastructure.persistence.jpa.repositories.PondAssignmentRepository;
import io.github.rafaviv.yakubackend.equipment.infrastructure.persistence.jpa.repositories.PondRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class PondAssignmentCommandServiceImpl implements PondAssignmentCommandService {

    private final PondAssignmentRepository pondAssignmentRepository;
    private final PondRepository pondRepository;
    private final SpringDomainEventPublisher eventPublisher;

    public PondAssignmentCommandServiceImpl(PondAssignmentRepository pondAssignmentRepository, PondRepository pondRepository, SpringDomainEventPublisher eventPublisher) {
        this.pondAssignmentRepository = pondAssignmentRepository;
        this.pondRepository = pondRepository;
        this.eventPublisher = eventPublisher;
    }

    @Override
    @Transactional
    public Optional<PondAssignment> assignFishFarmerToPond(Long pondId, Long fishFarmerId) {
        Pond pond = pondRepository.findById(pondId)
                .orElseThrow(() -> new IllegalArgumentException("Pond not found"));

        // End current assignments if any active exist
        var currentAssignments = pondAssignmentRepository.findByPondId(pondId);
        currentAssignments.stream()
                .filter(assignment -> assignment.getEndDate() == null)
                .forEach(assignment -> {
                    assignment.endAssignment();
                    pondAssignmentRepository.save(assignment);
                });

        // Update pond assigned farmer
        pond.assignFishFarmer(fishFarmerId);
        pondRepository.save(pond);

        // Create new assignment
        PondAssignment newAssignment = new PondAssignment(pondId, fishFarmerId);
        PondAssignment savedAssignment = pondAssignmentRepository.save(newAssignment);

        // Publish event
        eventPublisher.publish(new FishFarmerAssignedEvent(pondId, fishFarmerId));

        return Optional.of(savedAssignment);
    }

    @Override
    @Transactional
    public void deassignFishFarmerFromPond(Long pondId, Long fishFarmerId) {
        var assignment = pondAssignmentRepository.findByPondIdAndFishFarmerIdAndEndDateIsNull(pondId, fishFarmerId)
                .orElseThrow(() -> new IllegalArgumentException("Active assignment not found for pond and fish farmer"));

        assignment.endAssignment();
        pondAssignmentRepository.save(assignment);

        Pond pond = pondRepository.findById(pondId)
                .orElseThrow(() -> new IllegalArgumentException("Pond not found"));
        pond.assignFishFarmer(null);
        pondRepository.save(pond);
    }
}
