package com.zeusz.bsc.app.menu;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;

import com.zeusz.bsc.app.widget.ProjectListAdapter;
import com.zeusz.bsc.core.Localization;

import java.io.File;


public class ProjectsMenu extends Menu {

    public ProjectsMenu(Activity ctx, LinearLayout header, LinearLayout body, LinearLayout footer) {
        super(ctx, header, body, footer);
    }

    @Override
    protected void buildHeader(Activity ctx, LinearLayout header) {
        header.addView(MenuBuilder.getLabel(ctx, Localization.localize("menu.choose_project")));
    }

    @Override
    protected void buildBody(Activity ctx, LinearLayout body) {
        ListView projects = new ListView(ctx);
        LayoutParams params = new LayoutParams(LayoutParams. MATCH_PARENT, LayoutParams.MATCH_PARENT);

        params.setMargins(4, 0, 4, 0);
        projects.setLayoutParams(params);

        // generate list adapter
        File[] files = ctx.getExternalFilesDir("projects").listFiles();

        if(files != null) {
            ProjectListAdapter adapter = new ProjectListAdapter(ctx, files);
            projects.setAdapter(adapter);
            projects.setClickable(true);
            projects.setOnItemClickListener(null);  // TODO
        }

        body.setGravity(Gravity.NO_GRAVITY);
        body.setBackgroundColor(Color.WHITE);
        body.addView(projects);
    }

    @Override
    protected void buildFooter(Activity ctx, LinearLayout footer) {
        footer.setOrientation(LinearLayout.HORIZONTAL);
        footer.addView(MenuBuilder.getBackButton(ctx, MainMenu.class));
    }

}
