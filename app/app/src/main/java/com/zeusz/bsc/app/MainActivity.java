package com.zeusz.bsc.app;

import android.Manifest;
import android.os.Bundle;

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
        Menu.show(this);
    }

    @Override
    public void onBackPressed() {
        findViewById(R.id.back_btn).callOnClick();
    }

    public void destroyGameClient() {
        if(client != null) {
            client.close();
            client = null;
        }
    }

    public void setGameClient(GameClient client) {
        this.client = client;
    }

    public GameClient getGameClient() {
        return client;
    }

}
