package com.grayherring.nyc_opendata.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.grayherring.nyc_opendata.R;
import com.grayherring.nyc_opendata.models.CollisionModel;
import com.grayherring.nyc_opendata.networking.OpenNYRetroFitManager;
import com.grayherring.nyc_opendata.ui.FloatingActionButton;
import com.grayherring.nyc_opendata.util.NyCrashPrefManager;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class NYCMap extends ActionBarActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private ArrayList<Marker> mMarkers;
    private ArrayList<CollisionModel> mCollisions;
    private int mLimit;
    private int mCurrentOffSet =0;

    private ProgressDialog mProgressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
       //ui stuff
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nycmap);
        FloatingActionButton fabButton = new FloatingActionButton.Builder(this)
                .withDrawable(getResources().getDrawable(R.drawable.ic_next))
                .withButtonColor(getResources().getColor(R.color.fob_color))
                .withGravity(Gravity.BOTTOM | Gravity.RIGHT)
                .withMargins(0, 0, 16, 16)
                .create();

        fabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                next();
            }
        });


        mProgressDialog = new ProgressDialog(this);
       // mProgressDialog.setTitle("Searching");
        mProgressDialog.setMessage(getString(R.string.gathering_crash_report));
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        //init
        mLimit = NyCrashPrefManager.getInstance(this).getLimet();
        mCurrentOffSet = 0;
        mCollisions = new ArrayList<CollisionModel>();
        //map setup
        setUpMapIfNeeded();
        search();
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
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(40.6343610, -73.9754030), 10));
                mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {

                        Log.d("test","on click ");
                        int index =  mMarkers.indexOf(marker);
                        if (index>0){
                            final CollisionModel collisionModel = mCollisions.get(index);
                            new AlertDialog.Builder(NYCMap.this).setTitle(getString(R.string.crash_report)).setMessage(collisionModel.report()).setPositiveButton(getString(R.string.share),new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent=new Intent(android.content.Intent.ACTION_SEND);
                                    intent.setType("text/plain");
                                    intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.crash_report));
                                    intent.putExtra(Intent.EXTRA_TEXT,collisionModel.report() );
                                    startActivity(Intent.createChooser(intent,getString(R.string.share) ));
                                }
                            }).show();
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

        mMap.clear();
        mMarkers= new ArrayList<Marker>();
        for(CollisionModel collisionModel : mCollisions){
            mMarkers.add(  mMap.addMarker(new MarkerOptions().position(new LatLng(collisionModel.latitude, collisionModel.longitude))));
        }
        mProgressDialog.dismiss();
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

        switch (id){

            case R.id.action_next:
                next();
                break;
            case R.id.action_previous:
                previous();
                break;
        }

    
        return super.onOptionsItemSelected(item);
    }



    private void search(){
        mProgressDialog.show();
        mCollisions = new ArrayList<CollisionModel>();
        HashMap<String,String> quaryMap = new HashMap<String,String>();
        quaryMap.put("$limit",""+mLimit);
        quaryMap.put("$offset",""+mCurrentOffSet);
        quaryMap.put("$order","date DESC");
        quaryMap.put("$where","latitude IS NOT NULL");


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
                mProgressDialog.dismiss();
                Toast.makeText(NYCMap.this,getString(R.string.error),Toast.LENGTH_LONG).show();
            }
        });
    }

    private void next(){
       mCurrentOffSet += mLimit;
        search();

    }
    private  void previous(){
        if(mCurrentOffSet >0) {
            mCurrentOffSet -= mLimit;
            search();
        }
    }


}
