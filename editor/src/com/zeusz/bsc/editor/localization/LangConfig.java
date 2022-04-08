package com.zeusz.bsc.editor.localization;

import com.zeusz.bsc.editor.io.IOManager;

import org.ini4j.Ini;

import java.io.File;
import java.io.IOException;


final class LangConfig {

    private Ini config;

    LangConfig() {
        try {
            // load/create user's config
            File file = new File(IOManager.getInstance().getHomeDir(), "config.ini");

            if(!file.exists() && file.createNewFile()) {
                config = new Ini(file);  // load ini after creating file
                setLanguage(Language.getLanguageByTag("en"));
            }
            else {
                config = new Ini(file);  // file already exists
            }
        }
        catch(IOException e) {
            // couldn't read config
        }
    }

    void setLanguage(Language language) {
        try {
            if(config != null) {
                config.put("editor", "lang", language.getTag());
                config.store();
            }
        }
        catch(IOException e) {
            // couldn't write config
        }
    }

    Language getLanguage() {
        String tag = (config != null) ? config.get("editor", "lang") : null;
        return Language.getLanguageByTag(tag);
    }

}
