package com.culturacarabobo.cultorregistration.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration // Marks this class as a source of bean definitions for the application context
public class SecurityConfig {

    /**
     * Configures the HTTP security filter chain.
     * 
     * - Disables CSRF protection (commonly disabled for stateless APIs).
     * - Secures GET requests to "/cultors" by requiring authentication.
     * - Permits all other requests.
     * - Enables HTTP Basic authentication.
     *
     * @param http the {@link HttpSecurity} to modify
     * @return the configured {@link SecurityFilterChain}
     * @throws Exception if an error occurs during configuration
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(org.springframework.http.HttpMethod.GET, "/cultors").authenticated()
                        .anyRequest().permitAll())
                .httpBasic(httpBasic -> {
                });

        return http.build();
    }

    /**
     * Provides a BCrypt-based password encoder.
     *
     * @return a {@link PasswordEncoder} using BCrypt hashing
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Exposes the {@link AuthenticationManager} as a Spring bean.
     *
     * @param authConfig the {@link AuthenticationConfiguration} to retrieve the
     *                   manager from
     * @return the configured {@link AuthenticationManager}
     * @throws Exception if retrieval fails
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
}
