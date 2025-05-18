package com.secretariaculturacarabobo.cultistregistration.backend.utils;

public class StringUtils {

    public static String toCapitalize(String text) {
        if (text == null || text.isBlank())
            return text;
        String[] slicedText = text.trim().toLowerCase().split(" ");
        String textCapitalize = "";
        for (String sliceText : slicedText) {
            textCapitalize += sliceText.substring(0, 1).toUpperCase() + sliceText.substring(1) + " ";
        }
        return textCapitalize.substring(0, textCapitalize.length() - 1);

    }

}
