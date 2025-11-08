package com.culturacarabobo.sicuc.backend.exceptions;

import java.time.Instant;
import java.util.stream.Collectors; // <-- AÑADIDO: Para unir los errores

import org.slf4j.Logger; // <-- AÑADIDO: Para registrar errores 500
import org.slf4j.LoggerFactory; // <-- AÑADIDO: Para registrar errores 500
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
 * Intercepta y maneja excepciones de forma global para toda la aplicación.
 * Proporciona respuestas de error JSON consistentes con códigos de estado HTTP
 * apropiados.
 * Anotado con @RestControllerAdvice para aplicar a todos los @RestController.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    // <-- AÑADIDO: Logger para registrar excepciones no controladas (Errores 500)
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Maneja EntityNotFoundException (JPA) cuando se busca una entidad que no
     * existe.
     * (Ej. findById().orElseThrow(...))
     *
     * @param ex      La excepción lanzada.
     * @param request La petición HTTP.
     * @return ResponseEntity con HTTP 404 (Not Found).
     */
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEntityNotFound(EntityNotFoundException ex,
            HttpServletRequest request) {
        ErrorResponse response = new ErrorResponse(Instant.now(), HttpStatus.NOT_FOUND.value(),
                ex.getMessage(), request.getRequestURI());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    /**
     * Maneja nuestra excepción personalizada DuplicateEntityException.
     * (Ej. al crear un Cultor con una cédula que ya existe).
     *
     * @param ex      La excepción lanzada.
     * @param request La petición HTTP.
     * @return ResponseEntity con HTTP 409 (Conflict).
     */
    @ExceptionHandler(DuplicateEntityException.class)
    public ResponseEntity<ErrorResponse> handleDuplicate(DuplicateEntityException ex, HttpServletRequest request) {
        ErrorResponse response = new ErrorResponse(Instant.now(), HttpStatus.CONFLICT.value(), ex.getMessage(),
                request.getRequestURI());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    /**
     * Maneja IllegalArgumentException, usadas para validaciones de lógica de negocio.
     * (Ej. "La Parroquia no pertenece al Municipio", "No se puede modificar la
     * Cédula").
     *
     * @param ex      La excepción lanzada.
     * @param request La petición HTTP.
     * @return ResponseEntity con HTTP 400 (Bad Request).
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException ex,
            HttpServletRequest request) {
        ErrorResponse response = new ErrorResponse(Instant.now(), HttpStatus.BAD_REQUEST.value(),
                ex.getMessage(), request.getRequestURI());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    /**
     * --- ¡MEJORADO! ---
     * Maneja MethodArgumentNotValidException, lanzada por la anotación @Valid en
     * los DTOs.
     * Ahora concatena TODOS los errores de validación en un solo mensaje, en
     * lugar de solo el primero.
     *
     * @param ex      La excepción lanzada.
     * @param request La petición HTTP.
     * @return ResponseEntity con HTTP 400 (Bad Request).
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex,
            HttpServletRequest request) {

        // <-- INICIO DE LA LÓGICA MEJORADA
        String errors = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(error -> StringUtils.toCapitalize(error.getField()) + " "
                        + StringUtils.toCapitalize(error.getDefaultMessage()))
                .collect(Collectors.joining(", "));
        // <-- FIN DE LA LÓGICA MEJORADA

        ErrorResponse response = new ErrorResponse(Instant.now(), HttpStatus.BAD_REQUEST.value(),
                errors, // <-- Usa la cadena de errores concatenados
                request.getRequestURI());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    /**
     * Maneja HttpMessageNotReadableException (Ej. JSON malformado o cuerpo vacío).
     *
     * @param ex      La excepción lanzada.
     * @param request La petición HTTP.
     * @return ResponseEntity con HTTP 400 (Bad Request).
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
     * Maneja HttpRequestMethodNotSupportedException (Ej. usar GET en un endpoint
     * POST).
     *
     * @param ex      La excepción lanzada.
     * @param request La petición HTTP.
     * @return ResponseEntity con HTTP 405 (Method Not Allowed).
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
     * --- ¡CORREGIDO Y MEJORADO! ---
     * Manejador "Catch-All" para cualquier otra excepción no controlada.
     * Registra la traza de la excepción en el log del servidor para depuración.
     * Devuelve un mensaje genérico al cliente para no exponer detalles internos.
     *
     * @param ex      La excepción lanzada.
     * @param request La petición HTTP.
     * @return ResponseEntity con HTTP 500 (Internal Server Error).
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralError(Exception ex, HttpServletRequest request) {

        // <-- AÑADIDO: Registrar el error en el log del servidor
        logger.error("An unexpected error occurred at " + request.getRequestURI(), ex);

        // <-- CORREGIDO: Mensaje de error genérico
        ErrorResponse response = new ErrorResponse(Instant.now(), HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "An unexpected internal server error occurred. Please contact the administrator.",
                request.getRequestURI());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    /**
     * Maneja AuthenticationException (JWT inválido, caducado o ausente).
     * Se dispara cuando un usuario no autenticado intenta acceder a un recurso
     * protegido.
     *
     * @param ex      La excepción lanzada.
     * @param request La petición HTTP.
     * @return ResponseEntity con HTTP 401 (Unauthorized).
     */
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleAuthenticationException(AuthenticationException ex,
            HttpServletRequest request) {
        ErrorResponse response = new ErrorResponse(Instant.now(), HttpStatus.UNAUTHORIZED.value(),
                "Authentication Failed: " + ex.getMessage(), request.getRequestURI());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    /**
     * Maneja AccessDeniedException (Rol incorrecto).
     * Se dispara cuando un usuario autenticado (con rol USER) intenta acceder a un
     * recurso
     * que requiere rol ADMIN.
     *
     * @param ex      La excepción lanzada.
     * @param request La petición HTTP.
     * @return ResponseEntity con HTTP 403 (Forbidden).
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDenied(AccessDeniedException ex, HttpServletRequest request) {
        ErrorResponse response = new ErrorResponse(Instant.now(), HttpStatus.FORBIDDEN.value(),
                "Access Denied: " + ex.getMessage(), request.getRequestURI());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }
}