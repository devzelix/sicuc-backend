package com.secretariaculturacarabobo.cultistregistration.backend.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity // Marks this class as a JPA entity
@Table(name = "art_disciplines", uniqueConstraints = {
        @jakarta.persistence.UniqueConstraint(columnNames = { "name", "art_category_id" })
}) // Ensures discipline names are unique within their category
public class ArtDiscipline {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-generated primary key
    private int id;

    @Column(length = 100, nullable = false) // Discipline name with max length and not-null constraint
    private String name;

    @ManyToOne(fetch = FetchType.LAZY) // Many disciplines can belong to one category; lazily loaded
    @JoinColumn(name = "art_category_id", nullable = false) // Foreign key to ArtCategory, not nullable
    private ArtCategory artCategory;

    /**
     * Default constructor required by JPA.
     */
    public ArtDiscipline() {
    }

    /**
     * Constructor to create an ArtDiscipline with a name and category.
     *
     * @param name        the name of the discipline
     * @param artCategory the associated art category
     */
    public ArtDiscipline(String name, ArtCategory artCategory) {
        this.name = name;
        this.artCategory = artCategory;
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

    public ArtCategory getArtCategory() {
        return artCategory;
    }

    public void setArtCategory(ArtCategory artCategory) {
        this.artCategory = artCategory;
    }

}
