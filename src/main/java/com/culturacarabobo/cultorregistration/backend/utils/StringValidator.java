package com.culturacarabobo.cultorregistration.backend.utils;

/**
 * Utility class for string validation methods.
 */
public class StringValidator {

    /**
     * Validates if the given name contains only letters (including accented),
     * spaces, apostrophes, or hyphens in a proper format.
     * 
     * The name must start with letters and can include optional groups
     * of a space, apostrophe, or hyphen followed by letters.
     * 
     * Examples of valid names:
     * - "José"
     * - "Anne-Marie"
     * - "O'Connor"
     * 
     * @param name the name string to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidName(String name) {
        String regex = "^[A-Za-zÁÉÍÓÚáéíóúÑñ]+([ '-][A-Za-zÁÉÍÓÚáéíóúÑñ]+)*$";

        return name.matches(regex);
    }

}
