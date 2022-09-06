package com.zeusz.bsc.app;

import android.Manifest;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.zeusz.bsc.app.dialog.GameDialog;
import com.zeusz.bsc.app.network.GameClient;
import com.zeusz.bsc.app.ui.ViewManager;
import com.zeusz.bsc.core.Localization;


public class MainActivity extends AppCompatActivity {

    private final String[] PERMISSIONS = new String[] {
        Manifest.permission.INTERNET,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    private GameClient client;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        ActivityCompat.requestPermissions(this, PERMISSIONS, hashCode());
        Localization.init(MainActivity.class, getFilesDir().getPath());
    }

    @Override
    protected void onPause() {
        if(client != null && client.isDirty())
            new GameDialog(this, Localization.localize("game.player_disconnected")).show();

        setGameClient(null);
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ViewManager.show(this, ViewManager.MAIN_MENU);
    }

    @Override
    public void onBackPressed() {
        View button = findViewById(R.id.back_button);
        if(button != null) button.callOnClick();
    }

    public void setGameClient(GameClient client) {
        if(this.client != null)
            this.client.disconnect();

        this.client = client;
    }

    public GameClient getGameClient() {
        return client;
    }

}
