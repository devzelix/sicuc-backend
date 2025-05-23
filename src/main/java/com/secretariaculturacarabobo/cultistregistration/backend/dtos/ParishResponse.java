package com.secretariaculturacarabobo.cultistregistration.backend.dtos;

/**
 * DTO for transferring Parish data in API responses.
 */
public class ParishResponse {

    // Unique identifier for the parish
    private int id;

    // Name of the parish
    private String name;

    // ID of the municipality this parish belongs to
    private int municipalityId;

    /**
     * Constructor to initialize all fields of ParishResponse.
     */
    public ParishResponse(int id, String name, int municipalityId) {
        this.id = id;
        this.name = name;
        this.municipalityId = municipalityId;
    }

    // Getters and setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMunicipalityId() {
        return municipalityId;
    }

    public void setMunicipalityId(int municipalityId) {
        this.municipalityId = municipalityId;
    }

}
