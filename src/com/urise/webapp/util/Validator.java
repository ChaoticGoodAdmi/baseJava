package com.urise.webapp.util;

import java.util.regex.Pattern;

public class Validator {

    public static boolean validatePhoneNumber(String value) {
        return value.matches("\\d{11}");
    }

    public static boolean validateUrl(String value) {
        return value.matches("\\b(https?)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]");
    }

    public static boolean validateUrl(String value, String webSite) {
        return validateUrl(value) && value.contains(webSite);
    }

    public static boolean validateEmail(String value) {
        final Pattern VALID_EMAIL_ADDRESS_REGEX =
                Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
        return VALID_EMAIL_ADDRESS_REGEX.matcher(value).find();
    }
}
