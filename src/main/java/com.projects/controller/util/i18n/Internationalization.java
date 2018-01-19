package com.projects.controller.util.i18n;

import java.util.Locale;
import java.util.ResourceBundle;

public class Internationalization {
    private static String baseName = "messages";
    private static ResourceBundle resourceBundle = ResourceBundle.getBundle(baseName);

    public static void changeLocale(Locale locale) {
        resourceBundle = ResourceBundle.getBundle(baseName, locale);
    }

    public static String getText(String key) {
        return resourceBundle.getString(key);
    }
}
