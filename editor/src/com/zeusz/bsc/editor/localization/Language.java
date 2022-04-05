package com.zeusz.bsc.editor.localization;

public enum Language {

    EN("English", "en"), HU("Magyar", "hu");

    String name, tag;

    Language(String name, String tag) {
        this.name = name;
        this.tag = tag;
    }

    public String getName() { return name; }
    public String getTag() { return tag; }

    public static Language getLanguageByTag(String tag) {
        for(Language language: Language.values()) {
            if(tag.equalsIgnoreCase(language.tag))
                return language;
        }
        return EN;  // default
    }
}