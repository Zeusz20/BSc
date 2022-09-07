package com.zeusz.bsc.app.dialog;

import android.app.Activity;

import com.zeusz.bsc.app.MainActivity;
import com.zeusz.bsc.app.R;
import com.zeusz.bsc.app.network.GameClient;
import com.zeusz.bsc.core.Localization;


public class WinDialog extends GameDialog {

    public WinDialog(Activity ctx, boolean win) {
        super(
            ctx,
            win ? R.drawable.win_48dp : R.drawable.lose_48dp,
            Localization.localize(win ? "game.win_caption" : "game.lose_caption"),
            Localization.localize(win ? "game.win" : "game.lose")
        );

        GameClient client = ((MainActivity) ctx).getGameClient();
        client.setState(GameClient.State.EXIT);

        setCancelable(false);
        setPositiveButton(R.string.ok, (dialog, which) -> {
            client.getGame().exit(ctx, client.isDirty());
            ((MainActivity) ctx).setGameClient(null);
        });
    }

}
