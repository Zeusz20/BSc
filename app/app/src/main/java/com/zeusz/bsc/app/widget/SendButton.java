package com.zeusz.bsc.app.widget;

import android.app.Activity;
import android.content.Context;
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
import com.zeusz.bsc.app.layout.MenuLayout;
import com.zeusz.bsc.app.network.GameClient;
import com.zeusz.bsc.app.ui.ViewManager;
import com.zeusz.bsc.core.Attribute;
import com.zeusz.bsc.core.Localization;

import java.lang.reflect.Field;
import java.util.List;


public class SendButton extends MenuLayout {

    public static void globalStateToggle(Activity ctx) {
        View content = ctx.getWindow().getDecorView().findViewById(android.R.id.content);
        ((SendButton) content.findViewById(R.id.send_button)).toggleState(ctx);
    }

    protected boolean turn; // is the player's turn?

    public SendButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SendButton(Activity ctx) {
        super(ctx, LinearLayout.VERTICAL);

        try {
            // was too lazy to refactor game client's code for a single getter
            Field field = GameClient.class.getDeclaredField("isHost");
            field.setAccessible(true);
            turn = (boolean) field.get(((MainActivity) ctx).getGameClient());
        }
        catch(Exception e) {
            turn = true;
        }

        setId(R.id.send_button);
        toggleState(ctx);
    }

    /** Enables/disables the button depending on if it's the player's turn. */
    public void toggleState(Activity ctx) {
        ctx.runOnUiThread(this::removeAllViews);
        turn = !turn;

        if(turn) {
            MenuButton button = new MenuButton(ctx, Localization.localize("game.ask_question"), () -> {
                askQuestion(ctx);
                toggleState(ctx);   // disable button after sending question
            });
            ctx.runOnUiThread(() -> addView(button));
        }
        else {
            /* The button itself */
            LinearLayout wrapper = new LinearLayout(new ContextThemeWrapper(ctx, android.R.style.Widget_Holo_Light_Button));
            wrapper.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
            wrapper.setOrientation(LinearLayout.HORIZONTAL);
            wrapper.setTextAlignment(LinearLayout.TEXT_ALIGNMENT_CENTER);
            wrapper.setEnabled(false);

            // render button text
            int size = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 32.0f, ctx.getResources().getDisplayMetrics());
            ProgressBar progressBar = new ProgressBar(ctx);
            progressBar.setLayoutParams(new LayoutParams(size, size));

            TextView textView = new TextView(ctx);
            textView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
            textView.setText(Localization.localize("game.waiting_for_player"));

            // render button
            wrapper.addView(progressBar);
            wrapper.addView(textView);
            ctx.runOnUiThread(() -> addView(wrapper));
        }
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

        ((MainActivity) ctx).getGameClient().sendQuestion(attribute, value, question);
    }

}
