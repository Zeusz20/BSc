package com.zeusz.bsc.app;

import android.os.Bundle;
import android.os.StrictMode;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.zeusz.bsc.app.network.Client;
import com.zeusz.bsc.core.Localization;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        Localization.init(MainActivity.class, getFilesDir().getPath());
        //Menu.show(this);

        try {
            // TODO fix policy hack
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            setContentView(R.layout.commtest_layout);
            Client client = new Client(this);
            ((Button) findViewById(R.id.create_game_btn)).setOnClickListener(listener -> {
                new Thread(client::create).start();
            });
        }
        catch(Exception e) {

        }
    }

    @Override
    public void onBackPressed() {
        // TODO
        Menu.show(this);
    }

}
