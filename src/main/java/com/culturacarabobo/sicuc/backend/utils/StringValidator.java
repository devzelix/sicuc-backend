package com.culturacarabobo.sicuc.backend.utils;

/**
 * Final utility class for common string validation methods.
 * <p>
 * This class provides static methods for checking string patterns (e.g., regex).
 * It cannot be instantiated.
 */
public final class StringValidator {

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private StringValidator() {
        // Prevent instantiation
    }

    /**
     * Validates if a given string is a proper name.
     * <p>
     * The regex enforces that the name must:
     * 1. Start with one or more letters (including accented characters).
     * 2. Can be followed by zero or more groups of:
     * - A space, apostrophe, or hyphen.
     * - Followed by one or more letters.
     * <p>
     * Examples of valid names: "José", "Anne-Marie", "O'Connor", "Luis De
     * Leon".<br>
     * Examples of invalid names: "Jose123", ".Jose", "Jose--Perez".
     *
     * @param name The name string to validate.
     * @return {@code true} if the name matches the proper format, {@code false}
     * otherwise.
     */
    public static boolean isValidName(String name) {
        if (name == null || name.isBlank()) {
            return false;
        }
        
        String regex = "^[A-Za-zÁÉÍÓÚáéíóúÑñ]+([ '-][A-Za-zÁÉÍÓÚáéíóúÑñ]+)*$";
        return name.matches(regex);
    }
}