package com.zeusz.bsc.core;

import java.io.*;
import java.util.*;


/** This implementation DOES NOT SUPPORT locale variants. */
public final class Localization {

    private Localization() { }

    private static final String CONFIG_PATH = "config.properties";
    private static final String RESOURCE_PATH = "lang.properties";
    private static final String LOCALE = "locale";

    private static Set<Locale> supportedLocales;
    private static Class<?> application;        // which application uses localization
    private static Locale locale;               // holds current locale
    private static Properties localization;     // contains (unlocalized -> localized) pairs
    private static File config;

    /** @return All locales in the app's res/locale folder. */
    public static Set<Locale> getSupportedLocales() {
        if(supportedLocales == null) {
            supportedLocales = new LinkedHashSet<>();

            for(Locale locale: Locale.getAvailableLocales()) {
                String path = String.format("%s/%s/%s", LOCALE, locale.toString(), RESOURCE_PATH);
                InputStream resource = application.getClassLoader().getResourceAsStream(path);

                if(resource != null)
                    supportedLocales.add(locale);
            }
        }

        return supportedLocales;
    }

    public static void init(Class<?> application, String directory) {
        try {
            Localization.application = application;
            config = (directory == null) ? new File(CONFIG_PATH) : new File(directory, CONFIG_PATH);

            // load locale from config
            Properties properties = new Properties();
            properties.load(new FileInputStream(config));
            locale = parseLocale(properties.getProperty(LOCALE));

            // check if loaded locale is supported
            Optional<Locale> supported = getSupportedLocales().stream().filter(Localization::isCurrent).findAny();

            if(!supported.isPresent()) {
                System.err.printf("Locale %s is not supported!%n", locale.toString());
                throw new IOException();
            }
        }
        catch(IOException e) {
            locale = Locale.US;
        }

        load(locale);
    }

    public static void load(Locale locale) {
        try {
            String path = String.format("%s/%s/%s", LOCALE, locale.toString(), RESOURCE_PATH);
            localization = new Properties();
            localization.load(application.getClassLoader().getResourceAsStream(path));
            store(locale);
        }
        catch(IOException e) {
            // couldn't load resource
        }
    }

    public static void store(Locale locale) {
        if(isCurrent(locale) || !getSupportedLocales().contains(locale))
            return;

        try {
            Localization.locale = locale;
            Properties properties = new Properties();
            properties.put(LOCALE, locale.toString());
            properties.store(new FileOutputStream(config), null);
        }
        catch(IOException e) {
            // couldn't write config
        }
    }

    public static Locale parseLocale(String s) {
        String[] split = s.split("_");
        return new Locale(split[0], split[1]);
    }

    public static boolean isCurrent(Locale locale) {
        return getLocale().equals(locale);
    }

    public static Locale getLocale() { return locale; }

    /** @return Localized version of the unlocalized string. */
    public static String localize(String unlocalized) {
        return localization.getProperty(unlocalized);
    }

    /** @return Capitalized and localized version of the unlocalized string. */
    public static String capLocalize(String unlocalized) {
        String localized = localize(unlocalized);
        return localized.substring(0, 1).toUpperCase() + localized.substring(1).toLowerCase();
    }

    /** @return Capitalized and localized prompt of the unlocalized string. */
    public static String prompt(String unlocalized) {
        return capLocalize(unlocalized) + ": ";
    }

}