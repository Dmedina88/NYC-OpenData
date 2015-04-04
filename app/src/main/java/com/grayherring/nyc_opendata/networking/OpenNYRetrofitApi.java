package com.grayherring.nyc_opendata.networking;

import com.grayherring.nyc_opendata.models.CollisionModel;

import java.util.ArrayList;
import java.util.Map;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.QueryMap;

/**
 * Created by David on 4/1/2015.
 */
public interface OpenNYRetrofitApi {

    @Headers(OpenNYRetroFitManager.APP_TOKEN)
    @GET(OpenNYRetroFitManager.BASE_URL)
    public void getAll(Callback<ArrayList<CollisionModel>> response);

    @Headers(OpenNYRetroFitManager.APP_TOKEN)
    @GET(OpenNYRetroFitManager.BASE_URL)
    public void makeQuery(@QueryMap Map<String, String> options,Callback<ArrayList<CollisionModel>> response);

}
