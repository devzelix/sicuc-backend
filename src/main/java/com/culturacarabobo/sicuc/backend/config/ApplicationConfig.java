package com.culturacarabobo.sicuc.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class ApplicationConfig {

    // --- ¡HEMOS ELIMINADO EL AUTHENTICATIONPROVIDER DE AQUÍ! ---
    // Spring Boot lo creará automáticamente por nosotros usando los
    // beans UserDetailsService (tu UserService) y PasswordEncoder.

    /**
     * El gestor de autenticación que usaremos en nuestro controlador de login.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * Define el algoritmo para codificar las contraseñas.
     * BCrypt es el estándar de la industria.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}