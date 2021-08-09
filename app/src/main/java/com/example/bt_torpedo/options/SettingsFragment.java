package com.example.bt_torpedo.options;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import com.example.bt_torpedo.R;

// needed to create shared preferences
public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        // sets the contents of the preferences
        setPreferencesFromResource(R.xml.preference, rootKey);
    }
}