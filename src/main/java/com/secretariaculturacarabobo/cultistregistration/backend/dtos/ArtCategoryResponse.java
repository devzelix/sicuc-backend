package com.secretariaculturacarabobo.cultistregistration.backend.dtos;

/**
 * DTO for transferring ArtCategory data in API responses.
 */
public class ArtCategoryResponse {

    // Unique identifier for the art category
    private int id;

    // Name of the art category
    private String name;

    /**
     * Constructor to initialize all fields of ArtCategoryResponse.
     */
    public ArtCategoryResponse(int id, String name) {
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
