package com.Hayse.go4lunch.services.google_map;

import com.Hayse.go4lunch.services.google_map.google_api.GMapsApi;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitService {
    private static final Gson gson = new GsonBuilder().setLenient().create();
    private static final HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
    private static final OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

    private static final String BASE_URL = "https://maps.googleapis.com/maps/api/place/";
    private static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build();

    public static GMapsApi getGMapsApi(){
        return retrofit.create(GMapsApi.class);
    }
}