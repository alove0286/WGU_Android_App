package com.loveapps.activities;

import android.os.Bundle;
import android.preference.PreferenceActivity;

import com.loveapps.R;

public class SettingsActivity extends PreferenceActivity {

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preferences);
    }

}