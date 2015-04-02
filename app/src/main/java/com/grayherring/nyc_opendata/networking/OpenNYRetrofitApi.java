package com.grayherring.nyc_opendata.networking;

import com.grayherring.nyc_opendata.models.CollisionModel;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.http.GET;

/**
 * Created by David on 4/1/2015.
 */
public interface OpenNYRetrofitApi {

    @GET(OpenNYRetroFitManager.BASE_URL)
    public void getAll(Callback<ArrayList<CollisionModel>> response);

}
