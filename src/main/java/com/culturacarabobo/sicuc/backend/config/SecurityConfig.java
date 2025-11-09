package com.culturacarabobo.sicuc.backend.config;

import org.springframework.security.authentication.AuthenticationProvider;
import com.culturacarabobo.sicuc.backend.exceptions.DelegatedAuthEntryPoint;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Main Spring Security configuration class.
 * <p>
 * Enables web security, configures the JWT filter chain, and defines
 * stateless session management. It specifies public endpoints
 * (e.g., /auth/**) and secures all other endpoints.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity // Enables method-level security (e.g., @PreAuthorize)
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;
    private final DelegatedAuthEntryPoint delegatedAuthEntryPoint;

    /**
     * Injects the required security components.
     *
     * @param jwtAuthFilter           The custom filter to process JWT tokens on each request.
     * @param authenticationProvider  The bean (defined in ApplicationConfig) that links
     * UserDetailsService and PasswordEncoder.
     * @param delegatedAuthEntryPoint The custom handler to forward 401/403 errors to the
     * {@link com.culturacarabobo.sicuc.backend.exceptions.GlobalExceptionHandler}.
     */
    public SecurityConfig(JwtAuthenticationFilter jwtAuthFilter, AuthenticationProvider authenticationProvider,
            DelegatedAuthEntryPoint delegatedAuthEntryPoint) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.authenticationProvider = authenticationProvider;
        this.delegatedAuthEntryPoint = delegatedAuthEntryPoint;
    }

    /**
     * Defines the primary security filter chain for the application.
     * <p>
     * This bean configures:
     * 1. **Stateless Sessions**: Disables HTTP sessions, forcing token-based auth.
     * 2. **CSRF**: Disabled, as it's not needed for stateless JWT APIs.
     * 3. **Authorization Rules**: Defines public and protected routes.
     * 4. **JWT Filter**: Inserts the {@link JwtAuthenticationFilter} to run before
     * the default auth filters.
     * 5. **Error Handling**: Sets custom entry points for 401/403 errors to ensure
     * consistent JSON responses.
     *
     * @param http The HttpSecurity object to configure.
     * @return The configured {@link SecurityFilterChain}.
     * @throws Exception if an error occurs during configuration.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // Disable CSRF protection, standard for stateless REST APIs
                .csrf(csrf -> csrf.disable())
                
                // Define URL-level authorization rules
                .authorizeHttpRequests(auth -> auth
                        // Rule 1: Authentication endpoints are public.
                        .requestMatchers("/auth/**").permitAll()

                        // Rule 2: Public read-only (GET) endpoints for form data.
                        .requestMatchers(HttpMethod.GET, "/municipalities", "/parishes", "/art-categories", "/art-disciplines").permitAll()

                        // Rule 3: Public endpoint for new cultor registration.
                        .requestMatchers(HttpMethod.POST, "/cultors").permitAll()
                        
                        // Rule 4: All other requests (PUT, DELETE, GET /cultors, etc.) must be authenticated.
                        .anyRequest().authenticated()
                )
                
                // Configure session management to be STATELESS
                // Spring Security will not create or use HTTP sessions
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                
                // Set the custom AuthenticationProvider (from ApplicationConfig)
                .authenticationProvider(authenticationProvider)

                // Set custom entry points for 401 (Unauthorized) and 403 (Forbidden) errors
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(delegatedAuthEntryPoint)
                        .accessDeniedHandler(delegatedAuthEntryPoint))
                
                // Add our custom JWT filter before the standard UsernamePasswordAuthenticationFilter
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}