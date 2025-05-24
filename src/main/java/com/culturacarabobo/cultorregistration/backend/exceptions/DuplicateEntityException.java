package com.culturacarabobo.cultorregistration.backend.exceptions;

/**
 * Custom unchecked exception thrown when attempting to create
 * an entity that already exists (duplicate).
 */
public class DuplicateEntityException extends RuntimeException {

    /**
     * Constructs a new DuplicateEntityException with the specified detail message.
     *
     * @param message the detail message explaining the cause of the exception
     */
    public DuplicateEntityException(String message) {
        super(message);
    }

}
