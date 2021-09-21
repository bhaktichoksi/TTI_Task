package com.task.listingtask.retrofit;

import com.task.listingtask.BuildConfig;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AppUrl {

    public static final String URL_BASE = "https://www.reddit.com";
    public static final String URL_FOODDATA = URL_BASE + "/r/food.json";
    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        if (retrofit == null) {

            retrofit = new Retrofit.Builder()
                    .baseUrl(URL_BASE)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }


}
