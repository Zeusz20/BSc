package com.zeusz.bsc.app;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.LinearLayout;

import com.zeusz.bsc.app.layout.ObjectChooser;
import com.zeusz.bsc.core.Project;


// TODO
public final class Game {

    public Game(Activity ctx, Project project) {
        ctx.setContentView(R.layout.menu_layout);

        LinearLayout body = ctx.findViewById(R.id.menu_body);
        body.setBackgroundColor(Color.WHITE);
        body.setGravity(Gravity.NO_GRAVITY);
        body.addView(new ObjectChooser(ctx, project));
    }

}
