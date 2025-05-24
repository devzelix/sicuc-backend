package com.culturacarabobo.cultorregistration.backend.entities;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity // Specifies that this class is a JPA entity
@Table(name = "users") // Maps this entity to the "users" table in the database
public class User implements UserDetails { // Implements Spring Security's UserDetails for authentication

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-generated primary key
    private int id;
    @Column(nullable = false, unique = true, length = 25) // Username must be unique and not null
    private String username;
    @Column(nullable = false, length = 255) // Password must not be null
    private String password;

    /**
     * Default constructor required by JPA.
     */
    public User() {
    }

    /**
     * All-arguments constructor for easier object creation.
     */
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    /**
     * Returns the authorities granted to the user.
     * Here, all users have a single role: ROLE_USER.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    // Getters and setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
