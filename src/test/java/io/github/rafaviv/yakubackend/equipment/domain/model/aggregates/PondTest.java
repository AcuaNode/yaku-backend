package io.github.rafaviv.yakubackend.equipment.domain.model.aggregates;

import io.github.rafaviv.yakubackend.equipment.domain.model.valueobjects.PondStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PondTest {

    @Test
    @DisplayName("Given valid pond data, When creating pond, Then it is created with ACTIVE status")
    void createPond_Successfully() {
        Long farmId = 1L;
        String name = "Pond A";
        String species = "Tilapia";
        Double volume = 1000.0;

        Pond pond = new Pond(farmId, name, species, volume);

        assertEquals(farmId, pond.getFarmId());
        assertEquals(name, pond.getName());
        assertEquals(species, pond.getSpecies());
        assertEquals(volume, pond.getVolume());
        assertEquals(PondStatus.ACTIVE, pond.getStatus());
        assertNull(pond.getAssignedFishFarmerId());
    }

    @Test
    @DisplayName("Given pond, When assigning fish farmer, Then fish farmer is assigned")
    void assignFishFarmer_Successfully() {
        Pond pond = new Pond(1L, "Pond A", "Tilapia", 1000.0);
        Long fishFarmerId = 42L;

        pond.assignFishFarmer(fishFarmerId);

        assertEquals(fishFarmerId, pond.getAssignedFishFarmerId());
    }

    @Test
    @DisplayName("Given pond with fish farmer, When reassigning, Then fish farmer is updated")
    void assignFishFarmer_Reassign_Updates() {
        Pond pond = new Pond(1L, "Pond A", "Tilapia", 1000.0);
        pond.assignFishFarmer(42L);

        pond.assignFishFarmer(99L);

        assertEquals(99L, pond.getAssignedFishFarmerId());
    }

    @Test
    @DisplayName("Given active pond, When updating status, Then status changes")
    void updateStatus_Successfully() {
        Pond pond = new Pond(1L, "Pond A", "Tilapia", 1000.0);

        pond.updateStatus(PondStatus.FULL);

        assertEquals(PondStatus.FULL, pond.getStatus());
    }

    @Test
    @DisplayName("Given pond, When updating to all statuses, Then status changes correctly")
    void updateStatus_AllStatuses_Work() {
        Pond pond = new Pond(1L, "Pond A", "Tilapia", 1000.0);

        pond.updateStatus(PondStatus.ACTIVE);
        assertEquals(PondStatus.ACTIVE, pond.getStatus());

        pond.updateStatus(PondStatus.INACTIVE);
        assertEquals(PondStatus.INACTIVE, pond.getStatus());

        pond.updateStatus(PondStatus.FULL);
        assertEquals(PondStatus.FULL, pond.getStatus());
    }

    @Test
    @DisplayName("Given default constructor, When creating pond, Then creates empty pond")
    void createPond_DefaultConstructor() {
        Pond pond = new Pond();

        assertNull(pond.getId());
        assertNull(pond.getFarmId());
        assertNull(pond.getName());
    }
}
