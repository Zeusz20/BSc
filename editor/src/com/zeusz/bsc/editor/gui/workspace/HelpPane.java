package com.zeusz.bsc.editor.gui.workspace;

import com.zeusz.bsc.core.*;
import com.zeusz.bsc.core.Object;
import com.zeusz.bsc.editor.gui.Scrollable;
import com.zeusz.bsc.editor.gui.Style;
import com.zeusz.bsc.editor.gui.window.InfoWindow;
import com.zeusz.bsc.editor.io.HelpParser;

import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.Locale;


public final class HelpPane extends VBox {

    private static Locale locale;
    private static HelpPane objectHelpPane, attributeHelpPane, questionHelpPane;

    public static void init() {
        locale = Localization.getLocale();
        objectHelpPane = new HelpPane(Object.class);
        attributeHelpPane = new HelpPane(Attribute.class);
        questionHelpPane = new HelpPane(Question.class);
    }

    /* HelpPane factory */
    public static Scrollable getHelpFor(Class<? extends Item> type) {
        if(objectHelpPane == null || attributeHelpPane == null || questionHelpPane == null || !Localization.isCurrent(locale))
            init();

        if(type == Object.class) return new Scrollable(objectHelpPane);
        if(type == Attribute.class) return new Scrollable(attributeHelpPane);
        if(type == Question.class) return new Scrollable(questionHelpPane);
        return null;
    }

    /* Class methods and fields */
    private HelpPane(Class<? extends Item> type) {
        setPadding(Style.PADDING_MEDIUM);
        setStyle(Style.BG_WHITE);

        try {
            HelpParser parser = new HelpParser(locale);
            String name = type.getSimpleName().toLowerCase();

            Text title = new Text(Localization.capLocalize("word." + name));
            Text description = new Text(parser.getSection(name));

            title.setFont(Style.FONT_LARGE);
            description.setFont(Style.FONT_SMALL);
            description.setWrappingWidth(Style.SIDE_BAR_WIDTH);

            getChildren().addAll(title, description);
            setSpacing(Style.SPACING_LARGE);
        }
        catch(IOException e) {
            new InfoWindow(Localization.localize("error.help")).show();
        }
    }

}
