package com.culturacarabobo.sicuc.backend.utils;

import java.time.LocalDate;

/**
 * Utility class for validating birth dates based on age constraints.
 * Acceptable range: between 18 and 120 years ago (inclusive).
 */
public final class DateValidator {

    private DateValidator() {
        // Prevent instantiation
    }

    /**
     * Validates if the given birthDate is within the allowed range.
     *
     * @param birthDate The date to validate
     * @param referenceDate The reference "current" date (useful for testing)
     * @return true if birthDate is valid, false otherwise
     */
    public static boolean isValidBirthDate(LocalDate birthDate, LocalDate referenceDate) {
        if (birthDate == null || referenceDate == null) {
            return false;
        }

        LocalDate min = referenceDate.minusYears(120);
        LocalDate max = referenceDate.minusYears(18);

        return !birthDate.isBefore(min) && !birthDate.isAfter(max);
    }

    /**
     * Overload that uses the system date as reference.
     *
     * @param birthDate The date to validate
     * @return true if birthDate is valid, false otherwise
     */
    public static boolean isValidBirthDate(LocalDate birthDate) {
        return isValidBirthDate(birthDate, LocalDate.now());
    }
}
