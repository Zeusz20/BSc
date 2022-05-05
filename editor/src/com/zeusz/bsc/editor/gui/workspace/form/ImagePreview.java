package com.zeusz.bsc.editor.gui.workspace.form;

import com.zeusz.bsc.core.Localization;
import com.zeusz.bsc.editor.gui.FastTooltip;
import com.zeusz.bsc.editor.io.IOManager;
import com.zeusz.bsc.editor.io.ResourceLoader;

import javafx.beans.Observable;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javax.imageio.ImageIO;

import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;


public class ImagePreview extends Label implements ItemChangeListener {

    public static final double MAX_WIDTH = 256.0, MAX_HEIGHT = 144.0;   // 16:9 ratio
    private static final Image MISSING_IMAGE = ResourceLoader.getFXImage("img/missing.png");
    private static final FastTooltip TOOLTIP = new FastTooltip(Localization.localize("error.no_image"));

    /* Class methods and fields */
    private byte[] buffer;
    private ObjectProperty<ImageView> image;

    public ImagePreview(byte[] image) {
        this.image = new SimpleObjectProperty<>();
        preview(image);
        bindListener();
    }

    public void preview(byte[] image) {
        this.buffer = image;
        Image img = (image != null) ? new Image(new ByteArrayInputStream(image)) : MISSING_IMAGE;

        // calculate fit size
        double width = Math.min(img.getWidth(), MAX_WIDTH);
        double height = Math.min(img.getHeight(), MAX_HEIGHT);

        // build ImageView
        ImageView imageView = new ImageView(img);
        imageView.setPreserveRatio(true);
        imageView.setFitWidth(width);
        imageView.setFitHeight(height);

        // display image
        imageProperty().set(imageView);
        setGraphic(imageProperty().get());
        setTooltip((img == MISSING_IMAGE) ? TOOLTIP : null);
    }

    public boolean load(String path, boolean fromURL) {
        try {
            if(fromURL) {
                // is url an image?
                BufferedImage image = ImageIO.read(new URL(path));
                if(image != null) {
                    ByteArrayOutputStream blob = new ByteArrayOutputStream();
                    ImageIO.write(image, "png", blob);
                    buffer = blob.toByteArray();
                    blob.close();
                }
                else throw new IOException();   // url was not an image
            }
            else {
                FileInputStream stream = new FileInputStream(path);
                buffer = new byte[stream.available()];
                stream.read(buffer);
                IOManager.getInstance().setImagesDir(new File(path).getParentFile());
                stream.close();
            }
        }
        catch(IOException e) {
            // couldn't read image
            buffer = null;
        }

        // display image
        preview(buffer);
        return (buffer != null);
    }

    public byte[] getImageBuffer() { return buffer; }

    public ObjectProperty<ImageView> imageProperty() { return image; }

    public Image getImage() { return imageProperty().get().getImage(); }

    @Override
    public Observable getProperty() {
        return imageProperty();
    }

}
