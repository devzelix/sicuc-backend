package com.culturacarabobo.sicuc.backend.exceptions;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Componente "puente" que captura los errores de Spring Security
 * (AuthenticationEntryPoint y AccessDeniedHandler)
 * y los delega al HandlerExceptionResolver global.
 *
 * Esto permite que nuestro @RestControllerAdvice (GlobalExceptionHandler)
 * maneje los errores 401 y 403 y devuelva un JSON consistente.
 */
@Component
public class DelegatedAuthEntryPoint implements AuthenticationEntryPoint, AccessDeniedHandler {

    @Autowired
    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver resolver;

    /**
     * Se llama cuando falla la autenticación (401 Unauthorized).
     */
    @SuppressWarnings("null")
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException) throws IOException, ServletException {
        // Delega el manejo de la excepción al GlobalExceptionHandler
        resolver.resolveException(request, response, null, authException);
    }

    /**
     * Se llama cuando falla la autorización (403 Forbidden).
     */
    @SuppressWarnings("null")
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
            AccessDeniedException accessDeniedException) throws IOException, ServletException {
        // Delega el manejo de la excepción al GlobalExceptionHandler
        resolver.resolveException(request, response, null, accessDeniedException);
    }
}