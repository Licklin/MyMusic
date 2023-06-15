package com.lickling.mymusic.ui.setting;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import com.lickling.mymusic.R;

public class SettingsFragment extends PreferenceFragmentCompat {

    @SuppressLint("ResourceType")
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.layout.fragment_setting, rootKey);

    }
}