package com.grayherring.nyc_opendata.states;

import android.location.Location;
import android.widget.Toast;

import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.grayherring.nyc_opendata.R;
import com.grayherring.nyc_opendata.activities.NYCMapActivity;
import com.grayherring.nyc_opendata.models.CollisionModel;
import com.grayherring.nyc_opendata.models.MinMaxLatLon;
import com.grayherring.nyc_opendata.networking.OpenNYRetroFitManager;
import com.grayherring.nyc_opendata.util.NyCrashPrefManager;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

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
        String location = nyCrashPrefManager.getBorough();
        if (!location.equals("All")){
            if(location.equals("YOUR LOCATION")){
             Location myLocation =    mNycMap.mMap.getMyLocation();
             MinMaxLatLon minMaxLatLon = new MinMaxLatLon(myLocation.getLatitude(),myLocation.getLongitude(),Integer.valueOf(nyCrashPrefManager.getMiles()));
              where = "latitude+>=+"+minMaxLatLon.minLat +"+AND+latitude+<=+"+ minMaxLatLon.maxLat +"+AND+longitude+>=+"+minMaxLatLon.minLon +"+AND+longitude+<="+ minMaxLatLon.maxLon;
            }else{
                where = where + "&" + "borough=" + location.replace(" ", "+");
            }
        }
        if (nyCrashPrefManager.isDateChecked()) {
            where = where + "&" + "date=" + nyCrashPrefManager.getDate() + CollisionModel.USELESS_DATE_STRING;
        }
        quaryMap.put("$where", where);
        return quaryMap;
    }


    protected void runQuery(HashMap<String, String> queryMap) {

        mNycMap.lockScreen();
        OpenNYRetroFitManager.getInstince().makeQuary(queryMap, new Callback<ArrayList<CollisionModel>>() {
            @Override
            public void success(ArrayList<CollisionModel> collisionModels, Response response) {
                mNycMap.mCollisions = collisionModels;

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
                mNycMap.setUpMap();
                // part of me wants to make this a 3rd state LOCATION state
                NyCrashPrefManager nyCrashPrefManager = NyCrashPrefManager.getInstance(mNycMap);
                if( nyCrashPrefManager.getBorough().equals("YOUR LOCATION")){
                    Location myLocation =    mNycMap.mMap.getMyLocation();
                    mNycMap.mMap.addCircle(new CircleOptions().center(new LatLng(myLocation.getLatitude(),myLocation.getLongitude())).radius(Integer.valueOf(nyCrashPrefManager.getMiles())*MinMaxLatLon.METER_MILE));
                }
                mNycMap.unlockScreen();
            }

            @Override
            public void failure(RetrofitError error) {
                mNycMap.mProgressDialog.dismiss();
                Toast.makeText(mNycMap, R.string.error, Toast.LENGTH_LONG).show();
                mNycMap.unlockScreen();
            }
        });
    }



}
