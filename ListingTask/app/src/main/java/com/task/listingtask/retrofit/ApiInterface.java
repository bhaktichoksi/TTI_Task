package com.task.listingtask.retrofit;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Url;

public interface ApiInterface {

    @GET(AppUrl.URL_FOODDATA)
    Call<JsonObject> callApiGetFood();

    @GET
    Call<JsonArray> callApiGetComment(@Url String url);

}
