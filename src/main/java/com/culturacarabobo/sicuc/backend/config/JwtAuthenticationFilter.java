package com.culturacarabobo.sicuc.backend.config;

import java.io.IOException;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import com.culturacarabobo.sicuc.backend.services.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * A Spring Security filter that intercepts every incoming HTTP request once.
 * <p>
 * This filter is responsible for:
 * 1. Extracting the JWT (Bearer Token) from the "Authorization" header.
 * 2. Validating the token using {@link JwtService}.
 * 3. Loading the {@link UserDetails} from the database via
 * {@link UserDetailsService}.
 * 4. Setting the user's authentication in the {@link SecurityContextHolder} if
 * the token is valid.
 * <p>
 * This filter is inserted *before* the standard Spring Security filters by
 * {@link SecurityConfig}.
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    /**
     * Constructs the filter with required services.
     *
     * @param jwtService         The service for JWT operations (create, validate,
     * extract).
     * @param userDetailsService The service Spring Security uses to load a user by
     * username (our {@code UserService}).
     */
    public JwtAuthenticationFilter(JwtService jwtService, UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    /**
     * The core logic of the filter. This method is executed for every request.
     *
     * @param request     The incoming HTTP request.
     * @param response    The outgoing HTTP response.
     * @param filterChain The chain of subsequent filters.
     * @throws ServletException If an error occurs during filtering.
     * @throws IOException      If an I/O error occurs.
     */
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {

        // 1. Get the "Authorization" header
        final String authHeader = request.getHeader("Authorization");

        // 2. Check if the header is missing or not a Bearer token.
        // If so, pass the request to the next filter and exit.
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 3. Extract the JWT string (remove "Bearer ")
        final String jwt = authHeader.substring(7);
        
        // 4. Extract the username (subject) from the token.
        // This will throw ExpiredJwtException if the token is expired,
        // which is caught by our GlobalExceptionHandler.
        @SuppressWarnings("null")
        final String username = jwtService.extractUsername(jwt);

        // 5. Check if we have a username AND the user is not already authenticated
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            
            // 6. Load the user from the database
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

            // 7. Validate the token against the user details
            @SuppressWarnings("null")
            boolean isTokenValid = jwtService.isTokenValid(jwt, userDetails);

            if (isTokenValid) {
                // 8. (Custom Rule) Check if the token type is "access".
                // This prevents a "refresh" token from being used to access the API.
                @SuppressWarnings("null")
                String tokenType = jwtService.extractClaim(jwt, claims -> claims.get("type", String.class));
                
                if ("access".equals(tokenType)) {
                    // 9. Create the authentication token (User, null credentials, Authorities)
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null, // Credentials are null for token-based auth
                            userDetails.getAuthorities()
                    );
                    
                    // 10. Set request details (e.g., IP address) on the token
                    authToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request)
                    );
                    
                    // 11. Set the authenticated user in Spring's Security Context
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        }
        
        // 12. Pass the request to the next filter in the chain
        filterChain.doFilter(request, response);
    }
}