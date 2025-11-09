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
 * Represents an Art Discipline entity (e.g., "Guitarra", "Pintura al Óleo").
 * <p>
 * This entity maps to the {@code art_disciplines} table. Each discipline
 * must be associated with a parent {@link ArtCategory}.
 * <p>
 * A {@link UniqueConstraint} ensures that a discipline name is unique
 * *within* its specific art category (e.g., "Canto" can exist in "Música"
 * but not twice).
 */
@Entity
@Table(name = "art_disciplines", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "name", "art_category_id" })
})
public class ArtDiscipline {

    /**
     * The unique identifier (primary key) for the art discipline.
     * Auto-incremented by the database.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    /**
     * The name of the art discipline (e.g., "Guitarra"). Required.
     */
    @Column(length = 100, nullable = false)
    private String name;

    /**
     * The parent {@link ArtCategory} this discipline belongs to.
     * This is a many-to-one relationship (e.g., many disciplines -> one category).
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "art_category_id", nullable = false)
    private ArtCategory artCategory;

    /**
     * Default constructor required by JPA.
     */
    public ArtDiscipline() {
    }

    /**
     * Convenience constructor to create a new ArtDiscipline with a name and
     * parent category.
     *
     * @param name        The name of the discipline.
     * @param artCategory The associated {@link ArtCategory}.
     */
    public ArtDiscipline(String name, ArtCategory artCategory) {
        this.name = name;
        this.artCategory = artCategory;
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

    public ArtCategory getArtCategory() {
        return artCategory;
    }

    public void setArtCategory(ArtCategory artCategory) {
        this.artCategory = artCategory;
    }
}