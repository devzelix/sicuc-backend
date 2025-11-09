package com.culturacarabobo.sicuc.backend.utils;

import java.time.LocalDate;

/**
 * Final utility class for validating birth dates based on age constraints.
 * <p>
 * Defines an acceptable birth date range (e.g., between 18 and 120 years old).
 * This class cannot be instantiated.
 */
public final class DateValidator {

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private DateValidator() {
        // Prevent instantiation
    }

    /**
     * The core validation logic. Checks if a birth date is within the allowed
     * range based on a specific reference date.
     * <p>
     * The valid range is inclusive (i.e., it includes the 18th and 120th
     * birthdays).
     *
     * @param birthDate     The birth date to validate.
     * @param referenceDate The "current" date to calculate the age against (e.g.,
     * LocalDate.now()).
     * @return {@code true} if the birth date is valid (on or between the min/max
     * dates), {@code false} otherwise.
     */
    public static boolean isValidBirthDate(LocalDate birthDate, LocalDate referenceDate) {
        if (birthDate == null || referenceDate == null) {
            return false;
        }

        // Calculate boundaries
        LocalDate minAgeDate = referenceDate.minusYears(120);
        LocalDate maxAgeDate = referenceDate.minusYears(18);

        // Logic: !isBefore (>=) AND !isAfter (<=)
        return !birthDate.isBefore(minAgeDate) && !birthDate.isAfter(maxAgeDate);
    }

    /**
     * Convenience overload for {@link #isValidBirthDate(LocalDate, LocalDate)}.
     * <p>
     * Validates if the given birth date is within the allowed range (18-120
     * years old) using the current system clock ({@link LocalDate#now()}) as
     * the reference.
     *
     * @param birthDate The date to validate.
     * @return {@code true} if the birth date is valid, {@code false} otherwise.
     */
    public static boolean isValidBirthDate(LocalDate birthDate) {
        // Delegates logic to the testable core method
        return isValidBirthDate(birthDate, LocalDate.now());
    }
}