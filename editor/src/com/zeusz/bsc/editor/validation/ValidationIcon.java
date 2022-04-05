package com.zeusz.bsc.editor.validation;

import com.zeusz.bsc.core.GWObject;
import com.zeusz.bsc.editor.gui.FastTooltip;
import com.zeusz.bsc.editor.io.ResourceLoader;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;


public class ValidationIcon extends Button {

    private GWObject object;

    public ValidationIcon(GWObject object) {
        this.object = object;
        setStyle("-fx-background-color: transparent");
        setPadding(new Insets(0.0));
        setPrefSize(24.0, 24.0);
        setFocusTraversable(false);
        validateObject();
    }

    public void validateObject() {
        Validation validation = Validation.validate(object);
        setGraphic(getIcon(validation));
        buildTooltip(validation);
    }

    private ImageView getIcon(Validation validation) {
        String path = validation.isValid() ? "img/check.png" : "img/wrong.png";
        return new ImageView(ResourceLoader.getFXImage(path));
    }

    private void buildTooltip(Validation validation) {
        if(validation.isValid()) {
            setTooltip(null);
        }
        else {
            // concat errors
            String errors = validation.getErrors().stream().reduce("", (acc, it) -> (acc + it + "\n"));

            if(getTooltip() == null)
                setTooltip(new FastTooltip(errors));
            else
                getTooltip().setText(errors);
        }
    }

}
