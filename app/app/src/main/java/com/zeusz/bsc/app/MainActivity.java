package com.zeusz.bsc.app;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.zeusz.bsc.core.Localization;
import com.zeusz.bsc.core.Project;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;


public class MainActivity extends AppCompatActivity {

    private Game game;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Localization.init(MainActivity.class, getFilesDir().getPath());
        //Menu.show(this);

        File[] files = getExternalFilesDir("projects").listFiles();
        if(files != null) {
            try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(files[0]))) {
                Project project = (Project) ois.readObject();
                new Game(this, project);
            }
            catch(Exception e) {
                // ignored
            }
        }
    }

    @Override
    public void onBackPressed() {
        // TODO
        Menu.show(this);
    }

    public Game getGame() {
        return game;
    }

}
