package io.github.rafaviv.yakubackend.equipment.domain.model.aggregates;

import io.github.rafaviv.yakubackend.equipment.domain.model.valueobjects.PondStatus;
import io.github.rafaviv.yakubackend.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
@Table(name = "ponds")
public class Pond extends AuditableAbstractAggregateRoot<Pond> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long farmId;

    @Column(nullable = false)
    private String name;

    private String species;

    @Column(nullable = false)
    private Double volume;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PondStatus status;

    public Pond() {
        // JPA requires a default constructor
    }

    public Pond(Long farmId, String name, String species, Double volume) {
        this.farmId = farmId;
        this.name = name;
        this.species = species;
        this.volume = volume;
        this.status = PondStatus.ACTIVE;
    }

    public void updateStatus(PondStatus status) {
        this.status = status;
    }
}
