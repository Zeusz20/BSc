package com.zeusz.bsc.editor.gui.menu;

import com.zeusz.bsc.core.Localization;
import com.zeusz.bsc.editor.Editor;
import com.zeusz.bsc.editor.gui.workspace.HelpPane;

import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.ToggleGroup;

import java.util.Locale;


public class LangMenuItem extends RadioMenuItem {

    private static final ToggleGroup LANG_GROUP = new ToggleGroup();

    static {
        // load help with selected language (see HelpPane)
        LANG_GROUP.selectedToggleProperty().addListener(((observable, oldValue, newValue) -> HelpPane.init()));
    }

    private Locale locale;

    public LangMenuItem(Locale locale) {
        this.locale = locale;

        // some language names are not capitalized
        String language = locale.getDisplayLanguage(locale);
        language = language.substring(0, 1).toUpperCase(locale) + language.substring(1);

        String text = new StringBuilder()
                .append(language)
                .append(" (")
                .append(locale.getLanguage())
                .append(")")
                .toString();

        setText(text);
        setToggleGroup(LANG_GROUP);
        setOnAction(event -> {
            Localization.load(this.locale);

            // redraw editor by reinitializing the same project
            // need to save old hash as well because Editor::initProject saves the current hash
            byte[] oldHash = Editor.getInstance().getHash();
            Editor.getInstance().initProject(Editor.getInstance().getProject());
            Editor.getInstance().setHash(oldHash);

            Editor.getInstance().getWindow().setTitle(Localization.localize("window.title"));
        });
    }

    public Locale getLocale() { return locale; }

}
