package com.culturacarabobo.sicuc.backend.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Represents an Art Category entity (e.g., "Música", "Artesanía", "Danza").
 * <p>
 * This entity maps to the {@code art_categories} table and serves as a
 * lookup table for classifying {@link ArtDiscipline} and {@link Cultor} entities.
 */
@Entity
@Table(name = "art_categories")
public class ArtCategory {

    /**
     * The unique identifier (primary key) for the art category.
     * Auto-incremented by the database.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    /**
     * The name of the art category (e.g., "Música").
     * Must be unique and non-null.
     */
    @Column(length = 100, nullable = false, unique = true)
    private String name;

    /**
     * Default constructor required by JPA.
     */
    public ArtCategory() {
    }

    /**
     * Convenience constructor to create a new ArtCategory with a name.
     *
     * @param name The name of the art category.
     */
    public ArtCategory(String name) {
        this.name = name;
    }

    // --- Standard Getters and Setters ---

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