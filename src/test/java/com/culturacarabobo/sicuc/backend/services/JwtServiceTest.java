package com.culturacarabobo.sicuc.backend.services;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import io.jsonwebtoken.ExpiredJwtException;

/**
 * Unit tests for the {@link JwtService}.
 * <p>
 * This test uses a real instance of {@code JwtService} with mocked time
 * properties to verify token generation, signature validity, and expiration logic.
 */
public class JwtServiceTest {

    private JwtService jwtService;
    private UserDetails userDetails;

    // Constantes de prueba inyectadas
    private static final String FAKE_SECRET_KEY = 
        "xWey5IjnYkdI3C9DWeHDesTXwr0srNCH9zh1WBkGiR1CaZTA2PXoYQp31Svee2m4FHhfy5sHHPVKn3n0Eqa2Gw";
    private static final long ONE_HOUR = 3600000;
    private static final long SEVEN_DAYS = 604800000;

    /**
     * Initializes the service and a base user before each test.
     */
    @BeforeEach
    void setUp() {
        // Creates a real JwtService instance with test secrets/expirations
        jwtService = new JwtService(FAKE_SECRET_KEY, ONE_HOUR, SEVEN_DAYS);
        
        // Creates a base UserDetails object
        userDetails = User.builder()
            .username("testuser@mail.com")
            .password("password")
            .roles("USER")
            .build();
    }

    /**
     * Test Scenario: Generate an access token and verify that the original
     * username (subject) can be extracted correctly.
     */
    @Test
    void whenGenerateToken_shouldExtractCorrectUsername() {
        @SuppressWarnings("null")
        String token = jwtService.generateToken(userDetails);
        
        assertNotNull(token);
        assertEquals("testuser@mail.com", jwtService.extractUsername(token));
    }

    /**
     * Test Scenario: Validate a recently generated access token.
     * Expected: The token is valid when compared to its corresponding UserDetails.
     */
    @SuppressWarnings("null")
    @Test
    void whenTokenIsValid_shouldReturnTrue() {
        @SuppressWarnings("null")
        String token = jwtService.generateToken(userDetails);
        assertTrue(jwtService.isTokenValid(token, userDetails));
    }

    /**
     * Test Scenario: Validate a token against a different user's details.
     * Expected: The token is invalid because the username subject does not match.
     */
    @SuppressWarnings("null")
    @Test
    void whenTokenIsForDifferentUser_shouldReturnFalse() {
        // Token generated for 'testuser@mail.com'
        @SuppressWarnings("null")
        String token = jwtService.generateToken(userDetails);

        // UserDetails for 'other@mail.com'
        UserDetails otherUser = User.builder()
            .username("other@mail.com")
            .password("pass")
            .roles("ADMIN")
            .build();

        // Expected: Token validation fails
        assertFalse(jwtService.isTokenValid(token, otherUser));
    }

    /**
     * Test Scenario: Use a token generated with a negative expiration time.
     * Expected: Throws {@link ExpiredJwtException} upon extraction.
     */
    @Test
    void whenTokenIsExpired_shouldThrowExpiredJwtException() {
        // [ARRANGE] Service instance configured to generate instantly expired tokens
        JwtService expiredJwtService = new JwtService(FAKE_SECRET_KEY, -1000, -1000);

        @SuppressWarnings("null")
        String expiredToken = expiredJwtService.generateToken(userDetails);

        // [ASSERT] Verify that calling extractUsername() fails cryptographically
        assertThrows(ExpiredJwtException.class, () -> {
            jwtService.extractUsername(expiredToken);
        });
    }

    /**
     * Test Scenario: Verify that 'access' and 'refresh' tokens contain the correct
     * custom 'type' claim.
     * Expected: Access token type is "access", refresh token type is "refresh".
     */
    @Test
    void whenTokensAreGenerated_shouldHaveCorrectTypeClaim() {
        // [ACT] Generate both token types
        @SuppressWarnings("null")
        String accessToken = jwtService.generateToken(userDetails);
        @SuppressWarnings("null")
        String refreshToken = jwtService.generateRefreshToken(userDetails);

        // [ASSERT] Verify the custom claim content
        String accessTokenType = jwtService.extractClaim(accessToken, claims -> claims.get("type", String.class));
        String refreshTokenType = jwtService.extractClaim(refreshToken, claims -> claims.get("type", String.class));

        assertEquals("access", accessTokenType);
        assertEquals("refresh", refreshTokenType);
    }
}