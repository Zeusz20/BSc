package com.zeusz.bsc.editor.gui.explorer;

import com.zeusz.bsc.core.Item;
import com.zeusz.bsc.editor.Editor;
import com.zeusz.bsc.editor.gui.Drawable;
import com.zeusz.bsc.editor.io.ResourceLoader;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.List;
import java.util.stream.Collectors;


public class Tab extends VBox implements Drawable {

    private static final String OPEN = "img/down.png";
    private static final String CLOSED = "img/right.png";
    private static final Insets OPEN_PADDING = new Insets(5.0, 10.0, 5.0, 10.0);
    private static final Insets CLOSED_PADDING = new Insets(0.0, 5.0, 0.0, 5.0);

    private Class<? extends Item> type;
    private Button controlBtn;
    private VBox items;
    private boolean closed;

    public Tab(Class<? extends Item> type, String text, double width) {
        this.type = type;
        this.closed = true;

        controlBtn = new Button(" " + text, new ImageView(ResourceLoader.getFXImage(CLOSED)));
        controlBtn.setFont(Font.font(null, FontWeight.NORMAL, 18.0));
        controlBtn.setShape(new Rectangle(width, 22.0));
        controlBtn.getStylesheets().add("css/tab.css");
        controlBtn.setPrefWidth(width);
        controlBtn.setOnAction(event -> { toggle(); draw(); });

        items = new VBox();
        items.setPadding(new Insets(0.0, 10.0, 0.0, 10.0));

        getChildren().addAll(controlBtn, items);
        setStyle("-fx-background-color: white");
    }

    public List<ItemLabel> getItems() {
        return items.getChildren().stream().map(it -> (ItemLabel) it).collect(Collectors.toList());
    }

    public boolean isClosed() { return closed; }

    public void toggle() { closed = !closed; }

    @Override
    public void draw() {
        items.getChildren().clear();
        items.setPadding(closed ? CLOSED_PADDING : OPEN_PADDING);
        controlBtn.setGraphic(new ImageView(ResourceLoader.getFXImage(closed ? CLOSED : OPEN)));

        // display items
        if(!closed) {
            Editor.getInstance().getProject().getItemList(type).forEach(it -> {
                items.getChildren().add(new ItemLabel(it));
            });
        }
    }

}
