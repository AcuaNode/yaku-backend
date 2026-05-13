package io.github.rafaviv.yakubackend.equipment.domain.model.aggregates;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FarmTest {

    @Test
    @DisplayName("Given valid farm data, When creating farm, Then it is created with generated token")
    void createFarm_Successfully() {
        String name = "My Fish Farm";
        Long ownerId = 1L;
        String address = "123 Farm Street";

        Farm farm = new Farm(name, ownerId, address);

        assertEquals(name, farm.getName());
        assertEquals(ownerId, farm.getOwnerId());
        assertEquals(address, farm.getAddress());
        assertNotNull(farm.getFarmToken());
        assertFalse(farm.getFarmToken().isEmpty());
    }

    @Test
    @DisplayName("Given farm, When regenerating token, Then token changes")
    void regenerateFarmToken_Successfully() {
        Farm farm = new Farm("My Fish Farm", 1L, "123 Farm Street");
        String originalToken = farm.getFarmToken();

        farm.regenerateFarmToken();

        assertNotEquals(originalToken, farm.getFarmToken());
        assertNotNull(farm.getFarmToken());
    }

    @Test
    @DisplayName("Given two farms, When creating, Then each has unique token")
    void createFarm_UniqueTokens() {
        Farm farm1 = new Farm("Farm 1", 1L, "Address 1");
        Farm farm2 = new Farm("Farm 2", 2L, "Address 2");

        assertNotEquals(farm1.getFarmToken(), farm2.getFarmToken());
    }

    @Test
    @DisplayName("Given default constructor, When creating farm, Then creates empty farm")
    void createFarm_DefaultConstructor() {
        Farm farm = new Farm();

        assertNull(farm.getId());
        assertNull(farm.getName());
        assertNull(farm.getOwnerId());
    }
}
