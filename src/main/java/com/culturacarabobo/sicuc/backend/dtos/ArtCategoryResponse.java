package com.culturacarabobo.sicuc.backend.dtos;

/**
 * Data Transfer Object (DTO) for sending {@link com.culturacarabobo.sicuc.backend.entities.ArtCategory}
 * data to the client.
 * <p>
 * This is an immutable data carrier class.
 */
public final class ArtCategoryResponse {

    /**
     * The unique identifier (primary key) for the art category.
     */
    private final int id;

    /**
     * The name of the art category (e.g., "Música").
     */
    private final String name;

    /**
     * Constructs a new ArtCategoryResponse.
     *
     * @param id   The ID of the category.
     * @param name The name of the category.
     */
    public ArtCategoryResponse(int id, String name) {
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