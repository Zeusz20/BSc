package com.zeusz.bsc.app.menu;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.zeusz.bsc.core.Localization;


public class LangChooserLayout extends LinearLayout {

    private LinearLayout languages;

    public LangChooserLayout(Activity ctx) {
        super(ctx);
        setOrientation(VERTICAL);
        setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 448));

        languages = new LinearLayout(ctx);
        languages.setOrientation(LinearLayout.VERTICAL);
        languages.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

        TextView label = new TextView(ctx);
        label.setText(Localization.localize("menu.change_language"));
        label.setTextSize(24.0f);
        label.setTextColor(Color.WHITE);
        label.setTextAlignment(TEXT_ALIGNMENT_CENTER);

        ScrollView scrollView = new ScrollView(ctx);
        scrollView.addView(languages);

        super.addView(label);
        super.addView(scrollView);
    }

    @Override
    public void addView(View child) {
        languages.addView(child);
    }

}
