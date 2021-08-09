package com.example.bt_torpedo.options;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import static com.example.bt_torpedo.entry.BT_Activity.sharedPrefs;

import static com.example.bt_torpedo.game.TorpedoActivity.silent;

// activity for setting shared preferences
public class SettingsActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {
    boolean silentMode;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        silentMode = false;
        getSupportFragmentManager()
                .beginTransaction()
                .replace(android.R.id.content, new SettingsFragment()) // takes the BT_Torpedo preferences settings instead of the standard
                .commit();
        sharedPrefs.registerOnSharedPreferenceChangeListener(this);
    }

    // handling status change: set variable "silent" depending how SilentMode setting has been changed
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        silentMode = sharedPrefs.getBoolean("silent", false);
        if (silentMode) {
            Toast.makeText(this, "Silent mode selected", Toast.LENGTH_SHORT).show();
            silent = true;
        } else if (!silentMode) {
            silent = false;
        }
    }
}
