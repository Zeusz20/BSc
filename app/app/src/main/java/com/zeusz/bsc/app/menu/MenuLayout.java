package com.zeusz.bsc.app.menu;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import com.zeusz.bsc.core.Localization;

import java.util.Locale;


public class MenuLayout extends ConstraintLayout {

    private ImageView title;
    private LinearLayout controls;

    public MenuLayout(Activity ctx) {
        super(ctx);
        title = new ImageView(ctx);
        title.setId(View.generateViewId());
        title.setVisibility(VISIBLE);
        setTitle(ctx, Localization.getLocale());

        controls = new LinearLayout(ctx);
        controls.setId(View.generateViewId());

        addView(title);
        addView(controls);
        setConstraints();
    }

    public void setTitle(Activity ctx, Locale locale) {
        String name = locale.equals(Locale.US) ? "en_title" : "hu_title";
        int imageID = ctx.getResources().getIdentifier(name, "drawable", ctx.getPackageName());
        Log.d("ImageID: ", Integer.toString(imageID));
        title.setImageResource(imageID);
    }

    public void setConstraints() {
        ConstraintSet constraints = new ConstraintSet();
        constraints.connect(title.getId(), ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT);
        constraints.connect(title.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP);
        constraints.connect(controls.getId(), ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT);
        constraints.connect(controls.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP);
        constraints.applyTo(this);
    }

    public void addButtons(View... buttons) {
        for(View button: buttons)
            controls.addView(button);
    }

    public LinearLayout getControls() { return controls; }

}
