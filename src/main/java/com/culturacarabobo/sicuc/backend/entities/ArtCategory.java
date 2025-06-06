package com.culturacarabobo.sicuc.backend.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity // Marks this class as a JPA entity
@Table(name = "art_categories") // Maps the entity to the "art_categories" table
public class ArtCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-incremented primary key
    private int id;

    @Column(length = 100, nullable = false, unique = true) // Column must be unique, non-null, max length 100
    private String name;

    /**
     * Default constructor required by JPA.
     */
    public ArtCategory() {
    }

    /**
     * Convenience constructor to create an ArtCategory with a name.
     *
     * @param name the name of the art category
     */
    public ArtCategory(String name) {
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
