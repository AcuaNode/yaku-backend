package io.github.rafaviv.yakubackend.equipment.application.internal.eventhandlers;

import io.github.rafaviv.yakubackend.equipment.domain.model.aggregates.PondAssignment;
import io.github.rafaviv.yakubackend.equipment.infrastructure.persistence.jpa.repositories.PondAssignmentRepository;
import io.github.rafaviv.yakubackend.equipment.infrastructure.persistence.jpa.repositories.PondRepository;
import io.github.rafaviv.yakubackend.equipment.infrastructure.persistence.jpa.repositories.FarmRepository;
import io.github.rafaviv.yakubackend.iam.domain.model.events.UserRegisteredEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserRegisteredEventHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserRegisteredEventHandler.class);

    private final PondAssignmentRepository pondAssignmentRepository;
    private final PondRepository pondRepository;
    private final FarmRepository farmRepository;

    public UserRegisteredEventHandler(PondAssignmentRepository pondAssignmentRepository,
            PondRepository pondRepository,
            FarmRepository farmRepository) {
        this.pondAssignmentRepository = pondAssignmentRepository;
        this.pondRepository = pondRepository;
        this.farmRepository = farmRepository;
    }

    @EventListener
    @Transactional
    public void on(UserRegisteredEvent event) {
        if (event.farmToken() != null && !event.farmToken().isBlank()) {
            LOGGER.info("Processing UserRegisteredEvent for User ID {} with FarmToken {}", event.userId(),
                    event.farmToken());

            farmRepository.findByFarmToken(event.farmToken()).ifPresent(farm -> {
                farm.regenerateFarmToken();
                farmRepository.save(farm);

                pondRepository.findByFarmId(farm.getId()).forEach(pond -> {
                    pond.assignFishFarmer(event.userId());
                    pondRepository.save(pond);

                    PondAssignment assignment = new PondAssignment(pond.getId(), event.userId());
                    pondAssignmentRepository.save(assignment);
                });

                LOGGER.info("Assigned user {} to ponds in farm {} and regenerated token", event.userId(), farm.getId());
            });
        }
    }
}
