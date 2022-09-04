package com.zeusz.bsc.app.ui;

import android.app.Activity;
import android.graphics.Color;
import android.text.Editable;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.zeusz.bsc.app.MainActivity;
import com.zeusz.bsc.app.R;
import com.zeusz.bsc.app.layout.JSWebView;
import com.zeusz.bsc.app.layout.LanguageChooser;
import com.zeusz.bsc.app.layout.MenuLayout;
import com.zeusz.bsc.app.layout.ObjectChooser;
import com.zeusz.bsc.app.layout.ProjectChooser;
import com.zeusz.bsc.app.network.GameClient;
import com.zeusz.bsc.app.util.IOManager;
import com.zeusz.bsc.app.widget.BackButton;
import com.zeusz.bsc.app.widget.ConcedeButton;
import com.zeusz.bsc.app.widget.Label;
import com.zeusz.bsc.app.widget.LoadingIcon;
import com.zeusz.bsc.app.widget.MenuButton;
import com.zeusz.bsc.app.widget.TextInput;
import com.zeusz.bsc.app.widget.Title;
import com.zeusz.bsc.core.Localization;
import com.zeusz.bsc.core.Object;
import com.zeusz.bsc.core.Project;

import java.util.List;


public final class ViewManager {

    private ViewManager() { }

    public static final int MAIN_MENU = 0;
    public static final int LOBBY_MENU = 1;
    public static final int PROJECTS_MENU = 2;
    public static final int OPTIONS_MENU = 3;
    public static final int DOWNLOAD_MENU = 4;
    public static final int LOADING_SCREEN = 5;
    public static final int OBJECT_SELECTION = 6;
    public static final int GAME_SCREEN = 7;

    public static void show(Activity ctx, int layoutId) {
        View menu = View.inflate(ctx, R.layout.base_layout, null);

        ConstraintLayout root = menu.findViewById(R.id.root_layout);
        MenuLayout header = menu.findViewById(R.id.header_layout);
        MenuLayout body = menu.findViewById(R.id.body_layout);
        MenuLayout footer = menu.findViewById(R.id.footer_layout);

        render(ctx, layoutId, root, header, body, footer);
        ctx.runOnUiThread(() -> ctx.setContentView(menu));
    }

    private static void render(Activity ctx, int menuId, ConstraintLayout root, MenuLayout header, MenuLayout body, MenuLayout footer) {
        switch(menuId) {
            case MAIN_MENU:
                header.addView(new Title(ctx));
                body.addViews(
                        new MenuButton(ctx, Localization.localize("menu.create_game"), () -> show(ctx, PROJECTS_MENU)),
                        new MenuButton(ctx, Localization.localize("menu.join_game"), () -> show(ctx, LOBBY_MENU)),
                        new MenuButton(ctx, Localization.localize("menu.download"), () -> show(ctx, DOWNLOAD_MENU)),
                        new MenuButton(ctx, Localization.localize("menu.options"), () -> show(ctx, OPTIONS_MENU))
                );
                footer.addView(new MenuButton(ctx, Localization.localize("menu.exit"), ctx::finish));
                break;

            case LOBBY_MENU:
                header.addView(new Title(ctx));
                body.addViews(
                        new Label(ctx, Localization.localize("menu.enter_id"), 18.0f),
                        new TextInput(ctx),
                        new MenuButton(ctx, Localization.localize("menu.connect"), () -> {
                            Editable raw = ((TextInput) ctx.findViewById(R.id.text_input)).getText();
                            if(raw != null)
                                GameClient.joinGame(ctx, raw.toString().toUpperCase());
                        })
                );
                footer.addView(new BackButton(ctx, MAIN_MENU));
                break;

            case PROJECTS_MENU:
                ProjectChooser chooser = new ProjectChooser(ctx);
                LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

                params.setMargins(4, 0, 4, 0);
                chooser.setLayoutParams(params);

                header.addView(new Label(ctx, Localization.localize("menu.choose_project")));
                body.setGravity(Gravity.NO_GRAVITY);
                body.setBackgroundColor(Color.WHITE);
                body.addView(chooser);
                footer.addViews(new BackButton(ctx, MAIN_MENU));
                break;

            case OPTIONS_MENU:
                header.addView(new Label(ctx, Localization.localize("menu.change_language")));
                body.addView(new LanguageChooser(ctx));
                footer.addView(new BackButton(ctx, MAIN_MENU));
                break;

            case DOWNLOAD_MENU:
                JSWebView browser = new JSWebView(ctx, "/user/search/?lang=" + Localization.getLocale().getLanguage());
                browser.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

                body.addView(browser);
                footer.addView(new BackButton(ctx, MAIN_MENU));
                break;

            case LOADING_SCREEN:
                header.addViews(
                        new Label(ctx, Localization.localize("game.your_id")),
                        new Label(ctx, ((MainActivity) ctx).getGameClient().getId(), 48.0f)
                );
                body.addViews(
                        new Label(ctx, Localization.localize("game.waiting_for_player")),
                        new LoadingIcon(ctx)
                );
                footer.addView(new BackButton(ctx, PROJECTS_MENU, "word.cancel"));
                break;

            case OBJECT_SELECTION:
                List<Object> objects = ((MainActivity) ctx).getGameClient().getGame().getProject().getItemList(Object.class);

                header.addView(new Label(ctx, Localization.localize("game.choose_object")));
                body.addView(new ObjectChooser(ctx, objects));
                footer.addView(new ConcedeButton(ctx));
                break;

            case GAME_SCREEN:
                header.addView(inflate(ctx, R.layout.selected_object));
                footer.addView(new ConcedeButton(ctx));
                break;
        }
    }

    /** Inflates in-game layouts. */
    private static View inflate(Activity ctx, int layoutId) {
        Project project = ((MainActivity) ctx).getGameClient().getGame().getProject();
        Object object = ((MainActivity) ctx).getGameClient().getGame().getObject();

        View view = View.inflate(ctx, layoutId, null);

        // couldn't use switch because resource ids are not final
        if(layoutId == R.layout.selected_object) {
            ImageView image = view.findViewById(R.id.selected_object_image);
            TextView name = view.findViewById(R.id.selected_object_name);
            ImageView info = view.findViewById(R.id.info_button);

            image.setImageBitmap(IOManager.getImage(object.getImage()));
            name.setText(object.getName());
            info.setOnClickListener(listener -> DialogBuilder.showAttributeList(ctx, object));
        }

        return view;
    }

}
