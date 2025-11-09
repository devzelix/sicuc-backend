package com.culturacarabobo.sicuc.backend.utils;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link DateValidator} utility class.
 * <p>
 * This test suite verifies all age boundary conditions and null safety using a
 * fixed reference date, ensuring the tests remain stable regardless of the
 * system clock.
 */
class DateValidatorTest {

    /**
     * Fixed reference date used for all age calculations (2025-11-09).
     * <p>
     * Boundaries tested against this date: 18 years ago (2007-11-09) and
     * 120 years ago (1905-11-09).
     */
    private static final LocalDate FAKE_TODAY = LocalDate.of(2025, 11, 9);

    /**
     * Test Scenario: Verify that the {@link DateValidator#isValidBirthDate(LocalDate, LocalDate)}
     * method correctly identifies dates that are on the valid boundaries or outside them,
     * including null input.
     */
    @Test
    void testIsValidBirthDate_WithReferenceDate() {
        // Valid Boundaries (Expected: true)
        assertTrue(DateValidator.isValidBirthDate(LocalDate.of(2007, 11, 9), FAKE_TODAY));  // Justo 18 años (Límite superior, inclusive)
        assertTrue(DateValidator.isValidBirthDate(LocalDate.of(1995, 5, 10), FAKE_TODAY));  // 30 años (En el medio)
        assertTrue(DateValidator.isValidBirthDate(LocalDate.of(1905, 11, 9), FAKE_TODAY)); // Justo 120 años (Límite inferior, inclusive)
        
        // Invalid Cases (Expected: false)
        assertFalse(DateValidator.isValidBirthDate(LocalDate.of(2007, 11, 10), FAKE_TODAY)); // Demasiado joven (17 años y 364 días)
        assertFalse(DateValidator.isValidBirthDate(LocalDate.of(1905, 11, 8), FAKE_TODAY));  // Demasiado viejo (120 años y 1 día)
        assertFalse(DateValidator.isValidBirthDate(null, FAKE_TODAY));                       // Entrada nula (Null Safety)
    }
}