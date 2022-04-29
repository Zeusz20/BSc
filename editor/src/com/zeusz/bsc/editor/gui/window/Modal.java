package com.zeusz.bsc.editor.gui.window;

import com.zeusz.bsc.editor.Editor;
import com.zeusz.bsc.editor.gui.FixedButton;
import com.zeusz.bsc.editor.gui.Style;
import com.zeusz.bsc.editor.localization.Localization;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.Objects;


public abstract class Modal extends Stage {

    /* Components */
    protected static final String DEFAULT_BTN_ID = "default";
    protected static final String CANCEL_BTN_ID = "cancel";

    protected static final FixedButton okBtn = new FixedButton("OK");
    protected static final FixedButton confirmBtn = new FixedButton(Localization.capLocalize("word.yes"));
    protected static final FixedButton discardBtn = new FixedButton(Localization.capLocalize("word.no"));
    protected static final FixedButton cancelBtn = new FixedButton(Localization.capLocalize("word.cancel"));

    /* Meta data */
    public enum State { APPLY, DISCARD, CANCEL }

    protected enum Size {
        SMALL(366.0, 141.0), MEDIUM(635.0, 267.0), LARGE(800.0, 600.0);
        double width, height;
        Size(double width, double height) { this.width = width; this.height = height; }
    }

    /* Class methods and fields */
    protected State state;
    private Runnable onClose;

    public Modal() {
        super(StageStyle.UTILITY);
        initModality(Modality.WINDOW_MODAL);
        initOwner(Editor.getInstance().getWindow());
        setTitle(Editor.getInstance().getWindow().getTitle());
        setResizable(false);

        // init components
        okBtn.setOnAction(event -> closeWithState(State.APPLY));
        confirmBtn.setOnAction(event -> closeWithState(State.APPLY));
        discardBtn.setOnAction(event -> closeWithState(State.DISCARD));
        cancelBtn.setOnAction(event -> closeWithState(State.CANCEL));

        // Button::setDefaultButton and Button::setCancelButton didn't work after multiple instantiation
        okBtn.setId(DEFAULT_BTN_ID);
        confirmBtn.setId(DEFAULT_BTN_ID);
        cancelBtn.setId(CANCEL_BTN_ID);
    }

    public void setOnClose(Runnable event) { this.onClose = event; }

    public State getState() { return state; }

    public void init(Size size) {
        setWidth(size.width);
        setHeight(size.height);

        final Pane root = getContent();
        if(root == null) return;

        Scene scene = new Scene(root);
        setScene(scene);

        // enable hitting button components with enter or escape key
        scene.setOnKeyPressed(event -> {
            switch(event.getCode()) {
                case ENTER:
                    Button button = getScene().getFocusOwner() instanceof Button
                            ? (Button) getScene().getFocusOwner()   // trigger focused button
                            : findButton(root, DEFAULT_BTN_ID);   // trigger default button
                    button.fire();
                    break;

                case ESCAPE:
                    // trigger cancel button
                    findButton(root, CANCEL_BTN_ID).fire();
                    break;
            }

            event.consume();
        });
    }

    private Button findButton(Pane root, String id) {
        for(Node node: root.getChildren()) {
            if(node instanceof Button && Objects.equals(node.getId(), id)) {
                return (Button) node;
            }
            else if(node instanceof Pane) {
                return findButton((Pane) node, id);
            }
        }

        return new Button();    // dummy button
    }

    protected void closeWithState(State state) {
        this.state = state;
        this.close();
    }

    @Override
    public void close() {
        if(onClose != null) onClose.run();
        super.close();
    }

    protected abstract Content getContent();

    /* Helper class for displaying window content. */
    protected static final class Content extends BorderPane {
        private Content(Button... controls) {
            HBox controlPane = new HBox(controls);
            controlPane.setSpacing(Style.SPACING_MEDIUM);
            controlPane.setPadding(Style.PADDING_SMALL);
            controlPane.setAlignment(Pos.BOTTOM_RIGHT);
            controlPane.setId("controls");
            controlPane.getStylesheets().add("css/border.css");
            setBottom(controlPane);
        }

        /* Display message and controls */
        public Content(String message, Button... controls) {
            this(controls);
            Text text = new Text(message);
            StackPane textPane = new StackPane(text);
            text.setFill(Color.DIMGRAY.darker());
            text.setFont(Style.FONT_MEDIUM);
            textPane.setStyle(Style.BG_WHITE);
            textPane.setPadding(Style.PADDING_MEDIUM);
            textPane.setAlignment(Pos.TOP_LEFT);
            setCenter(textPane);
        }

        /* Display form and controls */
        public Content(Pane form, Button... controls) {
            this(controls);
            form.setPadding(Style.PADDING_MEDIUM);
            setCenter(form);
        }
    }

}
