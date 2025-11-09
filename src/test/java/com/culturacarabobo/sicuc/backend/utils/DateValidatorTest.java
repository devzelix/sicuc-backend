package com.culturacarabobo.sicuc.backend.utils;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class DateValidatorTest {

    private static final LocalDate FAKE_TODAY = LocalDate.of(2025, 11, 9);

    @Test
    void testIsValidBirthDate_WithReferenceDate() {
        assertTrue(DateValidator.isValidBirthDate(LocalDate.of(2007, 11, 9), FAKE_TODAY)); // Justo 18
        assertTrue(DateValidator.isValidBirthDate(LocalDate.of(1995, 5, 10), FAKE_TODAY)); // 30 años
        assertTrue(DateValidator.isValidBirthDate(LocalDate.of(1905, 11, 9), FAKE_TODAY)); // Justo 120
        assertFalse(DateValidator.isValidBirthDate(LocalDate.of(2007, 11, 10), FAKE_TODAY)); // Menor
        assertFalse(DateValidator.isValidBirthDate(LocalDate.of(1905, 11, 8), FAKE_TODAY)); // Mayor
        assertFalse(DateValidator.isValidBirthDate(null, FAKE_TODAY)); // Nulo
    }
}
