package com.zeusz.bsc.editor.gui.menu;

import com.zeusz.bsc.editor.Editor;
import com.zeusz.bsc.editor.gui.workspace.HelpPane;
import com.zeusz.bsc.editor.localization.Language;
import com.zeusz.bsc.editor.localization.Localization;

import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.ToggleGroup;


public class LangMenuItem extends RadioMenuItem {

    private static final ToggleGroup LANG_GROUP = new ToggleGroup();

    static {
        // load help with selected language (see HelpPane)
        LANG_GROUP.selectedToggleProperty().addListener(((observable, oldValue, newValue) -> HelpPane.init()));
    }

    private Language language;

    public LangMenuItem(Language language) {
        this.language = language;

        String text = new StringBuilder()
                .append(language.getName())
                .append(" (")
                .append(language.getTag())
                .append(")")
                .toString();

        setText(text);
        setToggleGroup(LANG_GROUP);
        setOnAction(event -> {
            Localization.load(language);

            // redraw editor by reinitializing the same project
            // need to save old hash as well because Editor::initProject saves the current hash
            byte[] oldHash = Editor.getInstance().getHash();
            Editor.getInstance().initProject(Editor.getInstance().getProject());
            Editor.getInstance().setHash(oldHash);

            Editor.getInstance().getWindow().setTitle(Localization.localize("window.title"));
        });
    }

    public Language getLanguage() { return language; }

}
