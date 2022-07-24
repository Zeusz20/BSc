package com.zeusz.bsc.app;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.zeusz.bsc.app.menu.MainMenu;
import com.zeusz.bsc.app.menu.MenuBuilder;
import com.zeusz.bsc.core.Localization;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Localization.init(MainActivity.class, getFilesDir().getPath());
        MenuBuilder.build(this, MainMenu.class);
    }

    @Override
    public void onBackPressed() {
        MenuBuilder.build(this, MainMenu.class);
    }

}
