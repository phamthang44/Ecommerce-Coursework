package com.greenwich.ecommerce.common.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class DateFormatterUtil {

    public static String formatForReceipt(String isoDateStr) {
        try {
            // Parse ISO string to LocalDateTime
            LocalDateTime dateTime = LocalDateTime.parse(isoDateStr, DateTimeFormatter.ISO_DATE_TIME);

            // Format theo kiểu "21 Jul 2025, 10:30 AM"
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a", Locale.ENGLISH);

            return dateTime.format(formatter);
        } catch (Exception e) {
            return "Invalid date";
        }
    }

}
