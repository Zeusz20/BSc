package com.zeusz.bsc.app.ui;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.zeusz.bsc.app.MainActivity;
import com.zeusz.bsc.app.R;
import com.zeusz.bsc.app.adapter.ValueListAdapter;
import com.zeusz.bsc.app.dialog.AttributeListDialog;
import com.zeusz.bsc.app.dialog.GameDialog;
import com.zeusz.bsc.app.layout.HistoryLayout;
import com.zeusz.bsc.app.layout.JSWebView;
import com.zeusz.bsc.app.layout.LanguageChooser;
import com.zeusz.bsc.app.layout.MenuLayout;
import com.zeusz.bsc.app.layout.ObjectList;
import com.zeusz.bsc.app.layout.ProjectList;
import com.zeusz.bsc.app.network.GameClient;
import com.zeusz.bsc.app.util.IOManager;
import com.zeusz.bsc.app.widget.BackButton;
import com.zeusz.bsc.app.widget.ConcedeButton;
import com.zeusz.bsc.app.widget.Label;
import com.zeusz.bsc.app.widget.LoadingIcon;
import com.zeusz.bsc.app.widget.MenuButton;
import com.zeusz.bsc.app.widget.SendButton;
import com.zeusz.bsc.app.widget.TextInput;
import com.zeusz.bsc.app.widget.Title;
import com.zeusz.bsc.core.Attribute;
import com.zeusz.bsc.core.Localization;
import com.zeusz.bsc.core.Object;
import com.zeusz.bsc.core.Project;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


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

    public static int pixelsToDip(Activity ctx, int pixels) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, (float) pixels, ctx.getResources().getDisplayMetrics());
    }

    public static void toast(Context context, String message) {
        if(context instanceof ContextThemeWrapper)
            context = ((ContextThemeWrapper) context).getBaseContext();

        final Activity ctx = (Activity) context;
        ctx.runOnUiThread(() -> Toast.makeText(ctx, message, Toast.LENGTH_LONG).show());
    }

    public static List<View> findViewsByTag(View root, String tag) {
        List<View> views = new ArrayList<>();

        if(root instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) root;

            for(int i = 0; i < group.getChildCount(); i++) {
                View child = group.getChildAt(i);

                if(Objects.equals(child.getTag(), tag))
                    views.add(child);

                if(child instanceof ViewGroup)
                    views.addAll(findViewsByTag((ViewGroup) child, tag));
            }
        }

        return views;
    }

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
                header.addView(new Label(ctx, Localization.localize("menu.choose_project")));
                body.setGravity(Gravity.NO_GRAVITY);
                body.addView(new ProjectList(ctx));
                footer.addViews(new BackButton(ctx, MAIN_MENU));
                break;

            case OPTIONS_MENU:
                header.addView(new Label(ctx, Localization.localize("menu.change_language")));
                body.addView(new LanguageChooser(ctx));
                footer.addView(new BackButton(ctx, MAIN_MENU));
                break;

            case DOWNLOAD_MENU:
                body.addView(JSWebView.getWebView());
                footer.addView(new BackButton(ctx, MAIN_MENU));
                break;

            case LOADING_SCREEN:
                header.addViews(
                        new Label(ctx, Localization.localize("game.game_id")),
                        new Label(ctx, ((MainActivity) ctx).getGameClient().getId(), 48.0f),
                        new Label(ctx, ((MainActivity) ctx).getGameClient().getGame().getProject().getName())
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
                body.setGravity(Gravity.NO_GRAVITY);
                body.addView(new ObjectList(ctx, objects));
                footer.addView(new ConcedeButton(ctx));
                break;

            case GAME_SCREEN:
                View questionLayout = inflate(ctx, R.layout.question_layout);
                ViewGroup wrapper = questionLayout.findViewById(R.id.button_wrapper);
                wrapper.addView(new SendButton(ctx, Localization.localize("game.ask_question"), false, true));

                header.addViews(inflate(ctx, R.layout.selected_object));
                body.addViews(questionLayout);
                footer.addViews(
                        new Label(ctx, Localization.localize("game.history"), 16.0f),
                        new HistoryLayout(ctx),
                        new SendButton(ctx, Localization.localize("game.guess"), true, false),
                        new ConcedeButton(ctx)
                );
                break;
        }
    }

    /** Inflates in-game layouts. */
    private static View inflate(Activity ctx, int layoutId) {
        Project project = ((MainActivity) ctx).getGameClient().getGame().getProject();
        Object object = ((MainActivity) ctx).getGameClient().getGame().getObject();

        View root = View.inflate(ctx, layoutId, null);

        // couldn't use switch because resource ids are not final
        if(layoutId == R.layout.selected_object) {
            ImageView info = root.findViewById(R.id.info_button);
            ImageView image = root.findViewById(R.id.selected_object_image);
            TextView name = root.findViewById(R.id.selected_object_name);
            Button help = root.findViewById(R.id.help_button);

            info.setOnClickListener(view -> new AttributeListDialog(ctx, object).show());
            image.setImageBitmap(IOManager.getImage(object.getImage()));
            name.setText(object.getName());
            help.setOnClickListener(view -> {
                new GameDialog(ctx, R.drawable.icon,
                        Localization.localize("game.help_caption"), Localization.localize("game.help")
                ).show();
            });
        }
        else if(layoutId == R.layout.question_layout) {
            Attribute attribute = project.getItemList(Attribute.class).get(0); // default to 1st attribute in project

            // attribute selection
            //TextView caption = root.findViewById(R.id.selected_attribute_caption);
            TextView name = root.findViewById(R.id.selected_attribute_name);
            Button selectBtn = root.findViewById(R.id.select_attribute_button);

            //caption.setText(Localization.localize("game.selected_attribute_caption"));
            name.setText(attribute.getName());
            selectBtn.setText(Localization.localize("game.attributes"));
            selectBtn.setOnClickListener(view -> new AttributeListDialog(ctx, null).show());

            // attribute render
            buildQuestion(ctx, root, attribute, true);
        }

        return root;
    }

    public static String buildQuestion(Activity ctx, @Nullable View view, Attribute attribute, boolean render) {
        if(view == null)
            view = ctx.getWindow().getDecorView().findViewById(android.R.id.content);

        String[] text = attribute.getQuestion().split("\\{\\$attr\\}");

        TextView name = view.findViewById(R.id.selected_attribute_name);
        TextView question1 = view.findViewById(R.id.question_part_1);
        TextView question2 = view.findViewById(R.id.question_part_2);
        Spinner spinner = view.findViewById(R.id.attribute_spinner);

        if(render) {
            name.setText(attribute.getName());
            spinner.setAdapter(new ValueListAdapter(ctx, R.layout.value_item, attribute.getValues()));
            question1.setText(text[0]);

            // the substitute pattern may be at the end
            if(text.length > 1)
                question2.setText(text[1]);
        }

        return question1.getText().toString() + spinner.getSelectedItem().toString() + question2.getText().toString();
    }

    public static View displayObject(Activity ctx, Object object) {
        View view = View.inflate(ctx, R.layout.object_preview, null);
        ((TextView) view.findViewById(R.id.object_name)).setText(object.getName());
        ((ImageView) view.findViewById(R.id.object_image)).setImageBitmap(IOManager.getImage(object.getImage()));

        return view;
    }

}
