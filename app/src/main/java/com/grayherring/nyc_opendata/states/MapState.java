package com.grayherring.nyc_opendata.states;

import android.widget.Toast;

import com.grayherring.nyc_opendata.R;
import com.grayherring.nyc_opendata.activities.NYCMapActivity;
import com.grayherring.nyc_opendata.models.CollisionModel;
import com.grayherring.nyc_opendata.networking.OpenNYRetroFitManager;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by David on 4/4/2015.
 */
public abstract class MapState {

    NYCMapActivity mNycMap;

    protected MapState(NYCMapActivity nycMap) {
        this.mNycMap = nycMap;
    }

    public abstract void updateUi();

    public abstract void queriedSearchClicked();

    public void start() {
        updateUi();
        search();
    }

    public void search() {
        runQuery(finishQuery(getBasicQuaryMap()));
    }

    protected abstract HashMap<String, String> finishQuery(HashMap<String, String> quaryMap);

    protected HashMap getBasicQuaryMap() {
        mNycMap.mProgressDialog.show();
        HashMap<String, String> quaryMap = new HashMap<>();
        quaryMap.put("$limit", "" + mNycMap.mLimit);
        quaryMap.put("$offset", "" + mNycMap.mCurrentOffSet);
        quaryMap.put("$order", "date+DESC");
        return quaryMap;
    }

    protected void runQuery(HashMap<String, String> quaryMap) {

        OpenNYRetroFitManager.getInstince().makeQuary(quaryMap, new Callback<ArrayList<CollisionModel>>() {
            @Override
            public void success(ArrayList<CollisionModel> collisionModels, Response response) {
                mNycMap.mCollisions = collisionModels;
                mNycMap.setUpMap();
                if(collisionModels.size() ==0){
                    if(mNycMap.mCurrentOffSet >0){
                        if(mNycMap.mCurrentOffSet >mNycMap.mLimit ) {
                            mNycMap.mCurrentOffSet -= mNycMap.mLimit;
                        }
                        else {
                            mNycMap.mCurrentOffSet = 0;
                        }
                    }
                }
            }

            @Override
            public void failure(RetrofitError error) {
                mNycMap.mProgressDialog.dismiss();

                Toast.makeText(mNycMap, R.string.error, Toast.LENGTH_LONG).show();
            }
        });
    }
}
