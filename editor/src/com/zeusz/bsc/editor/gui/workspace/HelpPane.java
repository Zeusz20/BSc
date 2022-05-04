package com.zeusz.bsc.editor.gui.workspace;

import com.zeusz.bsc.core.Attribute;
import com.zeusz.bsc.core.Item;
import com.zeusz.bsc.core.Object;
import com.zeusz.bsc.core.Question;
import com.zeusz.bsc.editor.gui.Scrollable;
import com.zeusz.bsc.editor.gui.Style;
import com.zeusz.bsc.editor.gui.window.InfoWindow;
import com.zeusz.bsc.editor.io.ResourceLoader;
import com.zeusz.bsc.editor.localization.Language;
import com.zeusz.bsc.editor.localization.Localization;

import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;


public final class HelpPane extends VBox {

    private static Language language;
    private static HelpPane objectHelpPane, attributeHelpPane, questionHelpPane;

    public static void init() {
        language = Localization.getLanguage();
        objectHelpPane = new HelpPane(Object.class);
        attributeHelpPane = new HelpPane(Attribute.class);
        questionHelpPane = new HelpPane(Question.class);
    }

    /* HelpPane factory */
    public static Scrollable getHelpFor(Class<? extends Item> type) {
        boolean isUninitialized = (objectHelpPane == null || attributeHelpPane == null || questionHelpPane == null);
        boolean languageChanged = (language != Localization.getLanguage());

        if(isUninitialized || languageChanged) init();

        if(type == Object.class) return new Scrollable(objectHelpPane);
        if(type == Attribute.class) return new Scrollable(attributeHelpPane);
        if(type == Question.class) return new Scrollable(questionHelpPane);
        return null;
    }

    /* Class methods and fields */
    private HelpPane(Class<? extends Item> type) {
        setPadding(Style.PADDING_MEDIUM);
        setStyle(Style.BG_WHITE);

        String path = new StringBuilder()
                .append("locale/help/")
                .append(Localization.getLanguage().getTag())
                .append("_")
                .append(type.getSimpleName().toLowerCase())
                .append(".txt")
                .toString();

        Text title = new Text(Localization.capLocalize("word." + type.getSimpleName().toLowerCase()));
        Text description = new Text();

        try(InputStreamReader reader = new InputStreamReader(ResourceLoader.getFile(path), StandardCharsets.UTF_8);
            Scanner scanner = new Scanner(reader)) {

            StringBuilder content = new StringBuilder();
            while(scanner.hasNextLine())
                content.append(scanner.nextLine()).append(" ");

            title.setFont(Style.FONT_LARGE);
            description.setText(content.toString());
            description.setFont(Style.FONT_SMALL);
            description.setWrappingWidth(Style.SIDE_BAR_WIDTH);

            getChildren().addAll(title, description);
            setSpacing(Style.SPACING_LARGE);
        }
        catch(Exception e) {    // file could be null
            new InfoWindow(Localization.localize("error.help")).show();
        }
    }

}
