package io.github.rafaviv.yakubackend.equipment.interfaces.acl;

/**
 * Facade for the Equipment Bounded Context.
 * Exposes internal functionalities to other bounded contexts in a decoupled way.
 */
public interface EquipmentContextFacade {

    /**
     * Checks if a given farm token is valid and unused.
     * @param token the token string
     * @return true if valid and unused, false otherwise
     */
    boolean isValidAndUnusedFarmToken(String token);

    /**
     * Finds the farm ID associated with a given token.
     * @param token the token string
     * @return an Optional containing the farm ID if found, empty otherwise
     */
    java.util.Optional<Long> findFarmIdByToken(String token);
}
