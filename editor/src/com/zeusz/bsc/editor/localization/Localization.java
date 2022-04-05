package com.zeusz.bsc.editor.localization;

import com.zeusz.bsc.editor.io.ResourceLoader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
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
        File file = ResourceLoader.getFile("locale/lang/" + language.getTag() + ".properties");

        try(InputStreamReader reader = new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8)) {
            // populate localization map
            localization.load(reader);
        }
        catch(IOException e) {
            // couldn't read properties file
        }

        // avoid unnecessary writes
        if(language != config.getLanguage())
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
