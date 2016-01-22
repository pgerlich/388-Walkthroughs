package com.example.filesandpreferences;

import android.content.Context;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A placeholder fragment containing a simple view.
 */
public class SettingsActivityFragment extends PreferenceFragment {

    public SettingsActivityFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PreferenceManager prefMgr = getPreferenceManager();
        prefMgr.setSharedPreferencesName(MainActivity.SHARED_PREF);
        prefMgr.setSharedPreferencesMode(Context.MODE_PRIVATE);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.book_preferences);
    }
}
