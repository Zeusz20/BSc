package com.zeusz.bsc.editor.localization;

import com.zeusz.bsc.editor.io.ResourceLoader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Properties;


public final class Localization {

    private static final LangConfig config;
    private static final Properties localization;    // contains (unlocalized -> localized) pairs

    static {
        config = new LangConfig();
        localization = new Properties();

        load(config.getLanguage());
    }

    public static Language getLanguage() { return config.getLanguage(); }

    public static void load(Language language) {
        try(InputStream file = ResourceLoader.getFile("locale/lang/" + language.getTag() + ".properties");
            InputStreamReader reader = new InputStreamReader(file, StandardCharsets.UTF_8)) {

            localization.load(reader);  // populate localization map
        }
        catch(IOException e) {
            // couldn't read properties file
        }

        // avoid unnecessary writes
        if(!Objects.equals(language.getTag(), config.getLanguage().getTag()))
            config.setLanguage(language);
    }

    /** @return Localized version of the unlocalized string. */
    public static String localize(String unlocalized) {
        return (String) localization.get(unlocalized);
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
