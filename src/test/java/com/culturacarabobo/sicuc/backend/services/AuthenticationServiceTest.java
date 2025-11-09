package com.culturacarabobo.sicuc.backend.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken; // Added for Javadoc

import com.culturacarabobo.sicuc.backend.dtos.AuthRequest;
import com.culturacarabobo.sicuc.backend.dtos.AuthResponse;
import com.culturacarabobo.sicuc.backend.dtos.RefreshTokenRequest;

/**
 * Unit tests for the {@link AuthenticationService}.
 * <p>
 * This class uses Mockito to test the login and token refresh logic in isolation
 * by simulating (mocking) the behavior of {@link AuthenticationManager},
 * {@link UserService}, and {@link JwtService}.
 */
@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceTest {

    // --- Mocks (Dependencies) ---
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private UserService userService;
    @Mock
    private JwtService jwtService;

    // --- Class Under Test (Injects the Mocks) ---
    @InjectMocks
    private AuthenticationService authenticationService;

    // ----------------------------------------------------------------
    // LOGIN TESTS
    // ----------------------------------------------------------------

    /**
     * Test (Happy Path): {@link AuthenticationService#login(AuthRequest)}.
     * <p>
     * Scenario: Valid credentials are provided.
     * Expected: Authentication succeeds, and new access/refresh tokens are returned.
     */
    @SuppressWarnings("null")
    @Test
    public void whenLogin_Success_shouldReturnAuthResponse() {
        // [ARRANGE] Setup mocks for successful authentication
        AuthRequest loginRequest = new AuthRequest();
        loginRequest.setUsername("testuser");
        loginRequest.setPassword("password");
        
        UserDetails userDetails = User.builder().username("testuser").password("password").roles("USER").build();

        when(userService.loadUserByUsername("testuser")).thenReturn(userDetails);
        when(jwtService.generateToken(userDetails)).thenReturn("fake-access-token");
        when(jwtService.generateRefreshToken(userDetails)).thenReturn("fake-refresh-token");
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
            .thenReturn(new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities()));

        // [ACT]
        AuthResponse response = authenticationService.login(loginRequest);

        // [ASSERT]
        assertEquals("fake-access-token", response.getAccessToken());
        assertEquals("fake-refresh-token", response.getRefreshToken());
        verify(authenticationManager, times(1)).authenticate(any());
        verify(userService, times(1)).loadUserByUsername("testuser");
    }

    /**
     * Test (Sad Path): {@link AuthenticationService#login(AuthRequest)}.
     * Scenario: Invalid password or unknown username.
     * Expected: Throws {@link BadCredentialsException} and skips token generation.
     */
    @SuppressWarnings("null")
    @Test
    public void whenLogin_BadCredentials_shouldThrowException() {
        // [ARRANGE] Setup mock to force authentication failure
        AuthRequest loginRequest = new AuthRequest();
        loginRequest.setUsername("testuser");
        loginRequest.setPassword("wrong-password");

        when(authenticationManager.authenticate(any()))
            .thenThrow(new BadCredentialsException("Invalid credentials"));

        // [ACT & ASSERT]
        assertThrows(BadCredentialsException.class, () -> {
            authenticationService.login(loginRequest);
        });

        // Verify token generation was never attempted
        verify(userService, never()).loadUserByUsername(anyString());
        verify(jwtService, never()).generateToken(any());
    }

    // ----------------------------------------------------------------
    // REFRESH TOKEN TESTS
    // ----------------------------------------------------------------

    /**
     * Test (Happy Path): {@link AuthenticationService#refreshToken(RefreshTokenRequest)}.
     * Scenario: A valid, unexpired refresh token is provided.
     * Expected: A new access token is generated, and the original refresh token is returned.
     */
    @SuppressWarnings("null")
    @Test
    public void whenRefreshToken_Success_shouldReturnNewAccessToken() {
        // [ARRANGE] Setup mocks for successful token renewal
        RefreshTokenRequest refreshRequest = new RefreshTokenRequest();
        refreshRequest.setRefreshToken("valid-refresh-token");
        
        UserDetails userDetails = User.builder().username("testuser").password("").roles("USER").build();

        when(jwtService.extractUsername("valid-refresh-token")).thenReturn("testuser");
        when(userService.loadUserByUsername("testuser")).thenReturn(userDetails);
        when(jwtService.isTokenValid("valid-refresh-token", userDetails)).thenReturn(true);
        when(jwtService.extractClaim(eq("valid-refresh-token"), any())).thenReturn("refresh");
        when(jwtService.generateToken(userDetails)).thenReturn("new-access-token");

        // [ACT]
        AuthResponse response = authenticationService.refreshToken(refreshRequest);

        // [ASSERT]
        assertEquals("new-access-token", response.getAccessToken());
        assertEquals("valid-refresh-token", response.getRefreshToken()); 
        verify(jwtService, times(1)).generateToken(userDetails);
    }
    
    /**
     * Test (Sad Path): {@link AuthenticationService#refreshToken(RefreshTokenRequest)}.
     * Scenario: An *access* token is mistakenly sent to the refresh endpoint.
     * Expected: Throws {@link RuntimeException} as the token type is wrong.
     */
    @SuppressWarnings("null")
    @Test
    public void whenRefreshToken_IsActuallyAccessToken_shouldThrowException() {
        // [ARRANGE] Setup mocks to confirm token type is "access"
        RefreshTokenRequest refreshRequest = new RefreshTokenRequest();
        refreshRequest.setRefreshToken("not-a-refresh-token");
        
        UserDetails userDetails = User.builder().username("testuser").password("").roles("USER").build();

        when(jwtService.extractUsername("not-a-refresh-token")).thenReturn("testuser");
        when(userService.loadUserByUsername("testuser")).thenReturn(userDetails);
        when(jwtService.isTokenValid("not-a-refresh-token", userDetails)).thenReturn(true);
        when(jwtService.extractClaim(eq("not-a-refresh-token"), any())).thenReturn("access"); // Token type is 'access'

        // [ACT & ASSERT]
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            authenticationService.refreshToken(refreshRequest);
        });

        assertEquals("Invalid or expired Refresh Token", exception.getMessage());
        
        // Verify token generation was never called
        verify(jwtService, never()).generateToken(userDetails);
    }
}