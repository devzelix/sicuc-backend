package com.secretariaculturacarabobo.cultistregistration.backend.utils;

import java.time.LocalDate;

public class DateValidator {

    public static LocalDate getMinBirthDate() {
        return LocalDate.now().minusYears(120);
    }

    public static LocalDate getMaxBirthDate() {
        return LocalDate.now().minusYears(18);
    }

    public static boolean isValidBirthDate(LocalDate birthDate) {
        return !(birthDate.isBefore(getMinBirthDate())) && !(birthDate.isAfter(getMaxBirthDate()));
    }
}