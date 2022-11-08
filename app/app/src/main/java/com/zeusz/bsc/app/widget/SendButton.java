package com.zeusz.bsc.app.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.view.ContextThemeWrapper;

import com.zeusz.bsc.app.MainActivity;
import com.zeusz.bsc.app.R;
import com.zeusz.bsc.app.dialog.ObjectListDialog;
import com.zeusz.bsc.app.layout.MenuLayout;
import com.zeusz.bsc.app.ui.ViewManager;
import com.zeusz.bsc.app.util.Dictionary;
import com.zeusz.bsc.core.Attribute;
import com.zeusz.bsc.core.Localization;
import com.zeusz.bsc.core.Object;

import java.util.List;


public class SendButton extends MenuLayout {

    /* Static functionalities */
    protected static boolean turn; // is the player's turn?

    public static void init(boolean turn) {
        SendButton.turn = turn;
    }

    public static void toggleAll(Activity ctx) {
        turn = !turn;

        View content = ctx.getWindow().getDecorView().findViewById(android.R.id.content);
        String tag = ctx.getResources().getString(R.string.send_button);

        for(View view: ViewManager.findViewsByTag(content, tag))
            ((SendButton) view).toggle(ctx);
    }

    /* Class fields and methods */
    protected String text;
    protected boolean isGuess, showWaiting;

    public SendButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SendButton(Activity ctx, String text, boolean isGuess, boolean showWaiting) {
        super(ctx, LinearLayout.VERTICAL);

        this.text = text.toUpperCase();
        this.isGuess = isGuess;
        this.showWaiting = showWaiting;

        setTag(ctx.getResources().getString(R.string.send_button));
        toggle(ctx);
    }

    /** Enables/disables the button depending on if it's the player's turn. */
    protected void toggle(Activity ctx) {
        ctx.runOnUiThread(this::removeAllViews);

        if(turn) {
            MenuButton button = new MenuButton(ctx, text, () -> {
                if(isGuess) guessObject(ctx);
                else askQuestion(ctx);
            });
            button.setBackgroundResource(R.drawable.positive_btn_bg);
            ctx.runOnUiThread(() -> addView(button));
        }
        else {
            /* The button itself */
            MarginLayoutParams layoutParams = new MarginLayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(0, 8, 0, 8);

            LinearLayout wrapper = new LinearLayout(new ContextThemeWrapper(ctx, android.R.style.Widget_Holo_Light_Button));
            wrapper.setLayoutParams(layoutParams);
            wrapper.setOrientation(LinearLayout.HORIZONTAL);
            wrapper.setTextAlignment(LinearLayout.TEXT_ALIGNMENT_CENTER);
            wrapper.setEnabled(false);

            // render button text
            int size = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 32.0f, ctx.getResources().getDisplayMetrics());
            ProgressBar progressBar = new ProgressBar(ctx);
            progressBar.setLayoutParams(new LayoutParams(size, size));

            TextView textView = new TextView(ctx);
            textView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
            textView.setText(showWaiting ? Localization.localize("game.waiting_for_player") : text);
            textView.setTextColor(Color.WHITE);

            // render button
            if(showWaiting) wrapper.addView(progressBar);
            wrapper.addView(textView);
            ctx.runOnUiThread(() -> addView(wrapper));
        }
    }

    protected void guessObject(Activity ctx) {
        List<Object> objects = ((MainActivity) ctx).getGameClient().getGame().getProject().getItemList(Object.class);
        new ObjectListDialog(ctx, objects).show();
    }

    protected void askQuestion(Activity ctx) {
        List<Attribute> attributes = ((MainActivity) ctx).getGameClient().getGame().getProject().getItemList(Attribute.class);
        View content = ctx.getWindow().getDecorView().findViewById(android.R.id.content);

        // find the attribute and its value to which the question is referring
        TextView attributeView = content.findViewById(R.id.selected_attribute_name);
        Spinner spinner = content.findViewById(R.id.attribute_spinner);

        Attribute attribute = attributes.stream()
                .filter(it -> it.getName().equals(attributeView.getText().toString()))
                .findAny().get();

        // send question
        String value = spinner.getSelectedItem().toString();
        String question = ViewManager.buildQuestion(ctx, null, attribute, false);

        SendButton.toggleAll(ctx);

        ((MainActivity) ctx).getGameClient().sendJSON(new Dictionary()
            .put("attribute", attribute.getName())
            .put("value", value)
            .put("question", question)
        );
    }

}
