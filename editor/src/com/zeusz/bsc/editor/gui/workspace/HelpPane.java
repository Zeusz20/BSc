package com.zeusz.bsc.editor.gui.workspace;

import com.zeusz.bsc.core.Attribute;
import com.zeusz.bsc.core.Item;
import com.zeusz.bsc.core.Object;
import com.zeusz.bsc.core.Question;
import com.zeusz.bsc.editor.gui.Scrollable;
import com.zeusz.bsc.editor.gui.window.InfoWindow;
import com.zeusz.bsc.editor.localization.Language;
import com.zeusz.bsc.editor.localization.Localization;
import com.zeusz.bsc.editor.io.ResourceLoader;

import javafx.geometry.Insets;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

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
        setPadding(new Insets(10.0));
        setStyle("-fx-background-color: white");

        String path = new StringBuilder()
                .append("locale/help/")
                .append(Localization.getLanguage().getTag())
                .append("_")
                .append(type.getSimpleName().toLowerCase())
                .append(".txt")
                .toString();

        Text title = new Text(Localization.capLocalize("word." + type.getSimpleName().toLowerCase()));
        Text description = new Text();

        try(Scanner scanner = new Scanner(ResourceLoader.getFile(path))) {
            StringBuilder content = new StringBuilder();
            while(scanner.hasNextLine())
                content.append(scanner.nextLine()).append(" ");

            title.setFont(Font.font(22.0));
            description.setText(content.toString());
            description.setFont(Font.font(16.0));
            description.setWrappingWidth(256.0);

            getChildren().addAll(title, description);
            setSpacing(10.0);
        }
        catch(Exception e) {    // file could be null
            new InfoWindow(Localization.localize("error.help")).show();
        }
    }

}
