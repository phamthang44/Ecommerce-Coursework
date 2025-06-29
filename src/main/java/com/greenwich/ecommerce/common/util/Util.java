package com.greenwich.ecommerce.common.util;

public class Util {

    public static String normalizePhoneNumber(String raw, String countryCode) {
        if (raw == null || countryCode == null) return null;

        // Bỏ dấu cách, ngoặc, gạch ngang, v.v.
        String cleaned = raw.replaceAll("[^0-9]", "");

        // Nếu bắt đầu bằng 0 thì bỏ đi để nối vào mã quốc gia
        if (cleaned.startsWith("0")) {
            cleaned = cleaned.substring(1);
        }

        switch (countryCode.toUpperCase()) {
            case "VN":
                return "+84" + cleaned;
            case "UK":
                return "+44" + cleaned;
            default:
                return null; // không hỗ trợ quốc gia khác
        }
    }
}
