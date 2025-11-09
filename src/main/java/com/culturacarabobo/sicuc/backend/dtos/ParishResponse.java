package com.culturacarabobo.sicuc.backend.dtos;

/**
 * Data Transfer Object (DTO) for sending {@link com.culturacarabobo.sicuc.backend.entities.Parish}
 * data to the client.
 * <p>
 * This is an immutable data carrier class.
 */
public final class ParishResponse {

    /**
     * The unique identifier (primary key) for the parish.
     */
    private final int id;

    /**
     * The name of the parish (e.g., "San José").
     */
    private final String name;

    /**
     * The foreign key ID of the parent {@link com.culturacarabobo.sicuc.backend.entities.Municipality}.
     */
    private final int municipalityId;

    /**
     * Constructs a new ParishResponse.
     *
     * @param id             The ID of the parish.
     * @param name           The name of the parish.
     * @param municipalityId The ID of the parent municipality.
     */
    public ParishResponse(int id, String name, int municipalityId) {
        this.id = id;
        this.name = name;
        this.municipalityId = municipalityId;
    }

    // --- Standard Getters ---
    // (No setters are provided, as this is an immutable DTO)

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getMunicipalityId() {
        return municipalityId;
    }
}