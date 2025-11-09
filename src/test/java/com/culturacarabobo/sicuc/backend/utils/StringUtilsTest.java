package com.culturacarabobo.sicuc.backend.utils;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the {@link StringUtils} utility class.
 * <p>
 * Verifies that the {@code toCapitalize} method correctly handles various string
 * inputs, including case normalization and blank/null safety.
 */
public class StringUtilsTest {

    /**
     * Test Scenario: Multi-word input including internal spaces and mixed case.
     * Expected: Correct capitalization of each word ("Jose Ángel De Jesus").
     */
    @Test
    void testToCapitalize_MultiWord() {
        String input = "jose ángel de jesus";
        String expected = "Jose Ángel De Jesus";
        assertEquals(expected, StringUtils.toCapitalize(input));
    }

    /**
     * Test Scenario: Input string is all lowercase.
     * Expected: Only the first letter of each word is capitalized.
     */
    @Test
    void testToCapitalize_AllLower() {
        String input = "pedro perez";
        String expected = "Pedro Perez";
        assertEquals(expected, StringUtils.toCapitalize(input));
    }

    /**
     * Test Scenario: Input string is all uppercase.
     * Expected: Correct case normalization (only first letter capitalized).
     */
    @Test
    void testToCapitalize_AllUpper() {
        String input = "JUAN GARCIA";
        String expected = "Juan Garcia";
        assertEquals(expected, StringUtils.toCapitalize(input));
    }

    /**
     * Test Scenario: Input is a single word.
     * Expected: Only the first letter is capitalized.
     */
    @Test
    void testToCapitalize_SingleWord() {
        String input = "valencia";
        String expected = "Valencia";
        assertEquals(expected, StringUtils.toCapitalize(input));
    }

    /**
     * Test Scenario: Input is null, empty (""), or contains only blank spaces ("   ").
     * Expected: Returns the original null/empty/blank string, preventing errors.
     */
    @Test
    void testToCapitalize_NullOrBlank() {
        assertNull(StringUtils.toCapitalize(null));
        assertEquals("", StringUtils.toCapitalize(""));
        assertEquals("   ", StringUtils.toCapitalize("   "));
    }
}