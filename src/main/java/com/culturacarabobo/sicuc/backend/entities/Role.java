package com.culturacarabobo.sicuc.backend.entities;

/**
 * Defines the user roles within the application.
 * <p>
 * These roles are used by Spring Security to handle authorization.
 * The "ROLE_" prefix is a standard convention for Spring Security.
 */
public enum Role {

    /**
     * Full administrative privileges. Can manage users and system settings.
     */
    ROLE_ADMIN,

    /**
     * Standard staff member. Can create, read, update, and delete cultor data.
     * Cannot manage users or system configuration.
     */
    ROLE_EDITOR,

    /**
     * The default role for a registered artist or cultural practitioner.
     * (Currently used as a base role, can be expanded later).
     */
    ROLE_CULTOR
}