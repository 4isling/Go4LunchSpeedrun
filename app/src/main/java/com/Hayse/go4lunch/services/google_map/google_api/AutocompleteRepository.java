package com.Hayse.go4lunch.services.google_map.google_api;

import static android.content.ContentValues.TAG;

import android.app.Application;
import android.location.Location;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.Hayse.go4lunch.R;
import com.Hayse.go4lunch.domain.entites.map_api.autocomplete.AutocompleteResponse;
import com.Hayse.go4lunch.domain.entites.map_api.autocomplete.Prediction;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AutocompleteRepository {
    private final GMapsApi gMapsApi;
    private final Application application;
    private final String API_KEY;

    public AutocompleteRepository(GMapsApi gMapsApi, Application application){
        this.gMapsApi = gMapsApi;
        this.application = application;
        API_KEY = application.getApplicationContext().getResources().getString(R.string.MAPS_API_KEY);
    }

    public LiveData<List<Prediction>> getPredictionLiveData(Location location,String input){
        MutableLiveData<List<Prediction>> listPredictionMutableLiveData = new MutableLiveData<>();
        if (location !=null && input != null){
            String sUserLocation = String.valueOf(location.getLatitude()+","+location.getLongitude());
            gMapsApi.getAutocompleteResult(sUserLocation, application.getString(R.string.type_gmap_query), 5000, "fr", input, API_KEY)
                    .enqueue(new Callback<AutocompleteResponse>() {
                        @Override
                        public void onResponse(Call<AutocompleteResponse> call, Response<AutocompleteResponse> response) {
                            if (response.body() != null){
                                listPredictionMutableLiveData.setValue(response.body().getPredictions());
                            }
                        }
                        @Override
                        public void onFailure(Call call, Throwable t) {
                            listPredictionMutableLiveData.setValue(new ArrayList<>());
                            Log.d(TAG, "onFailure: ", t);
                        }
                    }
            );
        }
        return listPredictionMutableLiveData;
    }

}
