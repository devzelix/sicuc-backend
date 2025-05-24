package com.culturacarabobo.cultorregistration.backend.exceptions;

import java.time.Instant;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.culturacarabobo.cultorregistration.backend.utils.StringUtils;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;

/**
 * Global exception handler to intercept and manage exceptions thrown by the
 * application.
 * Provides consistent error responses with appropriate HTTP status codes and
 * error messages.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

        /**
         * Handles HTTP 405 Method Not Allowed errors when an unsupported HTTP method is
         * used.
         */
        @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
        public ResponseEntity<ErrorResponse> handleHttpMessageNotReadable(HttpRequestMethodNotSupportedException ex,
                        HttpServletRequest request) {
                ErrorResponse response = new ErrorResponse(Instant.now(), HttpStatus.METHOD_NOT_ALLOWED.value(),
                                "Method Not Allowed",
                                request.getRequestURI());
                return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(response);
        }

        /**
         * Handles bad request errors due to empty or malformed JSON request bodies.
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
         * Handles conflicts caused by attempts to create or persist duplicate entities.
         */
        @ExceptionHandler(DuplicateEntityException.class)
        public ResponseEntity<ErrorResponse> handleDuplicate(DuplicateEntityException ex, HttpServletRequest request) {
                ErrorResponse response = new ErrorResponse(Instant.now(), HttpStatus.CONFLICT.value(), ex.getMessage(),
                                request.getRequestURI());
                return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }

        /**
         * Handles validation errors from invalid method arguments, returning the first
         * validation error.
         */
        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex,
                        HttpServletRequest request) {
                String errorField = ex.getBindingResult().getFieldErrors().getFirst().getField();
                String error = ex.getBindingResult().getFieldErrors().getFirst().getDefaultMessage();
                ErrorResponse response = new ErrorResponse(Instant.now(), HttpStatus.BAD_REQUEST.value(),
                                StringUtils.toCapitalize(errorField) + " " + StringUtils.toCapitalize(error),
                                request.getRequestURI());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        /**
         * Handles IllegalArgumentExceptions, typically thrown when method arguments are
         * invalid.
         */
        @ExceptionHandler(IllegalArgumentException.class)
        public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException ex,
                        HttpServletRequest request) {
                ErrorResponse response = new ErrorResponse(Instant.now(), HttpStatus.BAD_REQUEST.value(),
                                ex.getMessage(), request.getRequestURI());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        /**
         * Handles EntityNotFoundException when requested entities are not found.
         */
        @ExceptionHandler(EntityNotFoundException.class)
        public ResponseEntity<ErrorResponse> handleIllegalArgument(EntityNotFoundException ex,
                        HttpServletRequest request) {
                ErrorResponse response = new ErrorResponse(Instant.now(), HttpStatus.NOT_FOUND.value(),
                                ex.getMessage(), request.getRequestURI());
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        /**
         * Handles all other uncaught exceptions, returning a generic internal server
         * error response.
         */
        @ExceptionHandler(Exception.class)
        public ResponseEntity<ErrorResponse> handleErroGeneral(Exception ex, HttpServletRequest request) {
                ErrorResponse response = new ErrorResponse(Instant.now(), HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                "The Request Body Is Empty Or Malformed. Please Ensure The JSON Is Properly Formatted And Contains Only Valid Properties",
                                request.getRequestURI());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }

}
