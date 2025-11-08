package com.culturacarabobo.sicuc.backend.config;

// --- AÑADE ESTAS IMPORTACIONES ---
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;
// ---
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class ApplicationConfig {

    /**
     * Define el bean del "Proveedor de Autenticación".
     * Este es el "pegamento" que une el UserDetailsService (que busca usuarios)
     * con el PasswordEncoder (que verifica la contraseña).
     *
     * @param userDetailsService Tu bean de UserDetailsService (probablemente
     * UserService).
     * @param passwordEncoder    El bean de BCryptPasswordEncoder.
     * @return El proveedor de autenticación DAO.
     */
    @SuppressWarnings("deprecation")
    @Bean
    public AuthenticationProvider authenticationProvider(UserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService); // Le dice dónde buscar usuarios
        authProvider.setPasswordEncoder(passwordEncoder); // Le dice cómo verificar contraseñas
        return authProvider;
    }

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