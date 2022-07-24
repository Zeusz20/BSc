package com.zeusz.bsc.app.menu;

import android.app.Activity;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zeusz.bsc.app.R;
import com.zeusz.bsc.app.widget.MenuButton;
import com.zeusz.bsc.core.Localization;


public final class MenuBuilder {

    private MenuBuilder() { }

    public static void build(Activity ctx, Class<? extends Menu> menu) {
        ctx.setContentView(R.layout.menu_layout);

        LinearLayout header = ctx.findViewById(R.id.menu_header);
        LinearLayout body = ctx.findViewById(R.id.menu_body);
        LinearLayout footer = ctx.findViewById(R.id.menu_footer);

        try {
            menu.getConstructor(Activity.class, LinearLayout.class, LinearLayout.class, LinearLayout.class)
                    .newInstance(ctx, header, body, footer);
        }
        catch(Exception e) {
            // couldn't build menu
        }
    }

    public static ImageView getTitleImage(Activity ctx) {
        ImageView title = new ImageView(ctx);
        title.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 256));

        try {
            String resource = Localization.getLocale().getLanguage() + "_title";
            Integer id = (Integer) R.drawable.class.getField(resource).get(null);
            if(id != null) title.setImageResource(id);
        }
        catch(Exception e) {
            // drawable doesn't exist
        }

        return title;
    }

    public static TextView getLabel(Activity ctx, String text) {
        TextView label = new TextView(ctx);
        label.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        label.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24.0f);
        label.setTextColor(Color.WHITE);
        label.setText(text);

        return label;
    }

    public static MenuButton getBackButton(Activity ctx, Class<? extends Menu> menu) {
        MenuButton button = new MenuButton(ctx, Localization.localize("menu.back"));
        button.setOnClickListener(listener -> build(ctx, menu));

        return button;
    }

}
