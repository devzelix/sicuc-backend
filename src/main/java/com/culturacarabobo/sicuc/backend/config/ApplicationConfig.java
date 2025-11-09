package com.culturacarabobo.sicuc.backend.config;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Defines the core application-wide Spring beans for configuration,
 * particularly for the security setup.
 * <p>
 * This class provides the {@link AuthenticationProvider},
 * {@link AuthenticationManager},
 * and {@link PasswordEncoder} beans required by the application.
 */
@Configuration
public class ApplicationConfig {

    /**
     * Defines the password encoding bean for the application.
     * <p>
     * Uses **BCrypt** as the hashing algorithm, which is the industry standard
     * for securely storing passwords.
     *
     * @return A {@link PasswordEncoder} instance.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Defines the primary {@link AuthenticationProvider} bean.
     * <p>
     * This bean acts as the "glue" that connects Spring Security to the custom
     * {@link UserDetailsService} (to find users) and the {@link PasswordEncoder}
     * (to verify passwords).
     *
     * @param userDetailsService The custom service implementation for loading user
     * data (e.g., {@code UserService}).
     * @param passwordEncoder    The bean used for password hashing.
     * @return The configured {@link DaoAuthenticationProvider} bean.
     */
    @SuppressWarnings("deprecation") // Setters are still a valid way to configure this bean
    @Bean
    public AuthenticationProvider authenticationProvider(UserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        // Set the service that Spring Security will use to find a user by username
        authProvider.setUserDetailsService(userDetailsService);
        // Set the password encoder to use for verifying passwords
        authProvider.setPasswordEncoder(passwordEncoder);
        return authProvider;
    }

    /**
     * Exposes the {@link AuthenticationManager} as a Spring bean.
     * <p>
     * This bean is required by the {@code AuthenticationController} to manually
     * trigger the authentication process for the login endpoint.
     *
     * @param configuration The authentication configuration from Spring Boot.
     * @return The configured {@link AuthenticationManager}.
     * @throws Exception if an error occurs while retrieving the manager.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
}