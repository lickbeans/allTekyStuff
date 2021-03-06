package com.aaroncampbell.budget.Network;

import com.aaroncampbell.budget.BudgetApplication;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by aaroncampbell on 10/31/16.
 */

public class RestClient {
    private ApiService apiService;

    public RestClient() { //Use a builder to set Date format and convert to String
        GsonBuilder builder = new GsonBuilder();
        builder.setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        Gson gson = builder.create();

        // Prints to log whatever your response
        HttpLoggingInterceptor log = new HttpLoggingInterceptor();
        log.setLevel(HttpLoggingInterceptor.Level.BODY);

        // Doing basic work of sending information, then retrieving information in under 10 seconds
        OkHttpClient okHttpClient = new OkHttpClient.Builder() // Design a webclient on our end
                .connectTimeout(10, TimeUnit.SECONDS)
                .addInterceptor(new SessionRequestInterceptor()) // If I have a token, add as authorization header
                .addInterceptor(log).build();

        // Every URL we call will have the base URL included
        Retrofit restAdapter = new Retrofit.Builder()
                .baseUrl(BudgetApplication.API_BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson)) //Converts JSON to classes
                .build();

        //All GETS and POSTS are defined in the ApiService class
        apiService = restAdapter.create(ApiService.class);
    }

    public ApiService getApiService() {
        return apiService;
    }
}
