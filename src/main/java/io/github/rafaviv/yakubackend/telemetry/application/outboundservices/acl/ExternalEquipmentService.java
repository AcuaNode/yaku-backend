package io.github.rafaviv.yakubackend.telemetry.application.outboundservices.acl;

/**
 * Port for the Telemetry Bounded Context to communicate with external Equipment Context.
 * Resolves necessary information without coupling to Equipment's internal structures.
 */
public interface ExternalEquipmentService {
    
    /**
     * Retrieves the species name assigned to a specific pond.
     *
     * @param pondId The identifier of the pond
     * @return The species name as a plain string (e.g., "TRUCHA", "TILAPIA")
     */
    String getSpeciesByPondId(Long pondId);
}
