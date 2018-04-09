package com.nigamar.wallpaperswitcher.UI;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.nigamar.wallpaperswitcher.R;

public class MainActivity extends AppCompatActivity {
    private Fragment wallpaperListFragment;
    SharedPreferences sharedPreferences;
    boolean autoSwitchWallpaper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferences=PreferenceManager.getDefaultSharedPreferences(this);
        autoSwitchWallpaper=sharedPreferences.getBoolean(SettingsActivity.CHANGE_WALLPAPER,false);
        loadWallpaperViewFragment(autoSwitchWallpaper);
        PreferenceManager.setDefaultValues(this,R.xml.preferences,false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.settings:
                Intent intent=new Intent(this,SettingsActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void loadWallpaperViewFragment(boolean autoSwitchWallpaper) {
        wallpaperListFragment= DisplayWallpaperList.newInstance(autoSwitchWallpaper);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.baseContainer,wallpaperListFragment)
                .commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
