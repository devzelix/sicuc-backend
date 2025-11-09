package com.culturacarabobo.sicuc.backend.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Represents a Municipality entity (e.g., "Valencia", "Naguanagua").
 * <p>
 * This entity maps to the {@code municipalities} table and serves as a
 * lookup table for classifying {@link Parish} and {@link Cultor} entities.
 */
@Entity
@Table(name = "municipalities")
public class Municipality {

    /**
     * The unique identifier (primary key) for the municipality.
     * Auto-incremented by the database.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    /**
     * The name of the municipality (e.g., "Valencia").
     * Must be unique and non-null.
     */
    @Column(length = 100, nullable = false, unique = true)
    private String name;

    /**
     * Default constructor required by JPA.
     */
    public Municipality() {
    }

    /**
     * Convenience constructor to create a new Municipality with a name.
     *
     * @param name The name of the municipality.
     */
    public Municipality(String name) {
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