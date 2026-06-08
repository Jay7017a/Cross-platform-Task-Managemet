package com.taskmanager.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LoggerUtil {

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private LoggerUtil() {
        // Utility class — no instantiation
    }

    public static void info(String message) {
        System.out.println("[INFO]  " + timestamp() + " - " + message);
    }

    public static void warn(String message) {
        System.out.println("[WARN]  " + timestamp() + " - " + message);
    }

    public static void error(String message) {
        System.err.println("[ERROR] " + timestamp() + " - " + message);
    }

    public static void error(String message, Exception e) {
        System.err.println("[ERROR] " + timestamp() + " - " + message + " | Exception: " + e.getMessage());
    }

    private static String timestamp() {
        return LocalDateTime.now().format(FORMATTER);
    }
}
