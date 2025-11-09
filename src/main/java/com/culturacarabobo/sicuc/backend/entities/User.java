package com.culturacarabobo.sicuc.backend.entities;

import java.util.Collection;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Represents a User entity, mapping to the {@code users} table.
 * <p>
 * This class serves a dual purpose:
 * 1. It is a JPA Entity for persisting user data.
 * 2. It implements {@link UserDetails} to integrate directly with Spring
 * Security for authentication and authorization.
 */
@Entity
@Table(name = "users")
public class User implements UserDetails {

    /**
     * The unique identifier (primary key) for the user.
     * Auto-incremented by the database.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * The user's username (e.g., "admin", "test@mail.com").
     * Must be unique and non-null. This field is used as the login identifier.
     */
    @Column(nullable = false, unique = true)
    private String username;

    /**
     * The user's hashed password.
     * This field stores the BCrypt-encoded password.
     */
    @Column(nullable = false)
    private String password;

    /**
     * The authorization role assigned to the user (e.g., "ROLE_ADMIN").
     * Stored as a string in the database (e.g., "ROLE_ADMIN").
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    // --- UserDetails Implementation ---

    /**
     * Returns the authorities granted to the user.
     * <p>
     * Converts the single {@link Role} enum into a collection of
     * {@link GrantedAuthority} objects for Spring Security.
     *
     * @return A list containing the user's single role.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    /**
     * Returns the password used to authenticate the user.
     *
     * @return The user's hashed password.
     */
    @Override
    public String getPassword() {
        return this.password;
    }

    /**
     * Returns the username used to authenticate the user.
     *
     * @return The user's unique username.
     */
    @Override
    public String getUsername() {
        return this.username;
    }

    /**
     * Indicates whether the user's account has expired.
     *
     * @return {@code true} (account never expires).
     */
    @Override
    public boolean isAccountNonExpired() {
        return true; // Defaulting to true (accounts don't expire)
    }

    /**
     * Indicates whether the user is locked or unlocked.
     *
     * @return {@code true} (account is never locked).
     */
    @Override
    public boolean isAccountNonLocked() {
        return true; // Defaulting to true (accounts are not locked)
    }

    /**
     * Indicates whether the user's credentials (password) has expired.
     *
     * @return {@code true} (credentials never expire).
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Defaulting to true (credentials don't expire)
    }

    /**
     * Indicates whether the user is enabled or disabled.
     *
     * @return {@code true} (user is always enabled).
     */
    @Override
    public boolean isEnabled() {
        return true; // Defaulting to true (users are always enabled)
    }

    // --- Constructors, Getters, and Setters ---

    /**
     * Default constructor required by JPA.
     */
    public User() {
    }

    /**
     * Convenience constructor for creating a new User.
     *
     * @param username The user's login username.
     * @param password The user's *hashed* password.
     * @param role     The user's assigned {@link Role}.
     */
    public User(String username, String password, Role role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}