package com.zeusz.bsc.core;

import java.io.*;
import java.util.*;


/** This implementation DOES NOT SUPPORT locale variants. */
public final class Localization {

    private Localization() { }

    private static final String CONFIG_PATH = "config.properties";
    private static final String LOCALE = "locale";

    private static Locale locale;
    private static ResourceBundle localization;    // contains (unlocalized -> localized) pairs

    /** @return All locales in the app's res/locale folder. */
    public static Set<Locale> getSupportedLocales() {
        Set<Locale> supportedLocales = new LinkedHashSet<>();

        try(InputStream directory = Localization.class.getClassLoader().getResourceAsStream(LOCALE)) {
            if(directory != null) {
                byte[] contents = new byte[directory.available()];
                directory.read(contents);   // this ignores empty folders

                // get directory names
                String[] locales = new String(contents).split("\n");
                Arrays.stream(locales).forEach(it -> supportedLocales.add(parseLocale(it)));
            }
        }
        catch(IOException e) {
            // couldn't read dir
        }

        return supportedLocales;
    }

    public static void init() {
        try {
            Properties properties = new Properties();
            properties.load(new FileInputStream(CONFIG_PATH));
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
        localization = ResourceBundle.getBundle("locale/" + locale.toString() + "/lang");

        // avoid unnecessary writes
        if(!getLocale().equals(locale))
            store(locale);
    }

    public static void store(Locale locale) {
        try {
            Localization.locale = locale;
            Properties properties = new Properties();
            properties.put(LOCALE, locale.toString());
            properties.store(new FileOutputStream(CONFIG_PATH), null);
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
        return localization.getString(unlocalized);
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