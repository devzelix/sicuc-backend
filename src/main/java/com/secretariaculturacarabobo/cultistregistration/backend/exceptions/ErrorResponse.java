package com.secretariaculturacarabobo.cultistregistration.backend.exceptions;

import java.time.Instant;

/**
 * Represents a standardized structure for error responses
 * sent by the API, containing timestamp, HTTP status code,
 * error message, and the request path.
 */
public class ErrorResponse {

    private Instant timestamp;
    private int status;
    private String error;
    private String path;

    /**
     * Constructs an ErrorResponse with the specified details.
     *
     * @param timestamp the time the error occurred
     * @param status    the HTTP status code
     * @param error     a descriptive error message
     * @param path      the API endpoint path where the error occurred
     */
    public ErrorResponse(Instant timestamp, int status, String error, String path) {
        this.timestamp = timestamp;
        this.status = status;
        this.error = error;
        this.path = path;
    }

    // Getters and setters

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

}
