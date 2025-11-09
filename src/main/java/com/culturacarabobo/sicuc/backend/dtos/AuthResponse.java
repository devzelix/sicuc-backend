package com.culturacarabobo.sicuc.backend.dtos;

/**
 * Data Transfer Object (DTO) for sending authentication tokens back to the
 * client.
 * <p>
 * This immutable class models the JSON response body sent after a successful
 * login or token refresh.
 */
public final class AuthResponse {

    /**
     * The short-lived JSON Web Token (JWT) used for authenticating API requests.
     */
    private final String accessToken;

    /**
     * The long-lived JSON Web Token (JWT) used to request a new access token
     * when the old one expires.
     */
    private final String refreshToken;

    /**
     * Constructs a new authentication response.
     *
     * @param accessToken  The generated access token.
     * @param refreshToken The generated refresh token.
     */
    public AuthResponse(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    // --- Standard Getters ---
    // (No setters are provided, as this is an immutable DTO)

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }
}