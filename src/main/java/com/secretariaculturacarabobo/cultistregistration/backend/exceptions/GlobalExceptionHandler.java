package com.secretariaculturacarabobo.cultistregistration.backend.exceptions;

import java.time.Instant;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.secretariaculturacarabobo.cultistregistration.backend.utils.StringUtils;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

        @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
        public ResponseEntity<ErrorResponse> handleHttpMessageNotReadable(HttpRequestMethodNotSupportedException ex,
                        HttpServletRequest request) {
                ErrorResponse response = new ErrorResponse(Instant.now(), HttpStatus.METHOD_NOT_ALLOWED.value(),
                                "Method Not Allowed",
                                request.getRequestURI());
                return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(response);
        }

        @ExceptionHandler(HttpMessageNotReadableException.class)
        public ResponseEntity<ErrorResponse> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                        HttpServletRequest request) {
                ErrorResponse response = new ErrorResponse(Instant.now(), HttpStatus.BAD_REQUEST.value(),
                                "The Request Body Is Empty Or Malformed. Please Ensure The JSON Is Properly Formatted And Contains Only Valid Properties",
                                request.getRequestURI());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        @ExceptionHandler(DuplicateEntityException.class)
        public ResponseEntity<ErrorResponse> handleDuplicate(DuplicateEntityException ex, HttpServletRequest request) {
                ErrorResponse response = new ErrorResponse(Instant.now(), HttpStatus.CONFLICT.value(), ex.getMessage(),
                                request.getRequestURI());
                return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }

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

        @ExceptionHandler(IllegalArgumentException.class)
        public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException ex,
                        HttpServletRequest request) {
                ErrorResponse response = new ErrorResponse(Instant.now(), HttpStatus.BAD_REQUEST.value(),
                                ex.getMessage(), request.getRequestURI());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        @ExceptionHandler(EntityNotFoundException.class)
        public ResponseEntity<ErrorResponse> handleIllegalArgument(EntityNotFoundException ex,
                        HttpServletRequest request) {
                ErrorResponse response = new ErrorResponse(Instant.now(), HttpStatus.NOT_FOUND.value(),
                                ex.getMessage(), request.getRequestURI());
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        @ExceptionHandler(Exception.class)
        public ResponseEntity<ErrorResponse> handleErroGeneral(Exception ex, HttpServletRequest request) {
                ErrorResponse response = new ErrorResponse(Instant.now(), HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                "The Request Body Is Empty Or Malformed. Please Ensure The JSON Is Properly Formatted And Contains Only Valid Properties",
                                request.getRequestURI());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }

}
