package com.culturacarabobo.cultorregistration.backend.utils;

import java.time.LocalDate;

/**
 * Utility class for validating birth dates based on age constraints.
 * Defines acceptable birth date range between 18 and 120 years ago from today.
 */
public class DateValidator {

    /**
     * Returns the minimum valid birth date (120 years ago from now).
     * 
     * @return LocalDate representing 120 years before current date
     */
    public static LocalDate getMinBirthDate() {
        return LocalDate.now().minusYears(120);
    }

    /**
     * Returns the maximum valid birth date (18 years ago from now).
     * 
     * @return LocalDate representing 18 years before current date
     */
    public static LocalDate getMaxBirthDate() {
        return LocalDate.now().minusYears(18);
    }

    /**
     * Validates if the given birthDate is within the allowed range.
     * Birth date must be between 18 and 120 years ago.
     * 
     * @param birthDate the date to validate
     * @return true if birthDate is valid, false otherwise
     */
    public static boolean isValidBirthDate(LocalDate birthDate) {
        return !(birthDate.isBefore(getMinBirthDate())) && !(birthDate.isAfter(getMaxBirthDate()));
    }
}