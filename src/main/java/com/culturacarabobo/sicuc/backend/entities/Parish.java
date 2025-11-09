package com.culturacarabobo.sicuc.backend.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint; // Import for Javadoc

/**
 * Represents a Parish entity (e.g., "San José", "Candelaria").
 * <p>
 * This entity maps to the {@code parishes} table. Each parish
 * must be associated with a parent {@link Municipality}.
 * <p>
 * A {@link UniqueConstraint} ensures that a parish name is unique
 * *within* its specific municipality.
 */
@Entity
@Table(name = "parishes", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "name", "municipality_id" })
})
public class Parish {

    /**
     * The unique identifier (primary key) for the parish.
     * Auto-incremented by the database.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    /**
     * The name of the parish (e.g., "San José"). Required.
     */
    @Column(length = 100, nullable = false)
    private String name;

    /**
     * The parent {@link Municipality} this parish belongs to.
     * This is a many-to-one relationship (e.g., many parishes -> one
     * municipality).
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "municipality_id", nullable = false)
    private Municipality municipality;

    /**
     * Default constructor required by JPA.
     */
    public Parish() {
    }

    /**
     * Convenience constructor to create a new Parish with a name and
     * parent municipality.
     *
     * @param name         The name of the parish.
     * @param municipality The associated {@link Municipality}.
     */
    public Parish(String name, Municipality municipality) {
        this.name = name;
        this.municipality = municipality;
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

    public Municipality getMunicipality() {
        return municipality;
    }

    public void setMunicipality(Municipality municipality) {
        this.municipality = municipality;
    }

}