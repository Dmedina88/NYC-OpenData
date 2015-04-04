package com.grayherring.nyc_opendata.networking;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.grayherring.nyc_opendata.models.CollisionModel;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

/**
 * Created by David on 4/1/2015.
 */
public class OpenNYRetroFitManager {

    static final String BASE_URL = "/h9gi-nx95.json";
    static final String END_POINT = "https://data.cityofnewyork.us/resource";
    static final String APP_TOKEN = "X-App-Token: gsMJoOgPCopqmuS0jtqvlXGdC";

    private OpenNYRetrofitApi mSwagRetrofit;
    private static  OpenNYRetroFitManager  mOpenNYRetroFitManager;
    //  public static final String URL_FIELD = "url";


    private OpenNYRetroFitManager() {
        Gson gson = new GsonBuilder()
                .setDateFormat("MM-dd-yyyy")
                .create();

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(END_POINT).setConverter(new GsonConverter(gson))
                .build();
        mSwagRetrofit = restAdapter.create(OpenNYRetrofitApi.class);
    }

    public static OpenNYRetroFitManager getInstince(){

        if(mOpenNYRetroFitManager == null){
            mOpenNYRetroFitManager = new OpenNYRetroFitManager();
        }

        return mOpenNYRetroFitManager;
    }

    public void getall(Callback<ArrayList<CollisionModel>> callback){
        mSwagRetrofit.getAll(callback);

    }

    public void makeQuary(HashMap<String, String> quaryMap , Callback<ArrayList<CollisionModel>> callback){
        mSwagRetrofit.makeQuery(quaryMap ,callback);

    }
    //public String QueryBuilder(){};

}