package com.greenwich.ecommerce.common.util;

import com.greenwich.ecommerce.exception.InvalidDataException;

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
        String phoneRegex = "^0\\d{9,10}$";
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

    /**
     * Checks if a string is null or empty (empty or contains only whitespace).
     * for java 8, use isEmpty() method.
     * @param str the string to check
     * @return true if the string is null or empty, false otherwise
     */
    public static boolean isNullOrEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    public static boolean isNullOrEmpty(Object obj) {
        return obj == null || (obj instanceof String && ((String) obj).trim().isEmpty());
    }

    /**
     * Checks if a string is null or blank (empty or contains only whitespace).
     * for java 11 and later, use isBlank() method.
     * @param str the string to check
     * @return true if the string is null or blank, false otherwise
     */
    public static boolean isNullOrBlank(String str) {
        return str == null || str.isBlank();
    }

    public static int getPageNo(Integer pageNo) {
        return pageNo > 0 ? pageNo - 1 : 0; // Convert to zero-based index
    }

}
