package io.github.rafaviv.yakubackend.equipment.domain.model.aggregates;

import io.github.rafaviv.yakubackend.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
@Table(name = "farms")
public class Farm extends AuditableAbstractAggregateRoot<Farm> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Long ownerId; // Equivalent to ADMIN ID

    @Column(nullable = false, unique = true)
    private String farmToken;

    @Column
    private String address;

    public Farm() {
        // JPA requires a default constructor
    }

    public Farm(String name, Long ownerId, String address) {
        this.name = name;
        this.ownerId = ownerId;
        this.address = address;
        this.farmToken = java.util.UUID.randomUUID().toString();
    }

    public void regenerateFarmToken() {
        this.farmToken = java.util.UUID.randomUUID().toString();
    }
}
