package com.zeusz.bsc.core;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;


public final class Localization {

    private Localization() { }

    private static final Set<Locale> SUPPORTED_LOCALES = new LinkedHashSet<>(Arrays.asList(
            Locale.forLanguageTag("en"),
            Locale.forLanguageTag("hu")/*,
            Locale.forLanguageTag("sk")*/
    ));

    private static final String CONFIG_PATH = "lang.properties";
    private static final String LOCALE = Locale.class.getSimpleName().toLowerCase();

    private static Locale locale;
    private static ResourceBundle localization;    // contains (unlocalized -> localized) pairs

    public static Set<Locale> getSupportedLocales() { return SUPPORTED_LOCALES; }

    public static Locale getLocale() { return locale; }

    public static void init() {
        try {
            Properties properties = new Properties();
            properties.load(new FileInputStream(CONFIG_PATH));
            locale = Locale.forLanguageTag(properties.getProperty(LOCALE));

            // check if loaded locale is supported
            Optional<Locale> supported = getSupportedLocales().stream().filter(it -> {
                return Objects.equals(it.getLanguage(), locale.getLanguage());
            }).findAny();

            if(!supported.isPresent()) {
                System.err.println(String.format("Locale %s is not supported", locale.getLanguage()));
                throw new IOException();
            }
        }
        catch(IOException e) {
            locale = Locale.ENGLISH;
        }

        load(locale);
    }

    public static void load(Locale locale) {
        localization = ResourceBundle.getBundle("locale/" + locale.getLanguage() + "/" + locale);
        if(!isCurrent(locale)) store(locale);   // avoid unnecessary writes
    }

    public static void store(Locale locale) {
        try {
            Localization.locale = locale;
            Properties properties = new Properties();
            properties.put(LOCALE, locale.getLanguage());
            properties.store(new FileOutputStream(CONFIG_PATH), null);
        }
        catch(IOException e) {
            // couldn't write config
        }
    }

    public static boolean isCurrent(Locale locale) {
        return Objects.equals(locale.getLanguage(), getLocale().getLanguage());
    }

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