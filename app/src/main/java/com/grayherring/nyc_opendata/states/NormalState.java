package com.grayherring.nyc_opendata.states;

import com.grayherring.nyc_opendata.R;
import com.grayherring.nyc_opendata.activities.NYCMapActivity;

import java.util.HashMap;

/**
 * Created by David on 4/5/2015.
 */
public class NormalState extends MapState {


    public NormalState(NYCMapActivity nycMap) {
        super(nycMap);
    }


    @Override
    public void updateUi() {
        if (mNycMap.mMenu != null) {

            mNycMap.mMenu.findItem(R.id.search).setTitle(mNycMap.getResources().getString(R.string.search));
        }

    }

    @Override
    public void queriedSearchClicked() {
        mNycMap.mCurrentOffSet = 0;
        mNycMap.mapState = mNycMap.mQueriedState;
        mNycMap.mapState.start();
    }

    @Override
    protected HashMap<String, String> finishQuery(HashMap<String, String> quaryMap) {
        quaryMap.put("$where", "latitude+IS+NOT+NULL");
        return quaryMap;
    }

}
