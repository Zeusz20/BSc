package com.zeusz.bsc.app;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.zeusz.bsc.app.network.GameClient;
import com.zeusz.bsc.app.ui.Menu;
import com.zeusz.bsc.core.Localization;


public class MainActivity extends AppCompatActivity {

    private GameClient client;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        Localization.init(MainActivity.class, getFilesDir().getPath());
        Menu.show(this);
    }

    @Override
    public void onBackPressed() {
        findViewById(R.id.back_btn).callOnClick();
    }

    public void setGameClient(GameClient client) { this.client = client; }

    public GameClient getGameClient() { return client; }

}
