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

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider; // <-- AÑADIDO DE VUELTA (Esencial)
    private final DelegatedAuthEntryPoint delegatedAuthEntryPoint; // <-- AÑADIDO (Para errores 401/403)

    /**
     * Constructor modificado para inyectar todas las dependencias necesarias.
     */
    public SecurityConfig(JwtAuthenticationFilter jwtAuthFilter, AuthenticationProvider authenticationProvider,
            DelegatedAuthEntryPoint delegatedAuthEntryPoint) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.authenticationProvider = authenticationProvider;
        this.delegatedAuthEntryPoint = delegatedAuthEntryPoint;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        // REGLA 1: Endpoints de autenticación son públicos.
                        .requestMatchers("/auth/**").permitAll()

                        // REGLA 2: Endpoints de consulta (GET) para formularios son públicos.
                        .requestMatchers(HttpMethod.GET, "/municipalities", "/parishes", "/art-categories", "/art-disciplines").permitAll()

                        // REGLA 3: Endpoint para crear un nuevo cultor (POST) es público.
                        .requestMatchers(HttpMethod.POST, "/cultors").permitAll()
                        
                        // REGLA 4: Todas las demás peticiones (GET /cultors, PUT, DELETE, etc.) requieren autenticación.
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                
                // --- AÑADIDO DE VUELTA: Conecta tu lógica de autenticación ---
                .authenticationProvider(authenticationProvider)

                // --- AÑADIDO: Conecta tu manejador de errores 401/403 ---
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(delegatedAuthEntryPoint)
                        .accessDeniedHandler(delegatedAuthEntryPoint))

                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}