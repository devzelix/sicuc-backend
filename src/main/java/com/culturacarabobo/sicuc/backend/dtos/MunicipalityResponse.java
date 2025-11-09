package com.culturacarabobo.sicuc.backend.dtos;

/**
 * Data Transfer Object (DTO) for sending {@link com.culturacarabobo.sicuc.backend.entities.Municipality}
 * data to the client.
 * <p>
 * This is an immutable data carrier class.
 */
public final class MunicipalityResponse {

    /**
     * The unique identifier (primary key) for the municipality.
     */
    private final int id;

    /**
     * The name of the municipality (e.g., "Valencia").
     */
    private final String name;

    /**
     * Constructs a new MunicipalityResponse.
     *
     * @param id   The ID of the municipality.
     * @param name The name of the municipality.
     */
    public MunicipalityResponse(int id, String name) {
        this.id = id;
        this.name = name;
    }

    // --- Standard Getters ---
    // (No setters are provided, as this is an immutable DTO)

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}