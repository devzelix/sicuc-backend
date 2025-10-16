package com.culturacarabobo.sicuc.backend.services;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import com.culturacarabobo.sicuc.backend.dtos.AuthRequest;
import com.culturacarabobo.sicuc.backend.dtos.AuthResponse;
import com.culturacarabobo.sicuc.backend.dtos.RefreshTokenRequest;
// --- CAMBIO 1: Ya no importamos UserRepository ---

@Service
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    // --- CAMBIO 2: Usamos UserService en lugar de UserRepository ---
    private final UserService userService;
    private final JwtService jwtService;

    // --- CAMBIO 3: Actualizamos el constructor para que pida UserService ---
    public AuthenticationService(AuthenticationManager authenticationManager, UserService userService, JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.jwtService = jwtService;
    }

    public AuthResponse login(AuthRequest request) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );
        // --- CAMBIO 4: Usamos el servicio para cargar el usuario ---
        UserDetails userDetails = userService.loadUserByUsername(request.getUsername());
        
        String accessToken = jwtService.generateToken(userDetails);
        String refreshToken = jwtService.generateRefreshToken(userDetails);

        return new AuthResponse(accessToken, refreshToken);
    }

    public AuthResponse refreshToken(RefreshTokenRequest request) {
        String refreshToken = request.getRefreshToken();
        String username = jwtService.extractUsername(refreshToken);

        if (username != null) {
            // --- CAMBIO 5: Esta línea ahora funcionará correctamente ---
            UserDetails userDetails = this.userService.loadUserByUsername(username);

            if (jwtService.isTokenValid(refreshToken, userDetails)) {
                String tokenType = jwtService.extractClaim(refreshToken, claims -> claims.get("type", String.class));
                if ("refresh".equals(tokenType)) {
                    String newAccessToken = jwtService.generateToken(userDetails);
                    return new AuthResponse(newAccessToken, refreshToken);
                }
            }
        }
        throw new RuntimeException("Invalid or expired Refresh Token");
    }
}