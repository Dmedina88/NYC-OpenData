package com.grayherring.nyc_opendata.networking;

import retrofit.RestAdapter;

/**
 * Created by David on 4/1/2015.
 */
public class OpenNYRetroFitManager {

    static final String BASE_URL = "/something";
    static final String END_POINT = "https://data.cityofnewyork.us/resource/";
    private OpenNYRetrofitApi mSwagRetrofit;
    private static  OpenNYRetroFitManager  mOpenNYRetroFitManager;
    //  public static final String URL_FIELD = "url";


    private OpenNYRetroFitManager() {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(END_POINT)
                .build();
        mSwagRetrofit = restAdapter.create(OpenNYRetrofitApi.class);
    }

    public static OpenNYRetroFitManager getInstince(){

        if(mOpenNYRetroFitManager == null){
            mOpenNYRetroFitManager = new OpenNYRetroFitManager();
        }

        return mOpenNYRetroFitManager;
    }


}