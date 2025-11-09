package com.culturacarabobo.sicuc.backend.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq; // <-- Necesario para la prueba de claim

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import com.culturacarabobo.sicuc.backend.dtos.AuthRequest;
import com.culturacarabobo.sicuc.backend.dtos.AuthResponse;
import com.culturacarabobo.sicuc.backend.dtos.RefreshTokenRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

/**
 * Pruebas Unitarias para AuthenticationService.
 * Todas las dependencias (Managers, Services) son simuladas (Mocks).
 */
@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceTest {

    // --- Dependencias Simuladas (Mocks) ---
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private UserService userService;
    @Mock
    private JwtService jwtService;

    // --- Clase Bajo Prueba ---
    @InjectMocks
    private AuthenticationService authenticationService;

    @SuppressWarnings("null")
    @Test
    public void whenLogin_Success_shouldReturnAuthResponse() {
        // --- 1. ARRANGE (Preparar) ---
        AuthRequest loginRequest = new AuthRequest();
        loginRequest.setUsername("testuser"); // <-- ARREGLADO
        loginRequest.setPassword("password"); // <-- ARREGLADO
        
        UserDetails userDetails = User.builder().username("testuser").password("password").roles("USER").build();

        // Configuramos los Mocks
        // 1. El UserService encuentra al usuario
        when(userService.loadUserByUsername("testuser")).thenReturn(userDetails);
        
        // 2. El JwtService genera los tokens
        when(jwtService.generateToken(userDetails)).thenReturn("fake-access-token");
        when(jwtService.generateRefreshToken(userDetails)).thenReturn("fake-refresh-token");
        
        // El AuthenticationManager requiere un when() para la autenticación, aunque no lo usemos
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
            .thenReturn(new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities()));


        // --- 2. ACT (Actuar) ---
        AuthResponse response = authenticationService.login(loginRequest);

        // --- 3. ASSERT (Verificar) ---
        assertNotNull(response);
        assertEquals("fake-access-token", response.getAccessToken());
        assertEquals("fake-refresh-token", response.getRefreshToken());

        // Verificamos que los mocks fueron llamados
        verify(authenticationManager, times(1)).authenticate(any());
        verify(userService, times(1)).loadUserByUsername("testuser");
        verify(jwtService, times(1)).generateToken(userDetails);
        verify(jwtService, times(1)).generateRefreshToken(userDetails);
    }

    @SuppressWarnings("null")
    @Test
    public void whenLogin_BadCredentials_shouldThrowException() {
        // --- 1. ARRANGE (Preparar) ---
        AuthRequest loginRequest = new AuthRequest();
        loginRequest.setUsername("testuser"); // <-- ARREGLADO
        loginRequest.setPassword("wrong-password"); // <-- ARREGLADO

        // Configuramos el Mock
        // 1. El AuthenticationManager Falla y lanza una excepción
        when(authenticationManager.authenticate(any()))
            .thenThrow(new BadCredentialsException("Invalid credentials"));

        // --- 2. ACT & 3. ASSERT ---
        // Verificamos que la excepción es lanzada
        assertThrows(BadCredentialsException.class, () -> {
            authenticationService.login(loginRequest);
        });

        // Verificamos que los otros servicios NUNCA fueron llamados
        verify(userService, never()).loadUserByUsername(anyString());
        verify(jwtService, never()).generateToken(any());
    }

    @SuppressWarnings("null")
    @Test
    public void whenRefreshToken_Success_shouldReturnNewAccessToken() {
        // --- 1. ARRANGE (Preparar) ---
        RefreshTokenRequest refreshRequest = new RefreshTokenRequest();
        refreshRequest.setRefreshToken("valid-refresh-token"); // <-- ARREGLADO
        
        UserDetails userDetails = User.builder().username("testuser").password("").roles("USER").build();

        // Configuramos los Mocks
        when(jwtService.extractUsername("valid-refresh-token")).thenReturn("testuser");
        when(userService.loadUserByUsername("testuser")).thenReturn(userDetails);
        when(jwtService.isTokenValid("valid-refresh-token", userDetails)).thenReturn(true);
        when(jwtService.extractClaim(eq("valid-refresh-token"), any())).thenReturn("refresh");
        when(jwtService.generateToken(userDetails)).thenReturn("new-access-token");

        // --- 2. ACT (Actuar) ---
        AuthResponse response = authenticationService.refreshToken(refreshRequest);

        // --- 3. ASSERT (Verificar) ---
        assertNotNull(response);
        assertEquals("new-access-token", response.getAccessToken());
        assertEquals("valid-refresh-token", response.getRefreshToken()); 

        // Verificamos que 'generateToken' (para el nuevo access) fue llamado
        verify(jwtService, times(1)).generateToken(userDetails);
    }
    
    @SuppressWarnings("null")
    @Test
    public void whenRefreshToken_IsActuallyAccessToken_shouldThrowException() {
        // --- 1. ARRANGE (Preparar) ---
        RefreshTokenRequest refreshRequest = new RefreshTokenRequest();
        refreshRequest.setRefreshToken("not-a-refresh-token"); // <-- ARREGLADO
        
        UserDetails userDetails = User.builder().username("testuser").password("").roles("USER").build();

        // Configuramos los Mocks
        when(jwtService.extractUsername("not-a-refresh-token")).thenReturn("testuser");
        when(userService.loadUserByUsername("testuser")).thenReturn(userDetails);
        when(jwtService.isTokenValid("not-a-refresh-token", userDetails)).thenReturn(true);
        when(jwtService.extractClaim(eq("not-a-refresh-token"), any())).thenReturn("access"); // <-- ¡Es un token de "access"!

        // --- 2. ACT & 3. ASSERT ---
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            authenticationService.refreshToken(refreshRequest);
        });

        assertEquals("Invalid or expired Refresh Token", exception.getMessage());
        
        // Verificamos que NUNCA se generó un nuevo token
        verify(jwtService, never()).generateToken(userDetails);
    }

}