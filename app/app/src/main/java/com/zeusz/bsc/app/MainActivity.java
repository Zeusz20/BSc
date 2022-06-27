package com.zeusz.bsc.app;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.zeusz.bsc.app.menu.Menu;
import com.zeusz.bsc.core.Localization;


public class MainActivity extends AppCompatActivity {

    // TODO
    //  move files (manual scene management)
    //  change language button
    //  fix localization

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Localization.init(MainActivity.class);
        setContentView(R.layout.menu_screen);
        Menu.initMenu(this, Menu.MAIN_MENU);
    }

    @Override
    public void onBackPressed() {
        setContentView(R.layout.menu_screen);
        Menu.initMenu(this, Menu.MAIN_MENU);
    }

}
