package com.zeusz.bsc.editor.gui;

import javafx.geometry.Insets;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;


public final class Style {

    private Style() { }

    // Side Bars
    public static final double SIDE_BAR_WIDTH = 256.0;
    public static final Shape BUTTON_SHAPE = new Rectangle(SIDE_BAR_WIDTH, 22.0);

    // Buttons and icons
    public static final double ICON_SIZE = 24.0;
    public static final double BUTTON_SIZE = 70.0;
    public static final double LABEL_SIZE = 192.0;

    // Spacings
    public static final double SPACING_SMALL = 5.0;
    public static final double SPACING_MEDIUM = 8.0;
    public static final double SPACING_LARGE = 10.0;

    // Paddings
    public static final Insets PADDING_NONE = new Insets(0.0);
    public static final Insets PADDING_SMALL = new Insets(5.0);
    public static final Insets PADDING_MEDIUM = new Insets(10.0);
    public static final Insets PADDING_LARGE = new Insets(20.0);
    public static final Insets PADDING_BOTTOM = new Insets(0.0, 0.0, 5.0, 0.0);

    // Fonts
    public static final Font FONT_SMALL = Font.font(16.0);
    public static final Font FONT_MEDIUM = Font.font(18.0);
    public static final Font FONT_LARGE = Font.font(22.0);
    public static final Font FONT_UNSTYLED = Font.font(null, FontWeight.NORMAL, 18.0);

    // Backgrounds
    public static final String BG_WHITE = "-fx-background-color: white";
    public static final String BG_TRANSPARENT = "-fx-background-color: transparent";

}
