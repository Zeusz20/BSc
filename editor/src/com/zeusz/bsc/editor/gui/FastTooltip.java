package com.zeusz.bsc.editor.gui;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Tooltip;
import javafx.util.Duration;

import java.lang.reflect.Field;


public class FastTooltip extends Tooltip {

    private static final double DELAY = 100.0;

    public FastTooltip(String text) {
        super(text);

        try {
            Field field = Tooltip.class.getDeclaredField("BEHAVIOR");
            field.setAccessible(true);
            final Object behavior = field.get(this);    // cannot castAs because TooltipBehavior is private

            field = behavior.getClass().getDeclaredField("activationTimer");
            field.setAccessible(true);
            final Timeline activationTimer = (Timeline) field.get(behavior);

            activationTimer.getKeyFrames().clear();
            activationTimer.getKeyFrames().add(new KeyFrame(new Duration(DELAY)));
        }
        catch(NoSuchFieldException | IllegalAccessException e) {
            // could not modify activation timer
        }
    }

}
