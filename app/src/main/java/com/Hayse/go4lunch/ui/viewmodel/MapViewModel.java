package com.Hayse.go4lunch.ui.viewmodel;

import android.annotation.SuppressLint;
import android.location.Location;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.Hayse.go4lunch.domain.entites.map_api.nerbysearch.Result;
import com.Hayse.go4lunch.services.google_map.LocationRepository;
import com.Hayse.go4lunch.services.google_map.google_api.RestaurantRepository;
import com.Hayse.go4lunch.services.permission_checker.PermissionChecker;
import com.Hayse.go4lunch.ui.view_state.MapViewState;

import java.util.ArrayList;
import java.util.List;

public class MapViewModel extends ViewModel {
    private LiveData<MapViewState> mapViewStateLiveData;
    private LocationRepository locationRepository;

    private PermissionChecker permissionChecker;

    @SuppressLint("MissingPermission")
    public MapViewModel(
            @NonNull PermissionChecker permissionChecker,
            @NonNull LocationRepository locationRepository,
            @NonNull RestaurantRepository restaurantRepository
    ) {
        this.locationRepository = locationRepository;
        this.permissionChecker = permissionChecker;
        if (this.permissionChecker.hasLocationPermission()) {
            this.locationRepository.startLocationRequest();
            LiveData<Location> userLocation = this.locationRepository.getLastLocation();
            Log.d("MapViewModel", "MapViewModel: userLocation = " + userLocation.getValue());
            //if the LiveData that contains the current user location information change
            this.mapViewStateLiveData = Transformations.switchMap(userLocation, currentLocation -> {
                        // query the repository to get the user location (with a Transformations.switchMap)
                        if (currentLocation != null)
                            Transformations.map(restaurantRepository.getRestaurantLiveData(currentLocation), restaurants ->
                                        mapDataToViewState(restaurants, currentLocation)
                            );
                        return null;
                    }
            );

        }

    }

    private MapViewState mapDataToViewState(@Nullable List<Result> restaurants, Location currentLocation) {
        List<Result> restaurantToBeDisplayed = new ArrayList<>();

        if (restaurants != null) {
            restaurantToBeDisplayed.addAll(restaurants);
        }

        return new MapViewState(restaurantToBeDisplayed, currentLocation);
    }

    public LiveData<MapViewState> getRestaurantMapViewStateLiveData() {
        return mapViewStateLiveData;
    }

    @SuppressLint("MissingPermission")
    public void refresh() {
        if (this.permissionChecker.hasLocationPermission()) {
            locationRepository.startLocationRequest();

        } else {
            locationRepository.stopLocationRequest();
        }
    }

    @SuppressLint("MissingPermission")
    public Location getLastKnowLocation() {
        return locationRepository.getLastLocation().getValue();
    }
}
