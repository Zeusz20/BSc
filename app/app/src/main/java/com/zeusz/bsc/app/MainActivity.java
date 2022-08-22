package com.zeusz.bsc.app;

import android.Manifest;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.zeusz.bsc.app.network.GameClient;
import com.zeusz.bsc.app.ui.Menu;
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
        setGameClient(null);
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Menu.show(this);
    }

    @Override
    public void onBackPressed() {
        View button = findViewById(R.id.back_btn);
        if(button != null) button.callOnClick();
    }

    public void setGameClient(GameClient client) {
        if(this.client != null)
            this.client.close();

        this.client = client;
    }

    public GameClient getGameClient() {
        return client;
    }

}
