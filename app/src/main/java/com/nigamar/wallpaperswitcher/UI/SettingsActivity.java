package com.nigamar.wallpaperswitcher.UI;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SettingsActivity extends AppCompatActivity {

    public static final String CHANGE_WALLPAPER="changeWallpaper";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // will have to add a fragment in order to get the settings screen
        getSupportFragmentManager().beginTransaction()
                .replace(android.R.id.content,new SettingsFragment())
                .commit();
    }
}
