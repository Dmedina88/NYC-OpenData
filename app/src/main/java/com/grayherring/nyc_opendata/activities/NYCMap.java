package com.grayherring.nyc_opendata.activities;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.grayherring.nyc_opendata.R;
import com.grayherring.nyc_opendata.models.CollisionModel;
import com.grayherring.nyc_opendata.networking.OpenNYRetroFitManager;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class NYCMap extends ActionBarActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private int mCurrentOffSet =0;
    private ArrayList<Marker> mMarkers;
    private ArrayList<CollisionModel> mCollisions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nycmap);
        mCollisions = new ArrayList<CollisionModel>();

        setUpMapIfNeeded();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null ) {
                mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {

                        Log.d("test","on click ");
                        int index =  mMarkers.indexOf(marker);
                        if (index>0){
                            CollisionModel  collisionModel = mCollisions.get(index);
                            new AlertDialog.Builder(NYCMap.this).setTitle("Crash Report").setMessage(collisionModel.report()).show();
                        }

                        return false;
                    }
                });

                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {

        mMarkers= new ArrayList<Marker>();
        for(CollisionModel collisionModel : mCollisions){
            mMarkers.add(  mMap.addMarker(new MarkerOptions().position(new LatLng(collisionModel.latitude, collisionModel.longitude)).title(collisionModel.toString())));
        }


    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.map_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_dothing) {
            testapicall();
            return true;
        }

    
        return super.onOptionsItemSelected(item);
    }

    private void testapicall() {
        mCollisions = new ArrayList<CollisionModel>();
        HashMap<String,String> quaryMap = new HashMap<String,String>();
        quaryMap.put("$limit","10");
        quaryMap.put("$offset",""+mCurrentOffSet);
        quaryMap.put("$order","date DESC");
        quaryMap.put("$where","latitude IS NOT NULL");


        /*
        OpenNYRetroFitManager.getInstince().getall(new Callback<ArrayList<CollisionModel>>() {
            @Override
            public void success(ArrayList<CollisionModel> collisionModels, Response response) {

                for(int i =0;i<100;i++){
                    Log.d("TEST",collisionModels.get(i).uniqueKey + " " + collisionModels.get(i).date);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d("TEST",error.getUrl());
            }
        });
*/

        OpenNYRetroFitManager.getInstince().makeQuary(quaryMap, new Callback<ArrayList<CollisionModel>>() {
            @Override
            public void success(ArrayList<CollisionModel> collisionModels, Response response) {
                mCollisions = collisionModels;
                for (CollisionModel collisionModel : collisionModels) {
                    Log.d("TEST", collisionModel.uniqueKey + " " + collisionModel.latitude);
                }
                setUpMap();
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d("TEST", error.getUrl());
            }
        });
    }


}
