package com.grayherring.nyc_opendata.activities;

import android.app.Activity;
import android.os.Bundle;

import com.grayherring.nyc_opendata.R;
import com.grayherring.nyc_opendata.fragments.NyCrashPreferenceFragment;


public class SettingsActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        setContentView(R.layout.activity_setting);
        getFragmentManager().beginTransaction().replace(R.id.container,
                new NyCrashPreferenceFragment()).commit();
    }


}
