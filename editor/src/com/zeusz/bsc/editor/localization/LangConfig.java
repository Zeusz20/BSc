package com.zeusz.bsc.editor.localization;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;


final class LangConfig {

    private File file;
    private Properties config;

    LangConfig() {
        file = new File("config.properties");
        config = new Properties();

        try {
            if(!file.exists() && file.createNewFile())
                setLanguage(Language.getLanguageByTag("en"));

            config.load(new FileInputStream(file));
        }
        catch(IOException e) {
            // couldn't read config
        }
    }

    void setLanguage(Language language) {
        try {
            if(language != null) {
                config.put("lang", language.getTag());
                config.store(new FileOutputStream(file), null);
            }
        }
        catch(IOException e) {
            // couldn't write config
        }
    }

    Language getLanguage() {
        String tag = (String) config.get("lang");
        return Language.getLanguageByTag(tag);
    }

}
