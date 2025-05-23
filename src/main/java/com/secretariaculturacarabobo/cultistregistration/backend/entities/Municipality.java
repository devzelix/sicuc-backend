package com.secretariaculturacarabobo.cultistregistration.backend.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity // Marks this class as a JPA entity
@Table(name = "municipalities") // Maps the entity to the "municipalities" table
public class Municipality {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-incremented primary key
    private int id;

    @Column(length = 100, nullable = false, unique = true) // Column constraints: max length, not null, unique
    private String name;

    /**
     * Default constructor required by JPA.
     */
    public Municipality() {
    }

    /**
     * Convenience constructor for creating a Municipality with a name.
     *
     * @param name the name of the municipality
     */
    public Municipality(String name) {
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
