package com.culturacarabobo.sicuc.backend.services;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import com.culturacarabobo.sicuc.backend.dtos.AuthRequest;
import com.culturacarabobo.sicuc.backend.dtos.AuthResponse;
import com.culturacarabobo.sicuc.backend.dtos.RefreshTokenRequest;

/**
 * Service responsible for handling user authentication logic.
 * <p>
 * This class coordinates with Spring Security's {@link AuthenticationManager}
 * to validate credentials and with {@link JwtService} to issue tokens.
 */
@Service
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtService jwtService;

    /**
     * Constructs the service with required dependencies.
     *
     * @param authenticationManager Spring's manager to process authentication
     * requests.
     * @param userService           The service responsible for loading
     * {@link UserDetails}.
     * @param jwtService            The service responsible for generating and
     * validating JWTs.
     */
    public AuthenticationService(AuthenticationManager authenticationManager, UserService userService, JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.jwtService = jwtService;
    }

    /**
     * Authenticates a user based on credentials provided in {@link AuthRequest}.
     * <p>
     * If authentication is successful, it generates a new pair of access and
     * refresh tokens.
     *
     * @param request The login request DTO containing username and password.
     * @return An {@link AuthResponse} containing the new access and refresh tokens.
     * @throws org.springframework.security.core.AuthenticationException If
     * credentials are invalid (e.g., BadCredentialsException).
     */
    @SuppressWarnings("null")
    public AuthResponse login(AuthRequest request) {
        // 1. Authenticate the user.
        // This will throw an AuthenticationException if credentials are bad.
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        // 2. If authentication is successful, fetch user details
        UserDetails userDetails = userService.loadUserByUsername(request.getUsername());
        
        // 3. Generate tokens
        String accessToken = jwtService.generateToken(userDetails);
        String refreshToken = jwtService.generateRefreshToken(userDetails);

        return new AuthResponse(accessToken, refreshToken);
    }

    /**
     * Refreshes an access token using a valid refresh token.
     * <p>
     * This method validates the refresh token, extracts the user, and issues a
     * *new* access token if the refresh token is valid and is of type "refresh".
     *
     * @param request The request DTO containing the refresh token.
     * @return An {@link AuthResponse} with a new access token and the
     * *original* refresh token.
     * @throws RuntimeException If the refresh token is invalid, expired, or not of
     * type "refresh".
     */
    public AuthResponse refreshToken(RefreshTokenRequest request) {
        String refreshToken = request.getRefreshToken();
        @SuppressWarnings("null")
        String username = jwtService.extractUsername(refreshToken);

        if (username != null) {
            UserDetails userDetails = this.userService.loadUserByUsername(username);
            
            @SuppressWarnings("null")
            boolean isTokenValid = jwtService.isTokenValid(refreshToken, userDetails);

            if (isTokenValid) {
                // Ensure the token provided is actually a refresh token
                @SuppressWarnings("null")
                String tokenType = jwtService.extractClaim(refreshToken, claims -> claims.get("type", String.class));
                
                if ("refresh".equals(tokenType)) {
                    String newAccessToken = jwtService.generateToken(userDetails);
                    return new AuthResponse(newAccessToken, refreshToken);
                }
            }
        }
        
        // If any check fails, throw an exception
        throw new RuntimeException("Invalid or expired Refresh Token");
    }
}