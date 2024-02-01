package com.Hayse.go4lunch.ui.viewmodel;

import android.annotation.SuppressLint;
import android.location.Location;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.Hayse.go4lunch.domain.entites.map_api.nerbysearch.Result;
import com.Hayse.go4lunch.domain.usecases.GetNearBySearchResultUseCase;
import com.Hayse.go4lunch.services.google_map.GoogleMapStreams;
import com.Hayse.go4lunch.services.google_map.LocationRepository;
import com.Hayse.go4lunch.services.google_map.google_api.RestaurantRepository;
import com.Hayse.go4lunch.services.permission_checker.PermissionChecker;

import java.util.List;

public class HomeRestaurantSharedViewModel extends ViewModel {
    private final static String TAG = "MapViewModel";

    private LocationRepository locationRepository;
    private RestaurantRepository restaurantRepository;
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
            @NonNull RestaurantRepository restaurantRepository
    ) {
        this.locationRepository = locationRepository;
        this.restaurantRepository = restaurantRepository;

        locationRepository.startLocationRequest();
    }


    public LiveData<Location> getLocationMutableLiveData(){
        return locationRepository.getLocationLiveData();
    }

    public LiveData<List<Result>> getRestaurant(Location location){
        return restaurantRepository.getRestaurantLiveData(location);
    }
}