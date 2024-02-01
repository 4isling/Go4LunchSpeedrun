package com.Hayse.go4lunch.services.google_map.google_api;

import android.location.Location;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;


import com.Hayse.go4lunch.MainApplication;
import com.Hayse.go4lunch.R;
import com.Hayse.go4lunch.domain.entites.map_api.detail.DetailResponse;
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

public class RestaurantRepository {
    private static final String TAG = "RestaurantRepository: ";
    private final GMapsApi gMapsApi;
    private final Map<Location, RestaurantResult> alreadyFetchedResponses = new HashMap<>();
    private final MutableLiveData<RestaurantResult> resultMutableLiveData = new MutableLiveData<>();
    ;

    private final String API_KEY = MainApplication.getApplication().getApplicationContext().getResources().getString(R.string.MAPS_API_KEY);
    private Observable<List<Result>> observableResult;

    private List<Result> results;
    private MutableLiveData<com.Hayse.go4lunch.domain.entites.map_api.detail.Result> detailResult;
    private final Map<String, RestaurantResult> cache = new HashMap<>(2000);
    private String sUserLocation;

    /**
     * TODO CallNearbyPlaces recup et stocker le result et getRestaurents return resultMutableLiveData
     *
     * @param gMapsApi
     */
    public RestaurantRepository(GMapsApi gMapsApi) {
        this.gMapsApi = gMapsApi;
    }

    public void setUserLocation(String userLocation) {
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
        if (location != null) {
            Log.d(TAG, "getRestaurantLiveData: location !=null");
            String sUserLocation = String.valueOf(location.getLatitude() + "," + location.getLongitude());

            RestaurantResult restaurantResult = alreadyFetchedResponses.get(location);

            if (restaurantResult != null) {
                restaurantMutableLiveData.setValue(restaurantResult.getResults());
            } else {
                gMapsApi.getListOfRestaurants(sUserLocation, 5000, "restaurant", API_KEY)
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



    public LiveData<RestaurantResult> getRestaurantListLiveData(String type, String location, String radius) {
        MutableLiveData<RestaurantResult> RestaurantResultMutableLiveData = new MutableLiveData<>();

        RestaurantResult nearbySearchResults = cache.get(location);
        if (nearbySearchResults != null) {
            RestaurantResultMutableLiveData.setValue(nearbySearchResults);
        } else {
            gMapsApi.getNearBySearchResult(API_KEY, type, location, radius).enqueue(
                    new Callback<RestaurantResult>() {
                        @Override
                        public void onResponse(@NonNull Call<RestaurantResult> call, @NonNull Response<RestaurantResult> response) {
                            if (response.body() != null) {
                                cache.put(location, response.body());
                                RestaurantResultMutableLiveData.setValue(response.body());
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<RestaurantResult> call, @NonNull Throwable t) {
                            t.printStackTrace();
                        }
                    });

        }
        return RestaurantResultMutableLiveData;
    }

    public List<Result> getRestaurantList(Location location) {

        if (location != null) {
            Log.d(TAG, "getRestaurantLiveData: location !=null");
            String sUserLocation = String.valueOf(location.getLatitude() + "," + location.getLongitude());

            RestaurantResult restaurantResult = alreadyFetchedResponses.get(location);

            if (restaurantResult != null) {
                results = restaurantResult.getResults();
            } else {
                gMapsApi.getListOfRestaurants(sUserLocation, 5000, "Restaurants", API_KEY)
                        .enqueue(new Callback<RestaurantResult>() {
                            @Override
                            public void onResponse(Call<RestaurantResult> call, Response<RestaurantResult> response) {
                                if (response.body() != null) {
                                    alreadyFetchedResponses.put(location, response.body());
                                    results = response.body().getResults();
                                }
                            }

                            @Override
                            public void onFailure(Call<RestaurantResult> call, Throwable t) {
                                results = null;
                                Log.e(TAG, "onFailure: " + t);
                            }
                        });
            }
        }
        return results;
    }

    public LiveData<com.Hayse.go4lunch.domain.entites.map_api.detail.Result> getDetail(String place_id){
        gMapsApi.getPlaceDetails(place_id,API_KEY).enqueue(new Callback<DetailResponse>() {
            @Override
            public void onResponse(Call<DetailResponse> call, Response<DetailResponse> response) {
                Log.d(TAG, "onResponse: "+response.body().getResult());
                detailResult.setValue(response.body().getResult());
            }

            @Override
            public void onFailure(Call<DetailResponse> call, Throwable t) {
                detailResult = null;
                Log.e(TAG, "onFailure: ", t);
            }
        });
        return detailResult;
    }

    public RestaurantResult getAlreadyFetchedResponses(){
        return alreadyFetchedResponses.get(sUserLocation);
    }

}