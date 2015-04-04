package com.grayherring.nyc_opendata.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by David on 4/4/2015.
 */
public class NyCrashPrefManager {

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    public static final  String SHARED_PREF = "SHARED_PREF" ;
    public static final  String LIMET_KEY = "LIMET_KEY" ;

    private static NyCrashPrefManager instance;


    private NyCrashPrefManager(Context context) {
        pref = context.getSharedPreferences(SHARED_PREF, Activity.MODE_PRIVATE);

    }

   public static NyCrashPrefManager getInstance(Context context){
       if(instance == null){
           instance = new NyCrashPrefManager(context);
       }
       return  instance;

   }

    public void startEdit() {
        editor = pref.edit();
    }

    public void stopEdit() {
        editor.commit();
        editor = null;

    }


    public int getLimet() {

        return pref.getInt(LIMET_KEY, 15);
    }

    //part of me  wants to make this take a string and turn it to int
    public void setLimet(int value) {
        editor.putInt(LIMET_KEY, value);
    }

}
