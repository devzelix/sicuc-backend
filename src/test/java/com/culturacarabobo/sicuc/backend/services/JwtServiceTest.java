package com.culturacarabobo.sicuc.backend.services;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import io.jsonwebtoken.ExpiredJwtException;

/**
 * Pruebas Unitarias Puras para JwtService.
 * Esta prueba no usa Mocks, sino que crea una instancia real
 * del JwtService con valores de prueba.
 */
public class JwtServiceTest {

    private JwtService jwtService;
    private UserDetails userDetails;

    // Valores falsos para la prueba, similares a los de application.properties
    private static final String FAKE_SECRET_KEY = 
        "xWey5IjnYkdI3C9DWeHDesTXwr0srNCH9zh1WBkGiR1CaZTA2PXoYQp31Svee2m4FHhfy5sHHPVKn3n0Eqa2Gw";
    private static final long ONE_HOUR = 3600000;
    private static final long SEVEN_DAYS = 604800000;

    @BeforeEach
    void setUp() {
        // Creamos una instancia real de JwtService con nuestros valores de prueba
        jwtService = new JwtService(FAKE_SECRET_KEY, ONE_HOUR, SEVEN_DAYS);
        
        // Creamos un UserDetails de prueba
        userDetails = User.builder()
            .username("testuser@mail.com")
            .password("password")
            .roles("USER")
            .build();
    }

    @Test
    void whenGenerateToken_shouldExtractCorrectUsername() {
        @SuppressWarnings("null")
        String token = jwtService.generateToken(userDetails);
        
        assertNotNull(token);
        assertEquals("testuser@mail.com", jwtService.extractUsername(token));
    }

    @SuppressWarnings("null")
    @Test
    void whenTokenIsValid_shouldReturnTrue() {
        @SuppressWarnings("null")
        String token = jwtService.generateToken(userDetails);
        assertTrue(jwtService.isTokenValid(token, userDetails));
    }

    @SuppressWarnings("null")
    @Test
    void whenTokenIsForDifferentUser_shouldReturnFalse() {
        // 1. Token generado para "testuser@mail.com"
        @SuppressWarnings("null")
        String token = jwtService.generateToken(userDetails);

        // 2. Un usuario diferente
        UserDetails otherUser = User.builder()
            .username("other@mail.com")
            .password("pass")
            .roles("ADMIN")
            .build();

        // 3. El token no es válido para "otherUser"
        assertFalse(jwtService.isTokenValid(token, otherUser));
    }

    @Test
    void whenTokenIsExpired_shouldThrowExpiredJwtException() {
        // 1. Creamos un servicio con expiración negativa (ya caducó)
        JwtService expiredJwtService = new JwtService(FAKE_SECRET_KEY, -1000, -1000);

        // 2. Generamos un token que está caducado desde su creación
        @SuppressWarnings("null")
        String expiredToken = expiredJwtService.generateToken(userDetails);

        assertThrows(ExpiredJwtException.class, () -> {
            jwtService.extractUsername(expiredToken); // <-- ¡ARREGLADO!
        });
    }

    @Test
    void whenTokensAreGenerated_shouldHaveCorrectTypeClaim() {
        // 1. Generamos ambos tokens
        @SuppressWarnings("null")
        String accessToken = jwtService.generateToken(userDetails);
        @SuppressWarnings("null")
        String refreshToken = jwtService.generateRefreshToken(userDetails);

        // 2. Extraemos el claim "type" de cada uno
        String accessTokenType = jwtService.extractClaim(accessToken, claims -> claims.get("type", String.class));
        String refreshTokenType = jwtService.extractClaim(refreshToken, claims -> claims.get("type", String.class));

        // 3. Verificamos que el tipo sea el correcto
        assertEquals("access", accessTokenType);
        assertEquals("refresh", refreshTokenType);
    }
}