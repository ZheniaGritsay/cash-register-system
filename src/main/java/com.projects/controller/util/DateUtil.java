package com.projects.controller.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtil {
    public static final String MM_DD_YYYY_HH_MM_REGEX = "^\\d{2}-\\d{2}-\\d{4} \\d{2}:\\d{2}$";
    public static final String MM_DD_YYYY_HH_MM_PATTERN = "MM-dd-yyyy HH:mm";

    public static LocalDateTime parseDate(String date, String pattern) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(pattern);
        return LocalDateTime.parse(date, dtf);
    }

    public static boolean dateConforms(String date, String regex){
        return date.matches(regex);
    }
}
