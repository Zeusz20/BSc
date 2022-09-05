package com.zeusz.bsc.app.widget;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.zeusz.bsc.app.MainActivity;
import com.zeusz.bsc.app.R;


public class SendButton extends View {

    private View enabled, disabled;

    public SendButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SendButton(Activity ctx) {
        this(ctx, null);

        View view = View.inflate(ctx, R.layout.send_button, null);

        enabled = view.findViewById(R.id.send_button_enabled);
        disabled = view.findViewById(R.id.send_button_disabled);

        enabled.setOnClickListener(null); // TODO send question

        toggleState((MainActivity) ctx);
    }

    /** Disables/enables the button depending on if it's the player's turn. */
    public void toggleState(MainActivity ctx) {
        boolean turn = ctx.getGameClient().getGame().getTurn();

        enabled.setVisibility(turn ? View.VISIBLE : View.INVISIBLE);
        disabled.setVisibility(turn ? View.INVISIBLE : View.VISIBLE);
    }

}
