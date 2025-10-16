package com.culturacarabobo.sicuc.backend.dtos;

public class AuthResponse {
    private String accessToken;
    private String refreshToken; // <-- Campo añadido

    // Constructor actualizado para aceptar ambos tokens
    public AuthResponse(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    // Getters para ambos campos
    public String getAccessToken() { 
        return accessToken; 
    }

    public String getRefreshToken() { 
        return refreshToken; 
    }
}