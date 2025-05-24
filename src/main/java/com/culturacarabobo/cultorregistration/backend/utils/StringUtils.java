package com.culturacarabobo.cultorregistration.backend.utils;

/**
 * Utility class for string manipulation.
 */
public class StringUtils {

    /**
     * Capitalizes the first letter of each word in the given text,
     * converting the rest of the letters to lowercase.
     * 
     * Example: "joHN doE" -> "John Doe"
     * 
     * @param text the input string to capitalize
     * @return the capitalized string or original if null/blank
     */
    public static String toCapitalize(String text) {
        if (text == null || text.isBlank())
            return text;
        String[] slicedText = text.trim().toLowerCase().split(" ");
        String textCapitalize = "";
        for (String sliceText : slicedText) {
            textCapitalize += sliceText.substring(0, 1).toUpperCase() + sliceText.substring(1) + " ";
        }

        // Remove trailing space
        return textCapitalize.substring(0, textCapitalize.length() - 1);

    }

}
