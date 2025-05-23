package com.secretariaculturacarabobo.cultistregistration.backend.dtos;

/**
 * Data Transfer Object used to send municipality data in API responses.
 */
public class MunicipalityResponse {

    // Unique identifier for the municipality
    private int id;

    // Name of the municipality
    private String name;

    /**
     * Constructor to initialize the DTO with id and name.
     */
    public MunicipalityResponse(int id, String name) {
        this.id = id;
        this.name = name;
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

}
