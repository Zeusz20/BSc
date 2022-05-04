package com.zeusz.bsc.editor.localization;

import com.zeusz.bsc.editor.io.ResourceLoader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Properties;


public final class Language implements Comparable<Language> {

    /* Static class behavior */
    private static Language[] languages;

    static {
        try(InputStream file = ResourceLoader.getFile("config/language.properties");
            InputStreamReader reader = new InputStreamReader(file, StandardCharsets.UTF_8)) {

            // load available languages
            Properties config = new Properties();
            config.load(reader);

            languages = new Language[config.keySet().size()];

            int i = 0;
            for(String key: config.stringPropertyNames()) {
                String value = (String) config.get(key);
                languages[i++] = new Language(key, value);
            }

            Arrays.sort(languages);  // needed Comparable for sorting languages in alphabetical order
        }
        catch(IOException e) {
            // couldn't read config
            languages = new Language[0];
        }
    }

    public static Language[] values() { return languages; }

    public static Language getLanguageByTag(String tag) {
        for(Language language: languages) {
            if(language.getTag().equals(tag))
                return language;
        }

        return null;
    }

    /* Language struct */
    private final String tag, name;

    public Language(String tag, String name) {
        this.tag = tag.toLowerCase();
        this.name = name;
    }

    public String getTag() { return tag; }

    public String getName() { return name; }

    @Override
    public int compareTo(Language other) {
        return this.tag.compareTo(other.tag);
    }

}
