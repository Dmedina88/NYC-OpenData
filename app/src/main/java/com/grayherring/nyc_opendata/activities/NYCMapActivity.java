package com.grayherring.nyc_opendata.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.grayherring.nyc_opendata.R;
import com.grayherring.nyc_opendata.models.CollisionModel;
import com.grayherring.nyc_opendata.states.MapState;
import com.grayherring.nyc_opendata.states.NormalState;
import com.grayherring.nyc_opendata.states.QueriedState;
import com.grayherring.nyc_opendata.ui.FloatingActionButton;
import com.grayherring.nyc_opendata.util.NyCrashPrefManager;

import java.util.ArrayList;

public class NYCMapActivity extends Activity {

    public static final String RESULT_KEY = "RESULT" ;
    private static final String OFFSET_KEY = "OFFSET_KEY" ;
    private static final String COLLISION_MODEL_KEY = "COLLISION_MODEL_KEY" ;
    private static final String STATE_KEY = "STATE_KEY";
    public GoogleMap mMap; // Might be null if Google Play services APK is not available.
    public ArrayList<Marker> mMarkers;
    public ArrayList<CollisionModel> mCollisions;
    public int mLimit;
    public int mCurrentOffSet = 0;
    public ProgressDialog mProgressDialog;
    public Menu  mMenu;

    public MapState mapState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //ui stuff
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nycmap);
        FloatingActionButton fabButton = new FloatingActionButton.Builder(this)
                .withDrawable(getResources().getDrawable(R.drawable.ic_next))
                .withButtonColor(getResources().getColor(R.color.fob_color))
                .withGravity(Gravity.BOTTOM |  Gravity.END)
                .withMargins(0, 0, 16, 16)
                .create();
        fabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                next();
            }
        });

        mCollisions = new ArrayList<>();
        if(savedInstanceState !=null){
            mCurrentOffSet = savedInstanceState.getInt(OFFSET_KEY,0);
            mCollisions = savedInstanceState.getParcelableArrayList(COLLISION_MODEL_KEY);

            returnState(savedInstanceState.getString(STATE_KEY,NormalState.class.getSimpleName()));
        }else{
            mapState = new NormalState(this);
        }

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage(getString(R.string.gathering_crash_report));
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mLimit = Integer.parseInt(NyCrashPrefManager.getInstance(this).getLimet());

        setUpMapIfNeeded();
        if(mCollisions.size()<1){
            mapState.start();
        }


    }

    @Override
    protected void onResume() {
        super.onResume();


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
            mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {

                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(40.6343610, -73.9754030), 10));
                mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {


                        int index = mMarkers.indexOf(marker);
                        if (index > 0) {
                            final CollisionModel collisionModel = mCollisions.get(index);
                            new AlertDialog.Builder(NYCMapActivity.this).setTitle(getString(R.string.crash_report)).setMessage(collisionModel.report()).setPositiveButton(getString(R.string.share), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(android.content.Intent.ACTION_SEND);
                                    intent.setType("text/plain");
                                    intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.crash_report));
                                    intent.putExtra(Intent.EXTRA_TEXT, collisionModel.report());
                                    startActivity(Intent.createChooser(intent, getString(R.string.share)));
                                }
                            }).show();
                        }

                        return false;
                    }
                });
                // mMap.addMarker(new MarkerOptions().position(new LatLng(mMap.getMyLocation().getLatitude(),mMap.getMyLocation().getLongitude()));
                //mMap.setMyLocationEnabled(true);
                mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
                    @Override
                    public boolean onMyLocationButtonClick() {
                        double lat = mMap.getMyLocation().getLatitude();
                        double lon = mMap.getMyLocation().getLongitude();
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
    public void setUpMap() {

        mMap.clear();
        mMarkers = new ArrayList<>();
        for (CollisionModel collisionModel : mCollisions) {
            mMarkers.add(mMap.addMarker(new MarkerOptions().position(new LatLng(collisionModel.latitude, collisionModel.longitude))));
        }
        mProgressDialog.dismiss();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        mMenu = menu;
        getMenuInflater().inflate(R.menu.map_menu, menu);
        mapState.updateUi();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id) {

            case R.id.action_next:
                next();
                break;
            case R.id.action_previous:
                previous();
                break;
            case R.id.settings:
                startSettings();
                break;
            case R.id.search:
                mapState.queriedSearchClicked();
                break;

        }


        return super.onOptionsItemSelected(item);
    }


    private void next() {
        mCurrentOffSet += mLimit;
        mapState.search();

    }

    private void previous() {
        if (mCurrentOffSet > 0) {
            mCurrentOffSet -= mLimit;
            mapState.search();
        }
    }

    // I only want to check the setting  change  if this is the activity we are coming from  on every time its resumed
    private void startSettings() {
        Intent intent = new Intent(this, SettingsActivity.class);
        this.startActivityForResult(intent,0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //if limit changed in settings
        if(data!=null) {
            if (data.getBooleanExtra(RESULT_KEY, false)) {
                if (mapState instanceof QueriedState) {
                    mapState.search();
                    return;
                }
            }
        }
        int newLimit = Integer.parseInt(NyCrashPrefManager.getInstance(this).getLimet());
        if(newLimit != mLimit){
            mLimit = newLimit;
            mapState.search();
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(OFFSET_KEY,mCurrentOffSet);
        outState.putParcelableArrayList(COLLISION_MODEL_KEY,mCollisions);
        outState.putString(STATE_KEY, mapState.getClass().getSimpleName());
    }

    private void returnState(String className){

        if(className.equals(NormalState.class.getSimpleName())){
            mapState = new NormalState(this);
        }if(className.equals(QueriedState.class.getSimpleName())){
            mapState = new QueriedState(this);
        }
    }
}
