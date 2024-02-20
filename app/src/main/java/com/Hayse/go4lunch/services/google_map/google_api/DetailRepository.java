package com.Hayse.go4lunch.services.google_map.google_api;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.Hayse.go4lunch.MainApplication;
import com.Hayse.go4lunch.R;
import com.Hayse.go4lunch.domain.entites.map_api.detail.DetailResponse;
import com.Hayse.go4lunch.domain.entites.map_api.detail.Result;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailRepository {
    private static final String TAG = "DetailRepository: ";

    private final GMapsApi gMapsApi;
    private final String API_KEY = MainApplication.getApplication().getApplicationContext().getResources().getString(R.string.MAPS_API_KEY);
    private final String FIELDS = MainApplication.getApplication().getString(R.string.place_details_fields);
    private MutableLiveData<Result> detailResult = new MutableLiveData<>();
    public DetailRepository(GMapsApi gMapsApi) {
        this.gMapsApi = gMapsApi;
    }

    public LiveData<Result> getDetail(String place_id){
        Log.d(TAG, "getDetail: "+place_id);
        gMapsApi.getPlaceDetails(place_id,FIELDS,API_KEY).enqueue(new Callback<DetailResponse>() {
            @Override
            public void onResponse(Call<DetailResponse> call, Response<DetailResponse> response) {
                if (response.isSuccessful()){
                    if(response.body() != null){
                        detailResult.setValue(response.body().getResult());
                    }
                }else {
                    Log.e(TAG, "onResponse: error in apicall");
                }
                Log.d(TAG, "onResponse: "+response.body().getResult());
            }

            @Override
            public void onFailure(Call<DetailResponse> call, Throwable t) {
                detailResult.setValue(null);
                Log.e(TAG, "onFailure: error detail call ", t);
            }
        });
        return detailResult;
    }
}
