package com.culturacarabobo.sicuc.backend.utils;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class StringUtilsTest {

    @Test
    void testToCapitalize_MultiWord() {
        String input = "jose ángel de jesus";
        String expected = "Jose Ángel De Jesus";
        assertEquals(expected, StringUtils.toCapitalize(input));
    }

    @Test
    void testToCapitalize_AllLower() {
        String input = "pedro perez";
        String expected = "Pedro Perez";
        assertEquals(expected, StringUtils.toCapitalize(input));
    }

    @Test
    void testToCapitalize_AllUpper() {
        String input = "JUAN GARCIA";
        String expected = "Juan Garcia";
        assertEquals(expected, StringUtils.toCapitalize(input));
    }

    @Test
    void testToCapitalize_SingleWord() {
        String input = "valencia";
        String expected = "Valencia";
        assertEquals(expected, StringUtils.toCapitalize(input));
    }

    @Test
    void testToCapitalize_NullOrBlank() {
        assertNull(StringUtils.toCapitalize(null));
        assertEquals("", StringUtils.toCapitalize(""));
        assertEquals("   ", StringUtils.toCapitalize("   ")); // <-- LÍNEA CORREGIDA
    }
}