package com.Hayse.go4lunch.ui.viewmodel;

import android.app.Application;
import android.location.Location;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.Hayse.go4lunch.MainApplication;
import com.Hayse.go4lunch.R;
import com.Hayse.go4lunch.domain.entites.map_api.nerbysearch.Result;
import com.Hayse.go4lunch.services.google_map.LocationRepository;
import com.Hayse.go4lunch.services.google_map.google_api.RestaurantRepository;
import com.Hayse.go4lunch.services.permission_checker.PermissionChecker;
import com.Hayse.go4lunch.ui.view_state.MapViewState;

import java.util.ArrayList;
import java.util.List;

public class MapViewModel extends ViewModel {
    @NonNull
    private final LocationRepository locationRepository;
    @NonNull
    private final RestaurantRepository restaurantRepository;
    private final LiveData<Location> currentLocationLiveData;

    private final LiveData<MapViewState> mapViewStateLiveData;
    public MapViewModel(
            @NonNull PermissionChecker permissionChecker,
            @NonNull LocationRepository locationRepository,
            @NonNull RestaurantRepository restaurantRepository
    ){
        this.locationRepository = locationRepository;
        this.currentLocationLiveData = locationRepository.getLocationLiveData();
        this.restaurantRepository = restaurantRepository;



        String key = MainApplication.getApplication().getApplicationContext().getResources().getString(R.string.MAPS_API_KEY);
        //if the LiveData that contains the current user location information change
        this.mapViewStateLiveData  = Transformations.switchMap(currentLocationLiveData, currentLocation ->
                // query the repository to get the user location (with a Transformations.switchMap)
                Transformations.map(restaurantRepository.getNearbyPlaces(currentLocation, 5000, "restaurant"), restaurants ->
                        mapDataToViewState(restaurants.getResults(), currentLocation)
                )
        );
    }

    private MapViewState mapDataToViewState(@Nullable List<Result> restaurants, Location currentLocation) {
        List<Result> restaurantToBeDisplayed = new ArrayList<>();

        if (restaurants != null){
            restaurantToBeDisplayed.addAll(restaurants);
        }

        return new MapViewState(restaurantToBeDisplayed, currentLocation);
    }
    public LiveData<MapViewState> getRestaurantMapViewStateLiveData(){
        return mapViewStateLiveData;
    }

}
