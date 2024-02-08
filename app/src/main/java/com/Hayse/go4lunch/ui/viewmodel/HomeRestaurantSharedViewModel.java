package com.Hayse.go4lunch.ui.viewmodel;

import android.annotation.SuppressLint;
import android.location.Location;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.Hayse.go4lunch.domain.entites.map_api.nerbysearch.Result;
import com.Hayse.go4lunch.domain.usecases.GetNearBySearchResultUseCase;
import com.Hayse.go4lunch.services.google_map.LocationRepository;
import com.Hayse.go4lunch.services.google_map.google_api.NearBySearchRepository;
import com.Hayse.go4lunch.services.permission_checker.PermissionChecker;

import java.util.List;

public class HomeRestaurantSharedViewModel extends ViewModel {
    private final static String TAG = "MapViewModel";

    private LocationRepository locationRepository;
    private NearBySearchRepository nearBySearchRepository;
    private PermissionChecker permissionChecker;
    private GetNearBySearchResultUseCase getNearBySearchResultUseCase;
    private final Location defaultLocation = new Location("48.888053, 2.343312");
    private final MutableLiveData<Boolean> hasGpsPermissionLiveData = new MutableLiveData<>();

    ////////////////////////////////////////////////////
    private LiveData<List<Result>> resultLiveData = new MutableLiveData<>(null);

    private  LiveData<com.Hayse.go4lunch.domain.entites.map_api.detail.Result> detailResult;




    @SuppressLint("MissingPermission")
    public HomeRestaurantSharedViewModel(
            @NonNull PermissionChecker permissionChecker,
            @NonNull LocationRepository locationRepository,
            @NonNull NearBySearchRepository nearBySearchRepository
    ) {
        this.locationRepository = locationRepository;
        this.nearBySearchRepository = nearBySearchRepository;

        locationRepository.startLocationRequest();
    }


    public LiveData<Location> getLocationMutableLiveData(){
        return locationRepository.getLocationLiveData();
    }

    public LiveData<List<Result>> getRestaurant(Location location){
        return nearBySearchRepository.getRestaurantLiveData(location);
    }
}