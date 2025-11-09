package com.culturacarabobo.sicuc.backend.exceptions;

/**
 * A custom, unchecked exception thrown when an operation violates a unique
 * constraint.
 * <p>
 * This is typically used in the service layer to signal a duplicate entity
 * (e.g., existing email or ID number) that should result in an HTTP 409
 * (Conflict) response.
 *
 * @see GlobalExceptionHandler#handleDuplicate(DuplicateEntityException,
 * jakarta.servlet.http.HttpServletRequest)
 */
public class DuplicateEntityException extends RuntimeException {

    /**
     * Default serial version UID for serialization.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new DuplicateEntityException with the specified detail message.
     *
     * @param message The detail message (e.g., "Email Already Exists").
     */
    public DuplicateEntityException(String message) {
        super(message);
    }

}