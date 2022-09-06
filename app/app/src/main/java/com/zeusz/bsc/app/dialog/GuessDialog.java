package com.zeusz.bsc.app.dialog;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zeusz.bsc.app.MainActivity;
import com.zeusz.bsc.app.R;
import com.zeusz.bsc.app.ui.Game;
import com.zeusz.bsc.app.util.IOManager;
import com.zeusz.bsc.core.Localization;
import com.zeusz.bsc.core.Object;


public class GuessDialog extends GameDialog {

    public GuessDialog(Activity ctx, String objectName) {
        super(ctx);

        Object object = Game.findItemByName((MainActivity) ctx, objectName, Object.class);
        boolean isGuessed = ((MainActivity) ctx).getGameClient().getGame().getObject().equals(object);

        display(ctx, object);
        setCancelable(false);
        setTitle(Localization.localize("game.guess_object"));
        setPositiveButton(Localization.localize(isGuessed ? "word.yes" : "word.no"), (dialog, which) -> {
            ((MainActivity) ctx).getGameClient().sendAnswer(object, isGuessed);
        });
    }

    private void display(Activity ctx, Object object) {
        View view = View.inflate(ctx, R.layout.object_preview, null);
        ((TextView) view.findViewById(R.id.object_name)).setText(object.getName());
        ((ImageView) view.findViewById(R.id.object_image)).setImageBitmap(IOManager.getImage(object.getImage()));

        setView(view);
    }

}
