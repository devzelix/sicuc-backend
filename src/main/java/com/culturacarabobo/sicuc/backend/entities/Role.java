package com.culturacarabobo.sicuc.backend.entities;

/**
 * Defines the standard, professional roles a user can have within the system.
 */
public enum Role {
    
    /**
     * Grants full administrative privileges over the entire system.
     */
    ROLE_ADMIN,

    /**
     * Allows a staff member to create, read, update, and delete cultural data.
     * Does not grant access to system configuration or other user management.
     */
    ROLE_EDITOR,

    /**
     * The standard role for a registered artist/cultural practitioner.
     */
    ROLE_ARTIST
}