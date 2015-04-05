package com.grayherring.nyc_opendata.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by David on 4/4/2015.
 */
public class NyCrashPrefManager {

    public static final String SHARED_PREF = "SHARED_PREF";
    public static final String LIMET_KEY = "LIMET_KEY";
    public static final String DATE_KEY = "DATE_KEY";
    public static final String DATE_CHECKBOX_KEY = "date_checkbox";
    public static final String BOROUGH_KEY = "borough_key";

    private static NyCrashPrefManager instance;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;


    private NyCrashPrefManager(Context context) {
        pref = android.preference.PreferenceManager.getDefaultSharedPreferences(context);

    }

    public static NyCrashPrefManager getInstance(Context context) {
        if (instance == null) {
            instance = new NyCrashPrefManager(context);
        }
        return instance;

    }

    public void startEdit() {
        editor = pref.edit();
    }

    public void stopEdit() {
        editor.commit();
        editor = null;

    }


    public String getLimet() {
        return pref.getString(LIMET_KEY, "15");
    }

    public boolean isDateChecked() {
        return pref.getBoolean(DATE_CHECKBOX_KEY, false);
    }

    public String getDate() {
        return pref.getString(DATE_KEY, "1988-09-23");
    }
    public String GetBorught() {
        return pref.getString(BOROUGH_KEY, "All");
    }


    //part of me  wants to make this take a string and turn it to int
    public void setLimet(String value) {
        editor = pref.edit();
        editor.putString(LIMET_KEY, value);
        editor.commit();
        editor = null;
    }

}
