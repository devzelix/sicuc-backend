package com.culturacarabobo.sicuc.backend.utils;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class StringValidatorTest {

    @Test
    void testIsValidName_ValidNames() {
        assertTrue(StringValidator.isValidName("Jose"));
        assertTrue(StringValidator.isValidName("José Ángel"));
        assertTrue(StringValidator.isValidName("O'Brien"));
        assertTrue(StringValidator.isValidName("Anne-Marie"));
    }

    @Test
    void testIsValidName_InvalidNames() {
        assertFalse(StringValidator.isValidName("Jose123")); // Contiene números
        assertFalse(StringValidator.isValidName("Jose.")); // Contiene punto
        assertFalse(StringValidator.isValidName(" Jose")); // Empieza con espacio
        assertFalse(StringValidator.isValidName("Jose  Perez")); // Doble espacio
    }
}