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
 * A "bridge" component that catches Spring Security errors and delegates them
 * to the global {@link HandlerExceptionResolver}.
 * <p>
 * By default, Spring Security's filter chain intercepts
 * {@link AuthenticationException} (401) and {@link AccessDeniedException} (403)
 * *before* they reach the {@link GlobalExceptionHandler}.
 * <p>
 * This component acts as the entry point for both, ensuring that these security
 * exceptions are forwarded to the {@code GlobalExceptionHandler}
 * to return a consistent JSON {@link ErrorResponse} just like all other API
 * errors.
 */
@Component
public class DelegatedAuthEntryPoint implements AuthenticationEntryPoint, AccessDeniedHandler {

    /**
     * Injects the primary {@link HandlerExceptionResolver} bean from Spring's
     * context.
     * We use {@link Qualifier("handlerExceptionResolver")} to ensure we get the
     * default resolver that Spring Boot configures, which knows about our
     * {@code @RestControllerAdvice}.
     */
    @Autowired
    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver resolver;

    /**
     * Handles authentication failures (HTTP 401 Unauthorized).
     * <p>
     * This method is triggered by Spring Security when an unauthenticated user
     * attempts to access a protected resource (e.g., missing or invalid JWT).
     *
     * @param request       The request that resulted in an
     * {@link AuthenticationException}.
     * @param response      The response.
     * @param authException The exception that was thrown.
     */
    @SuppressWarnings("null")
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException) throws IOException, ServletException {
        // Delegate the exception to the GlobalExceptionHandler
        resolver.resolveException(request, response, null, authException);
    }

    /**
     * Handles authorization failures (HTTP 403 Forbidden).
     * <p>
     * This method is triggered by Spring Security when an authenticated user
     * attempts to access a resource they do not have the required role for.
     *
     * @param request               The request that resulted in an
     * {@link AccessDeniedException}.
     * @param response              The response.
     * @param accessDeniedException The exception that was thrown.
     */
    @SuppressWarnings("null")
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
            AccessDeniedException accessDeniedException) throws IOException, ServletException {
        // Delegate the exception to the GlobalExceptionHandler
        resolver.resolveException(request, response, null, accessDeniedException);
    }
}