package com.zeusz.bsc.app;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.zeusz.bsc.app.menu.Menu;
import com.zeusz.bsc.core.Localization;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Localization.init(MainActivity.class, getFilesDir().getPath());
        Menu.show(this, Menu.MAIN_MENU);
    }

    @Override
    public void onBackPressed() {
        Menu.show(this, Menu.MAIN_MENU);
    }

}
