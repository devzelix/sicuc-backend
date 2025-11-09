package com.culturacarabobo.sicuc.backend.utils;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the {@link StringValidator} utility class.
 * <p>
 * Specifically verifies that the {@code isValidName} method correctly accepts
 * standard, accented, hyphenated, and apostrophe-containing names while rejecting
 * names that contain numbers or invalid spacing.
 */
public class StringValidatorTest {

    /**
     * Tests {@link StringValidator#isValidName(String)} with expected valid inputs.
     * <p>
     * Scenarios covered: Simple names, accented characters, apostrophes, and hyphens.
     */
    @Test
    void testIsValidName_ValidNames() {
        assertTrue(StringValidator.isValidName("Jose"));
        assertTrue(StringValidator.isValidName("José Ángel"));
        assertTrue(StringValidator.isValidName("O'Brien"));
        assertTrue(StringValidator.isValidName("Anne-Marie"));
    }

    /**
     * Tests {@link StringValidator#isValidName(String)} with expected invalid inputs.
     * <p>
     * Scenarios covered: Names containing numbers, invalid characters (period),
     * leading spaces, and multiple internal spaces.
     */
    @Test
    void testIsValidName_InvalidNames() {
        assertFalse(StringValidator.isValidName("Jose123")); // Contains numbers
        assertFalse(StringValidator.isValidName("Jose.")); // Contains period
        assertFalse(StringValidator.isValidName(" Jose")); // Starts with a leading space
        assertFalse(StringValidator.isValidName("Jose  Perez")); // Double internal space
    }
}