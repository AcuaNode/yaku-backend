package io.github.rafaviv.yakubackend.equipment.domain.model.aggregates;

import io.github.rafaviv.yakubackend.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "pond_assignments")
public class PondAssignment extends AuditableAbstractAggregateRoot<PondAssignment> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long pondId;

    @Column(nullable = false)
    private Long fishFarmerId;

    @Column(nullable = false)
    private LocalDateTime startDate;

    private LocalDateTime endDate;

    public PondAssignment() {
    }

    public PondAssignment(Long pondId, Long fishFarmerId) {
        this.pondId = pondId;
        this.fishFarmerId = fishFarmerId;
        this.startDate = LocalDateTime.now();
    }

    public void endAssignment() {
        this.endDate = LocalDateTime.now();
    }
}
