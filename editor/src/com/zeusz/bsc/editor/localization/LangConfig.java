package com.zeusz.bsc.editor.localization;

import com.zeusz.bsc.editor.io.IOManager;

import org.ini4j.Ini;

import java.io.File;
import java.io.IOException;


final class LangConfig {

    private Ini config;

    LangConfig() {
        try {
            File file = new File(IOManager.getInstance().getHomeDir(), "config.ini");
            config = new Ini(file);

            if(!file.exists() && file.createNewFile())
                setLanguage(Language.EN);
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
        String tag = config.get("editor", "lang");
        return (config != null) ? Language.getLanguageByTag(tag) : Language.EN;
    }

}
