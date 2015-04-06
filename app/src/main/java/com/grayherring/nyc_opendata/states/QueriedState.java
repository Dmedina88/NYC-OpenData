package com.grayherring.nyc_opendata.states;

import com.grayherring.nyc_opendata.R;
import com.grayherring.nyc_opendata.activities.NYCMapActivity;
import com.grayherring.nyc_opendata.models.CollisionModel;
import com.grayherring.nyc_opendata.util.NyCrashPrefManager;

import java.util.HashMap;

/**
 * Created by David on 4/5/2015.
 */
public class QueriedState extends MapState {

    public QueriedState(NYCMapActivity nycMap) {
        super(nycMap);
    }

    @Override
    public void updateUi() {
        mNycMap.mMenu.findItem(R.id.search).setTitle("Stop Search");
    }

    @Override
    public void queriedSearchClicked() {
        mNycMap.mapState = mNycMap.mNormalState;
        mNycMap.mCurrentOffSet = 0;
        mNycMap.mapState.start();
    }


    @Override
    protected HashMap<String, String> finishQuery(HashMap<String, String> quaryMap) {
        NyCrashPrefManager nyCrashPrefManager = NyCrashPrefManager.getInstance(mNycMap);
        String where = "latitude+IS+NOT+NULL";
        if (nyCrashPrefManager.isDateChecked()) {
            where = where + "&" + "date=" + nyCrashPrefManager.getDate() + CollisionModel.USELESS_DATE_STRING;
        }
        String borough = nyCrashPrefManager.GetBorught();
        if (!borough.equals("All")) {
            where = where + "&" + "borough=" + borough.replace(" ", "+");

        }

        quaryMap.put("$where", where);
        return quaryMap;
    }

}
