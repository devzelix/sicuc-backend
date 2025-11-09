package com.culturacarabobo.sicuc.backend.exceptions;

import java.time.Instant;

/**
 * Represents a standardized, immutable error response body sent by the API.
 * <p>
 * This DTO is used by the {@link GlobalExceptionHandler} to ensure all
 * client-facing errors (4xx, 5xx) have a consistent JSON structure.
 */
public final class ErrorResponse {

    /**
     * The exact time the error occurred.
     */
    private final Instant timestamp;

    /**
     * The HTTP status code (e.g., 404, 400, 500).
     */
    private final int status;

    /**
     * A descriptive error message explaining what went wrong.
     */
    private final String error;

    /**
     * The API endpoint path where the error occurred (e.g., "/cultors/99").
     */
    private final String path;

    /**
     * Constructs a new, immutable ErrorResponse.
     *
     * @param timestamp The time the error occurred.
     * @param status    The HTTP status code.
     * @param error     A descriptive error message.
     * @param path      The API endpoint path where the error occurred.
     */
    public ErrorResponse(Instant timestamp, int status, String error, String path) {
        this.timestamp = timestamp;
        this.status = status;
        this.error = error;
        this.path = path;
    }

    // --- Standard Getters ---
    // (No setters are provided, as this is an immutable DTO)

    public Instant getTimestamp() {
        return timestamp;
    }

    public int getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }

    public String getPath() {
        return path;
    }
}