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

@Entity // Marks this class as a JPA entity
@Table(name = "parishes", uniqueConstraints = {
        @jakarta.persistence.UniqueConstraint(columnNames = { "name", "municipality_id" })
}) // Ensures that each parish name is unique within a municipality
public class Parish {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-incremented primary key
    private int id;

    @Column(length = 100, nullable = false) // Name column with max length and not null constraint
    private String name;

    @ManyToOne(fetch = FetchType.LAZY) // Many parishes can belong to one municipality; lazy loading for performance
    @JoinColumn(name = "municipality_id", nullable = false) // Foreign key column, not nullable
    private Municipality municipality;

    /**
     * Default constructor required by JPA.
     */
    public Parish() {
    }

    /**
     * Convenience constructor to create a Parish with a name and municipality.
     *
     * @param name         the parish name
     * @param municipality the associated municipality
     */
    public Parish(String name, Municipality municipality) {
        this.name = name;
        this.municipality = municipality;
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

    public Municipality getMunicipality() {
        return municipality;
    }

    public void setMunicipality(Municipality municipality) {
        this.municipality = municipality;
    }

}
