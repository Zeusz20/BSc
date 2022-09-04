package com.zeusz.bsc.editor.gui.explorer;

import com.zeusz.bsc.core.*;
import com.zeusz.bsc.core.Object;
import com.zeusz.bsc.editor.gui.Drawable;
import com.zeusz.bsc.editor.gui.Style;

import javafx.scene.control.Hyperlink;
import javafx.scene.layout.VBox;


public class SideMenu extends VBox implements Drawable {

    /* ItemLabel management */
    private static Hyperlink label;

    public static void setActiveLabel(ItemLabel itemLabel) {
        if(label != null) label.setDisable(false);  // release previous label
        label = (Hyperlink) itemLabel.getLeft();  // save new label
        label.setDisable(true); // lock new label
    }

    public static Hyperlink getActiveLabel() { return label; }

    /* Class methods and fields */
    private Tab objectTab, attributeTab, questionTab;

    public SideMenu() {
        setWidth(Style.SIDE_BAR_WIDTH);
        setStyle(Style.BG_WHITE);

        objectTab = new Tab(Object.class, Localization.localize("side.objects"));
        attributeTab = new Tab(Attribute.class, Localization.localize("side.attributes"));
        //questionTab = new Tab(Question.class, Localization.localize("side.questions"));

        getChildren().addAll(objectTab, attributeTab/*,  questionTab*/);
    }

    public Tab getTabByType(Class<? extends Item> type) {
        if(type == Object.class) return objectTab;
        if(type == Attribute.class) return attributeTab;
        if(type == Question.class) return questionTab;
        return null;
    }

    @Override
    public void draw() {
        objectTab.draw();
        attributeTab.draw();
        //questionTab.draw();
    }

}
