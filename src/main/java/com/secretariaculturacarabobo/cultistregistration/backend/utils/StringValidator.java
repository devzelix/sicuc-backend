package com.secretariaculturacarabobo.cultistregistration.backend.utils;

public class StringValidator {

    public static boolean isValidName(String name) {
        String regex = "^[A-Za-zÁÉÍÓÚáéíóúÑñ'\\- ]{1,50}$";
        if (!name.matches(regex))
            return false;

        long specialCount = name.chars()
                .filter(c -> c == ' ' || c == '-' || c == '\'')
                .count();

        return specialCount <= 2;
    }

}
