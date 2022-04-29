package com.zeusz.bsc.editor.gui.explorer;

import com.zeusz.bsc.core.Item;
import com.zeusz.bsc.editor.Editor;
import com.zeusz.bsc.editor.gui.Drawable;
import com.zeusz.bsc.editor.gui.Style;
import com.zeusz.bsc.editor.io.ResourceLoader;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.util.List;
import java.util.stream.Collectors;


public class Tab extends VBox implements Drawable {

    private Class<? extends Item> type;
    private Button controlBtn;
    private VBox items;
    private boolean closed;

    public Tab(Class<? extends Item> type, String text) {
        this.type = type;
        this.closed = true;

        controlBtn = new Button(" " + text);
        controlBtn.setFont(Style.FONT_UNSTYLED);
        controlBtn.setShape(Style.BUTTON_SHAPE);
        controlBtn.getStylesheets().add("css/tab.css");
        controlBtn.setPrefWidth(Style.SIDE_BAR_WIDTH);
        controlBtn.setOnAction(event -> { toggle(); draw(); });

        items = new VBox();

        getChildren().addAll(controlBtn, items);
        setStyle(Style.BG_WHITE);
        draw();
    }

    public List<ItemLabel> getItems() {
        return items.getChildren().stream().map(it -> (ItemLabel) it).collect(Collectors.toList());
    }

    public boolean isClosed() { return closed; }

    public void toggle() { closed = !closed; }

    private strictfp Insets getItemPadding() {
        double[] paddings = new double[4];

        for(int i = 0; i < paddings.length; i++) {
            double value = (i % 2) != 0 ? Style.SPACING_SMALL : 0.0;
            paddings[i] = closed ? value : value + Style.SPACING_SMALL;
        }

        return new Insets(paddings[0], paddings[1], paddings[2], paddings[3]);
    }

    @Override
    public void draw() {
        items.getChildren().clear();
        items.setPadding(getItemPadding());
        controlBtn.setGraphic(new ImageView(ResourceLoader.getFXImage(closed ? "img/right.png" : "img/down.png")));

        // display items
        if(!closed) {
            Editor.getInstance().getProject().getItemList(type).forEach(it -> {
                items.getChildren().add(new ItemLabel(it));
            });
        }
    }

}
