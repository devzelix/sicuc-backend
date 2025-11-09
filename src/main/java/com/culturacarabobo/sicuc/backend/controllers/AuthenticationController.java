package com.culturacarabobo.sicuc.backend.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.culturacarabobo.sicuc.backend.dtos.AuthRequest;
import com.culturacarabobo.sicuc.backend.dtos.AuthResponse;
import com.culturacarabobo.sicuc.backend.dtos.RefreshTokenRequest;
import com.culturacarabobo.sicuc.backend.services.AuthenticationService;

/**
 * REST controller for handling user authentication endpoints.
 * <p>
 * This controller exposes public endpoints for user login and token refreshing.
 * It delegates all logic to the {@link AuthenticationService}.
 */
@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    /**
     * Constructs the controller with the required AuthenticationService.
     *
     * @param authenticationService The service responsible for login and token
     * logic.
     */
    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    /**
     * [POST /auth/login] Authenticates a user and returns a token pair.
     *
     * @param request The {@link AuthRequest} DTO containing the username and
     * password.
     * @return A {@link ResponseEntity} with an {@link AuthResponse} (containing
     * access and refresh tokens) and HTTP 200 (OK).
     * @throws org.springframework.security.core.AuthenticationException If
     * credentials are invalid (handled by GlobalExceptionHandler as 401).
     */
    @SuppressWarnings("null")
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        // Delegate login logic to the service
        return ResponseEntity.ok(authenticationService.login(request));
    }

    /**
     * [POST /auth/refresh] Issues a new access token using a valid refresh token.
     *
     * @param request The {@link RefreshTokenRequest} DTO containing the refresh
     * token.
     * @return A {@link ResponseEntity} with an {@link AuthResponse} (containing a
     * *new* access token) and HTTP 200 (OK).
     * @throws RuntimeException If the refresh token is invalid or expired
     * (handled by GlobalExceptionHandler).
     */
    @SuppressWarnings("null")
    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refreshToken(@RequestBody RefreshTokenRequest request) {
        // Delegate refresh logic to the service
        return ResponseEntity.ok(authenticationService.refreshToken(request));
    }
}