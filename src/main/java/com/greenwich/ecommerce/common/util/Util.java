package com.greenwich.ecommerce.common.util;

public class Util {

    public static String normalizePhoneNumber(String raw, String countryCode) {
        if (raw == null || countryCode == null) return null;

        String cleaned = raw.replaceAll("[^0-9]", "");

        if (cleaned.startsWith("0")) {
            cleaned = cleaned.substring(1);
        }

        switch (countryCode.toUpperCase()) {
            case "VN":
                return "+84" + cleaned;
            case "UK":
                return "+44" + cleaned;
            default:
                return null;
        }
    }

    public static boolean isInvalidEmailFormat(String email) {
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        return !email.matches(emailRegex);
    }

    public static boolean isInvalidPhoneNumberFormat(String phoneNumber) {
        String phoneRegex = "^\\?[0,9]{1,10}$";
        return !phoneNumber.matches(phoneRegex);
    }

    public static boolean isInvalidPasswordFormat(String password) {
        String passwordRegex = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$";
        return !password.matches(passwordRegex);
    }

    public static boolean isInvalidFullNameFormat(String fullName) {
        String fullNameRegex = "^[\\p{L} '-]{2,50}$";
        return !fullName.matches(fullNameRegex);
    }

    public static boolean isInvalidEmailLength(String email) {
        return email.length() >= 255; //nếu invalid thì trả về true
    }

    public static boolean isNullOrEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }



}
