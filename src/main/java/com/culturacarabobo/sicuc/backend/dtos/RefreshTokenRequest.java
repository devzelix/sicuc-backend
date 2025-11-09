package com.culturacarabobo.sicuc.backend.dtos;

/**
 * Data Transfer Object (DTO) for incoming token refresh requests.
 * <p>
 * This class models the JSON body expected by the {@code /auth/refresh}
 * endpoint,
 * containing the user's long-lived refresh token.
 */
public class RefreshTokenRequest {

    /**
     * The long-lived JWT refresh token.
     */
    private String refreshToken;

    /**
     * Default constructor required for JSON (de)serialization by Jackson.
     */
    public RefreshTokenRequest() {
    }

    // --- Standard Getters and Setters ---

    /**
     * @return The refresh token provided by the client.
     */
    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}