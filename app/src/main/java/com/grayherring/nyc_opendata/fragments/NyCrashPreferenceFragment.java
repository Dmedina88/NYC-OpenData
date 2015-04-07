package com.grayherring.nyc_opendata.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceGroup;
import android.text.method.DigitsKeyListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.grayherring.nyc_opendata.R;
import com.grayherring.nyc_opendata.activities.NYCMapActivity;
import com.grayherring.nyc_opendata.ui.DatePickerPreference;
import com.grayherring.nyc_opendata.util.NyCrashPrefManager;

public class NyCrashPreferenceFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener,
        SharedPreferences.OnSharedPreferenceChangeListener {


    Activity mActivity;

    public NyCrashPreferenceFragment() {
        // Required empty public constructor
    }

    public static NyCrashPreferenceFragment newInstance() {
        NyCrashPreferenceFragment fragment = new NyCrashPreferenceFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_general);

        EditTextPreference limitPref = (EditTextPreference) findPreference(NyCrashPrefManager.LIMIT_KEY);
        limitPref.setOnPreferenceChangeListener(this);
        EditText limitEditText = limitPref.getEditText();
        limitEditText.setKeyListener(DigitsKeyListener.getInstance());
        initSummaries(getPreferenceScreen());
        this.getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_ny_crash_preference, container, false);
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
    }



    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (preference instanceof EditTextPreference) {
            if (preference.getKey().equals(NyCrashPrefManager.LIMIT_KEY)) {
                String limit = ((EditTextPreference) preference).getEditText().getText().toString();
                int intLimit = Integer.parseInt(limit);
                if (limit.isEmpty() || limit.equals("0")||intLimit  >100) {
                  new  AlertDialog.Builder(mActivity).setMessage(mActivity.getString(R.string.limit_alert)).setTitle(mActivity.getString(R.string.alert)).setNeutralButton(mActivity.getString(R.string.ok),null).show();
                    return false;
                }
            }

        }
        return true;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Intent returnIntent = new Intent();
        returnIntent.putExtra(NYCMapActivity.RESULT_KEY, true);
        mActivity.setResult(Activity.RESULT_OK, returnIntent);
        Preference pref = findPreference(key);
        setSummary(pref);
    }

    private void initSummaries(PreferenceGroup pg) {
        for (int i = 0; i < pg.getPreferenceCount(); ++i) {
            Preference p = pg.getPreference(i);
            if (p instanceof PreferenceGroup)
                this.initSummaries((PreferenceGroup) p);
            else
                this.setSummary(p);
        }
    }


    private void setSummary(Preference pref) {

        if (pref instanceof EditTextPreference) {
            EditTextPreference editTextPref = (EditTextPreference) pref;
            pref.setSummary(editTextPref.getText());
            return;
        }


        if (pref instanceof ListPreference) {
            ListPreference listPreference = (ListPreference) pref;
            pref.setSummary(listPreference.getEntry());
            return;
        }
        if (pref instanceof DatePickerPreference) {
            // DatePickerPreference listPreference  = (DatePickerPreference) pref;
            pref.setSummary(NyCrashPrefManager.getInstance(getActivity()).getDate());
            return;
        }

    }


}
