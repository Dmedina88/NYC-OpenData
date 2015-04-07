package com.grayherring.nyc_opendata.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by David on 4/4/2015.
 */
public class NyCrashPrefManager {

    public static final String LIMIT_KEY = "LIMIT_KEY";
    public static final String DATE_KEY = "DATE_KEY";
    public static final String DATE_CHECKBOX_KEY = "date_checkbox";
    public static final String BOROUGH_KEY = "borough_key";
    public static final String MILES_KEY = "MILES_KEY";

    private static NyCrashPrefManager instance;
    private SharedPreferences pref;

    private NyCrashPrefManager(Context context) {
        pref = android.preference.PreferenceManager.getDefaultSharedPreferences(context);

    }

    public static NyCrashPrefManager getInstance(Context context) {
        if (instance == null) {
            instance = new NyCrashPrefManager(context);
        }
        return instance;

    }

    public String getLimit() {
        return pref.getString(LIMIT_KEY, "15");
    }

    public boolean isDateChecked() {
        return pref.getBoolean(DATE_CHECKBOX_KEY, false);
    }

    public String getDate() {
        return pref.getString(DATE_KEY, "2012-07-01");
    }

    public String getBorough() {
        return pref.getString(BOROUGH_KEY, "All");
    }
// consider returning this as an int here or changing  how it is saved in the fragment to  save it as an int
    public String getMiles() {
        return pref.getString(MILES_KEY, "1");
    }

}
