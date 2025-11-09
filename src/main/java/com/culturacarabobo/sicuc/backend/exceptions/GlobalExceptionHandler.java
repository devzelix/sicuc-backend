package com.culturacarabobo.sicuc.backend.exceptions;

import java.time.Instant;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.culturacarabobo.sicuc.backend.utils.StringUtils;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;

/**
 * Global exception handler for the application.
 * <p>
 * This {@link RestControllerAdvice} intercepts exceptions thrown by any
 * {@link RestController}
 * and converts them into a standardized {@link ErrorResponse} JSON object with
 * the appropriate {@link HttpStatus}.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Handles {@link EntityNotFoundException} (e.g., findById().orElseThrow()).
     * Returns an HTTP 404 (Not Found) response.
     *
     * @param ex      The exception thrown.
     * @param request The original HTTP request.
     * @return A ResponseEntity with a 404 status and {@link ErrorResponse} body.
     */
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEntityNotFound(EntityNotFoundException ex,
            HttpServletRequest request) {
        ErrorResponse response = new ErrorResponse(Instant.now(), HttpStatus.NOT_FOUND.value(),
                ex.getMessage(), request.getRequestURI());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    /**
     * Handles the custom {@link DuplicateEntityException}.
     * This is thrown on a unique constraint violation (e.g., duplicate idNumber).
     * Returns an HTTP 409 (Conflict) response.
     *
     * @param ex      The exception thrown.
     * @param request The original HTTP request.
     * @return A ResponseEntity with a 409 status and {@link ErrorResponse} body.
     */
    @ExceptionHandler(DuplicateEntityException.class)
    public ResponseEntity<ErrorResponse> handleDuplicate(DuplicateEntityException ex, HttpServletRequest request) {
        ErrorResponse response = new ErrorResponse(Instant.now(), HttpStatus.CONFLICT.value(), ex.getMessage(),
                request.getRequestURI());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    /**
     * Handles {@link IllegalArgumentException}, typically for business rule
     * violations.
     * (e.g., "Parish does not belong to Municipality", "IdNumber cannot be
     * modified").
     * Returns an HTTP 400 (Bad Request) response.
     *
     * @param ex      The exception thrown.
     * @param request The original HTTP request.
     * @return A ResponseEntity with a 400 status and {@link ErrorResponse} body.
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException ex,
            HttpServletRequest request) {
        ErrorResponse response = new ErrorResponse(Instant.now(), HttpStatus.BAD_REQUEST.value(),
                ex.getMessage(), request.getRequestURI());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    /**
     * Handles {@link MethodArgumentNotValidException}, thrown by @Valid DTO
     * validation failures.
     * <p>
     * Collects *all* validation errors (e.g., "FirstName Is Required, IdNumber Is
     * Invalid")
     * into a single, comma-separated string, rather than just the first error.
     *
     * @param ex      The exception containing all binding errors.
     * @param request The original HTTP request.
     * @return A ResponseEntity with a 400 status and the concatenated error
     * messages.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex,
            HttpServletRequest request) {

        // Collects all field errors into a single string
        String errors = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(error -> StringUtils.toCapitalize(error.getField()) + " "
                        + StringUtils.toCapitalize(error.getDefaultMessage()))
                .collect(Collectors.joining(", "));

        ErrorResponse response = new ErrorResponse(Instant.now(), HttpStatus.BAD_REQUEST.value(),
                errors,
                request.getRequestURI());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    /**
     * Handles {@link HttpMessageNotReadableException} (e.g., malformed JSON or
     * empty request body).
     * Returns an HTTP 400 (Bad Request) response.
     *
     * @param ex      The exception thrown.
     * @param request The original HTTP request.
     * @return A ResponseEntity with a 400 status and {@link ErrorResponse} body.
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
            HttpServletRequest request) {
        ErrorResponse response = new ErrorResponse(Instant.now(), HttpStatus.BAD_REQUEST.value(),
                "The Request Body Is Empty Or Malformed. Please Ensure The JSON Is Properly Formatted And Contains Only Valid Properties",
                request.getRequestURI());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    /**
     * Handles {@link HttpRequestMethodNotSupportedException} (e.g., using GET on a
     * POST endpoint).
     * Returns an HTTP 405 (Method Not Allowed) response.
     *
     * @param ex      The exception thrown.
     * @param request The original HTTP request.
     * @return A ResponseEntity with a 405 status and {@link ErrorResponse} body.
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleMethodNotSupported(HttpRequestMethodNotSupportedException ex,
            HttpServletRequest request) {
        ErrorResponse response = new ErrorResponse(Instant.now(), HttpStatus.METHOD_NOT_ALLOWED.value(),
                "Method Not Allowed",
                request.getRequestURI());
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(response);
    }

    /**
     * Handles {@link AuthenticationException} (e.g., invalid, expired, or missing
     * JWT).
     * Triggered when an unauthenticated user attempts to access a protected
     * resource.
     * <p>
     * This handler is invoked via the {@link DelegatedAuthEntryPoint}.
     *
     * @param ex      The exception thrown.
     * @param request The original HTTP request.
     * @return A ResponseEntity with a 401 (Unauthorized) status and
     * {@link ErrorResponse} body.
     */
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleAuthenticationException(AuthenticationException ex,
            HttpServletRequest request) {
        ErrorResponse response = new ErrorResponse(Instant.now(), HttpStatus.UNAUTHORIZED.value(),
                "Authentication Failed: " + ex.getMessage(), request.getRequestURI());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    /**
     * Handles {@link AccessDeniedException} (e.g., incorrect role).
     * Triggered when an authenticated user attempts to access a resource
     * they do not have the required permissions for.
     * <p>
     * This handler is invoked via the {@link DelegatedAuthEntryPoint}.
     *
     * @param ex      The exception thrown.
     * @param request The original HTTP request.
     * @return A ResponseEntity with a 403 (Forbidden) status and
     * {@link ErrorResponse} body.
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDenied(AccessDeniedException ex, HttpServletRequest request) {
        ErrorResponse response = new ErrorResponse(Instant.now(), HttpStatus.FORBIDDEN.value(),
                "Access Denied: " + ex.getMessage(), request.getRequestURI());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

    /**
     * A "catch-all" handler for any other unhandled {@link Exception}.
     * <p>
     * This method performs two critical functions:
     * 1. **Logs the full stack trace** to the server logs for debugging.
     * 2. Returns a **generic, safe error message** to the client to avoid
     * exposing internal implementation details.
     * <p>
     * Returns an HTTP 500 (Internal Server Error) response.
     *
     * @param ex      The unhandled exception.
     * @param request The original HTTP request.
     * @return A ResponseEntity with a 500 status and a generic error message.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralError(Exception ex, HttpServletRequest request) {
        // Log the exception with stack trace for internal debugging
        logger.error("An unexpected error occurred at " + request.getRequestURI(), ex);

        // Return a generic response to the client
        ErrorResponse response = new ErrorResponse(Instant.now(), HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "An unexpected internal server error occurred. Please contact the administrator.",
                request.getRequestURI());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}