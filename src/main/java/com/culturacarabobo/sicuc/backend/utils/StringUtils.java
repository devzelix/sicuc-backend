package com.culturacarabobo.sicuc.backend.utils;

/**
 * Final utility class for common string manipulation tasks.
 * <p>
 * This class cannot be instantiated.
 */
public final class StringUtils {

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private StringUtils() {
        // Prevent instantiation
    }

    /**
     * Capitalizes the first letter of each word in the given text and converts
     * the rest to lowercase.
     * <p>
     * This method also trims leading/trailing whitespace and normalizes internal
     * spacing.
     * <p>
     * Example: " jOHN   doE " -> "John Doe"
     *
     * @param text The input string to capitalize.
     * @return The capitalized string. Returns the original string if it is null
     * or blank.
     */
    public static String toCapitalize(String text) {
        if (text == null || text.isBlank()) {
            return text;
        }

        // Normalize by trimming, lowercasing, and splitting by spaces
        String[] slicedText = text.trim().toLowerCase().split(" ");
        
        // Use StringBuilder for efficient string building in a loop
        StringBuilder textCapitalize = new StringBuilder();
        
        for (String sliceText : slicedText) {
            // This check handles multiple spaces (e.g., "John  Doe")
            if (!sliceText.isEmpty()) { 
                textCapitalize
                    .append(sliceText.substring(0, 1).toUpperCase())
                    .append(sliceText.substring(1))
                    .append(" ");
            }
        }

        // Convert to string and trim the final trailing space
        return textCapitalize.toString().trim();
    }
}