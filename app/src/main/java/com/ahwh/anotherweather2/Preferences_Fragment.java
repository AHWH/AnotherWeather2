package com.ahwh.anotherweather2;

import android.os.Bundle;
import android.preference.PreferenceFragment;

/**
 * Created by weiho on 13/5/2016.
 */
public class Preferences_Fragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preference);
    }
}
