package com.culturacarabobo.sicuc.backend.dtos;

// DTO para recibir las credenciales en la petición de login
public class AuthRequest {
    private String username;
    private String password;

    // Getters y Setters
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}