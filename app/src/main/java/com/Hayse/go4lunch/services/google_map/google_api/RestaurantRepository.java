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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RestaurantRepository {
    private static final String TAG = "RestaurantRepository: ";
    private final GMapsApi gMapsApi;
    private final Map<Location, RestaurantResult> alreadyFetchedResponses = new HashMap<>();
    private final MutableLiveData<RestaurantResult> resultMutableLiveData = new MutableLiveData<>();
    ;

    private final String API_KEY = MainApplication.getApplication().getApplicationContext().getResources().getString(R.string.MAPS_API_KEY);
    private List<Result> results;
    private String sUserLocation;

    /**
     * TODO CallNearbyPlaces recup et stocker le result et getRestaurents return resultMutableLiveData
     *
     * @param gMapsApi
     */
    public RestaurantRepository(GMapsApi gMapsApi) {
        this.gMapsApi = gMapsApi;
    }

    public void setUserLocation(String userLocation){
        sUserLocation = userLocation;
        getNearbyPlaces();
    }

    /**
     * @return le but de la fonction est de faire appel a l'api si le resultat n'as pas deja été demander
     */
    private MutableLiveData<RestaurantResult> getNearbyPlaces() {
        Log.d(TAG, "getNearbyPlaces: key" + API_KEY + " suserLocation: " + sUserLocation);
        gMapsApi.getListOfRestaurants(sUserLocation, 5000, "restaurant", API_KEY).enqueue(new Callback<RestaurantResult>() {
            @Override
            public void onResponse(Call<RestaurantResult> call, Response<RestaurantResult> response) {
                if (response.isSuccessful()) {
                    results = response.body().getResults();
                    resultMutableLiveData.setValue(response.body());
                } else {
                    // Gérez les erreurs ici
                    Log.e("restaurant repo", "there is an error in getNearbyPlaces");
                }
            }

            @Override
            public void onFailure(Call<RestaurantResult> call, Throwable t) {
                // Gérez les erreurs ici
                Log.e("restaurant repo", "there is an error in getNearbyPlaces 2");
            }
        });
        return resultMutableLiveData;
    }


    public LiveData<List<Result>> getRestaurantLiveData(Location location) {
        MutableLiveData<List<Result>> restaurantMutableLiveData = new MutableLiveData<>();
        String sUserLocation = String.valueOf(location.getLatitude()+","+location.getLongitude());

        RestaurantResult restaurantResult = alreadyFetchedResponses.get(location);

        if (restaurantResult != null) {
            restaurantMutableLiveData.setValue(restaurantResult.getResults());
        } else {
            gMapsApi.getListOfRestaurants(sUserLocation, 5000, "Restaurants", API_KEY)
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
                            Log.e(TAG, "onFailure: " + t );
                        }
                    });
        }
        return restaurantMutableLiveData;
    }


}