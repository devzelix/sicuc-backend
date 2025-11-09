package com.culturacarabobo.sicuc.backend.dtos;

/**
 * Data Transfer Object (DTO) for sending {@link com.culturacarabobo.sicuc.backend.entities.ArtDiscipline}
 * data to the client.
 * <p>
 * This is an immutable data carrier class.
 */
public final class ArtDisciplineResponse {

    /**
     * The unique identifier (primary key) for the art discipline.
     */
    private final int id;

    /**
     * The name of the art discipline (e.g., "Guitarra").
     */
    private final String name;

    /**
     * The foreign key ID of the parent {@link com.culturacarabobo.sicuc.backend.entities.ArtCategory}.
     */
    private final int artCategoryId;

    /**
     * Constructs a new ArtDisciplineResponse.
     *
     * @param id            The ID of the discipline.
     * @param name          The name of the discipline.
     * @param artCategoryId The ID of the parent category.
     */
    public ArtDisciplineResponse(int id, String name, int artCategoryId) {
        this.id = id;
        this.name = name;
        this.artCategoryId = artCategoryId;
    }

    // --- Standard Getters ---
    // (No setters are provided, as this is an immutable DTO)

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getArtCategoryId() {
        return artCategoryId;
    }
}