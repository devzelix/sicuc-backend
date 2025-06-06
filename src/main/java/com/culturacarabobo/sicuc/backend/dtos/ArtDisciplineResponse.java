package com.culturacarabobo.sicuc.backend.dtos;

/**
 * Data Transfer Object for Art Discipline response.
 */
public class ArtDisciplineResponse {

    // Unique identifier of the art discipline
    private int id;

    // Name of the art discipline
    private String name;

    // ID of the related art category
    private int artCategoryId;

    /**
     * Constructor to initialize all fields of ArtDisciplineResponse.
     */
    public ArtDisciplineResponse(int id, String name, int artCategoryId) {
        this.id = id;
        this.name = name;
        this.artCategoryId = artCategoryId;
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

    public int getArtCategoryId() {
        return artCategoryId;
    }

    public void setArtCategoryId(int artCategoryId) {
        this.artCategoryId = artCategoryId;
    }

}
