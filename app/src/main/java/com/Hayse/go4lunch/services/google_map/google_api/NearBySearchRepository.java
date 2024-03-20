package com.Hayse.go4lunch.services.google_map.google_api;

import android.location.Location;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;


import com.Hayse.go4lunch.MainApplication;
import com.Hayse.go4lunch.R;
import com.Hayse.go4lunch.domain.entites.map_api.nerbysearch.RestaurantResult;
import com.Hayse.go4lunch.domain.entites.map_api.nerbysearch.Result;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NearBySearchRepository {
    private static final String TAG = "NearBySearchRep: ";
    private final GMapsApi gMapsApi;
    private final Map<Location, RestaurantResult> alreadyFetchedResponses = new HashMap<>();
    private final String API_KEY = MainApplication.getApplication().getApplicationContext().getResources().getString(R.string.MAPS_API_KEY);
    MutableLiveData<List<Result>> restaurantMutableLiveData = new MutableLiveData<>();
    private final Map<String, RestaurantResult> cache = new HashMap<>(2000);

    public NearBySearchRepository(GMapsApi gMapsApi) {
        this.gMapsApi = gMapsApi;
    }


    public LiveData<List<Result>> getRestaurantLiveData(Location location) {

        if (location != null) {
            Log.d(TAG, "getRestaurantLiveData: location !=null");
            String sUserLocation = String.valueOf(location.getLatitude() + "," + location.getLongitude());

            RestaurantResult restaurantResult = alreadyFetchedResponses.get(location);

            if (restaurantResult != null) {
                restaurantMutableLiveData.setValue(restaurantResult.getResults());
            } else {
                gMapsApi.getListOfRestaurants(sUserLocation, 5000, MainApplication.getApplication().getString(R.string.type_gmap_query), API_KEY)
                        .enqueue(new Callback<RestaurantResult>() {
                            @Override
                            public void onResponse(Call<RestaurantResult> call, Response<RestaurantResult> response) {
                                if (response.body() != null) {
                                    alreadyFetchedResponses.put(location, response.body());
                                    restaurantMutableLiveData.setValue(response.body().getResults());
                                }
                            }

                            @Override
                            public void onFailure(Call<RestaurantResult> call, Throwable t) {
                                restaurantMutableLiveData.setValue(null);
                                Log.e(TAG, "onFailure: " + t);
                            }
                        });
            }
        }
        return restaurantMutableLiveData;
    }
}