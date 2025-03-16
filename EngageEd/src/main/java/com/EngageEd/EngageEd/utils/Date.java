package com.EngageEd.EngageEd.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Date {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static String formatDate(LocalDateTime dateTime) {
        return dateTime.format(FORMATTER);
    }

    public static LocalDateTime parseDate(String dateStr) {
        return LocalDateTime.parse(dateStr, FORMATTER);
    }
}
