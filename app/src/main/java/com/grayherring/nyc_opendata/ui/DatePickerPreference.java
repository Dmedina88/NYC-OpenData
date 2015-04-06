package com.grayherring.nyc_opendata.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;

import com.grayherring.nyc_opendata.R;

/**
 * Created by David on 4/4/2015.
 */
public class DatePickerPreference extends DialogPreference {
    private DatePicker mDatePicker;
    private int mDay =1;
    private int mYear =2012;
    private int mMonth =7;
    private String mDate;

    public DatePickerPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        setPositiveButtonText("Set");
        setNegativeButtonText("Cancel");

    }

    @Override
    protected View onCreateDialogView() {
        mDatePicker = new DatePicker(getContext());
        mDatePicker.setCalendarViewShown(false);

        return (mDatePicker);
    }


    @Override
    protected void onBindDialogView(View v) {
        super.onBindDialogView(v);
        //yyyy-mm-dd

        mDatePicker.updateDate(mYear, mMonth , mDay);

    }


    @Override
    protected void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);


        if (positiveResult) {
            mDay = mDatePicker.getDayOfMonth();
            mMonth = mDatePicker.getMonth();
            mYear = mDatePicker.getYear();

            int numMonth = mMonth + 1;
            String month = "";
            if (numMonth < 10) {
                month = "0" + numMonth;
            } else {
                month = "" + numMonth;
            }
            String day="";
            if (mDay < 10) {
                day = "0" + mDay;
            } else {
                day = "" + mDay;
            }
            mDate = String.format("%d-%s-%s", mYear, month, day);


            if (callChangeListener(mDate)) {
                Log.d("callChangeListener TEST", mDate);
                persistString(mDate);
            }

        }


    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return (a.getString(index));
    }

    @Override
    protected void onSetInitialValue(boolean restore, Object defaultValue) {

        if (restore) {
            if (defaultValue == null) {
                mDate = getPersistedString(getContext().getString(R.string.starting_date));
            } else {
                mDate  = getPersistedString(defaultValue.toString());
            }
        } else {
            mDate  = defaultValue.toString();
        }
            mYear = getYear(mDate);
            mDay = getDay(mDate);
            mMonth = getMonth(mDate) - 1;

        }

        // may move into some sort of util class?

    private int getYear(String date) {
        String[] pieces = date.split("-");
        return (Integer.parseInt(pieces[0]));
    }

    private int getDay(String date) {
        String[] pieces = date.split("-");
        return (Integer.parseInt(pieces[2]));
    }

    private int getMonth(String date) {
        String[] pieces = date.split("-");
        return (Integer.parseInt(pieces[1]));
    }


}
