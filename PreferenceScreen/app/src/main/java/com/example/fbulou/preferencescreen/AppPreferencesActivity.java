package com.example.fbulou.preferencescreen;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class AppPreferencesActivity extends PreferenceActivity {


    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Loading preferences from xml file
        addPreferencesFromResource(R.xml.myapppreferences);
    }
}
