package com.zeusz.bsc.app.dialog;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zeusz.bsc.app.MainActivity;
import com.zeusz.bsc.app.R;
import com.zeusz.bsc.app.util.Dictionary;
import com.zeusz.bsc.app.util.IOManager;
import com.zeusz.bsc.core.Localization;
import com.zeusz.bsc.core.Object;


public class GuessDialog extends GameDialog {

    public GuessDialog(Activity ctx, Object object) {
        super(ctx);

        boolean isGuessed = ((MainActivity) ctx).getGameClient().getGame().getObject().equals(object);

        // display object
        View view = View.inflate(ctx, R.layout.object_preview, null);
        ((TextView) view.findViewById(R.id.object_name)).setText(object.getName());
        ((ImageView) view.findViewById(R.id.object_image)).setImageBitmap(IOManager.getImage(object.getImage()));

        setView(view);
        setCancelable(false);
        setTitle(Localization.localize("game.guess_object"));
        setPositiveButton(Localization.localize(isGuessed ? "word.yes" : "word.no"), (dialog, which) -> {
            ((MainActivity) ctx).getGameClient().sendJSON(new Dictionary()
                .put("object", object.getName())
                .put("answer", isGuessed)
            );
        });
    }

}
