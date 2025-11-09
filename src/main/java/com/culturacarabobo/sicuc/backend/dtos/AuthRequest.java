package com.culturacarabobo.sicuc.backend.dtos;

/**
 * Data Transfer Object (DTO) for incoming user authentication (login) requests.
 * <p>
 * This class models the JSON body expected by the {@code /auth/login} endpoint,
 * containing the user's credentials.
 */
public class AuthRequest {

    /**
     * The user's unique username (e.g., "admin").
     */
    private String username;

    /**
     * The user's plain-text password.
     */
    private String password;

    /**
     * Default constructor required for JSON (de)serialization by Jackson.
     */
    public AuthRequest() {
    }

    // --- Standard Getters and Setters ---

    /**
     * @return The username provided for authentication.
     */
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return The plain-text password provided for authentication.
     */
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}