package com.culturacarabobo.sicuc.backend.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.culturacarabobo.sicuc.backend.dtos.AuthRequest;
import com.culturacarabobo.sicuc.backend.dtos.AuthResponse;
import com.culturacarabobo.sicuc.backend.dtos.RefreshTokenRequest;
import com.culturacarabobo.sicuc.backend.services.AuthenticationService; // <-- Ahora solo depende de un servicio

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        // El controlador solo pasa la orden al servicio
        return ResponseEntity.ok(authenticationService.login(request));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refreshToken(@RequestBody RefreshTokenRequest request) {
        // Y aquí también
        return ResponseEntity.ok(authenticationService.refreshToken(request));
    }
}