package com.culturacarabobo.sicuc.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
// --- CAMBIO 1: Ya no necesitamos importar AuthenticationProvider ---
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

    // --- CAMBIO 2: Eliminamos la variable 'authenticationProvider' ---
    private final JwtAuthenticationFilter jwtAuthFilter;

    // --- CAMBIO 3: Simplificamos el constructor ---
    public SecurityConfig(JwtAuthenticationFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                // REGLA 1: Endpoints de autenticación son públicos.
                .requestMatchers("/auth/**").permitAll()

                // REGLA 2: Endpoints de consulta para formularios (solo método GET) son públicos.
                // Asumiendo que tus rutas son /municipalities, /parishes, etc.
                .requestMatchers(HttpMethod.GET, "/municipalities", "/parishes", "/art-categories", "/art-disciplines").permitAll()

                // REGLA 3: Endpoint para crear un nuevo cultor (solo método POST) es público.
                .requestMatchers(HttpMethod.POST, "/cultors").permitAll()
                
                // REGLA 4: Todas las demás peticiones, incluyendo GET a /cultors, requieren autenticación.
                .anyRequest().authenticated()
            )
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}