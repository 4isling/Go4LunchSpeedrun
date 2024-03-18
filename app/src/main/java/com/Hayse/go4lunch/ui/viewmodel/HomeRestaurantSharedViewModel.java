package com.Hayse.go4lunch.ui.viewmodel;

import android.annotation.SuppressLint;
import android.location.Location;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.Hayse.go4lunch.domain.entites.Workmate;
import com.Hayse.go4lunch.domain.entites.map_api.nerbysearch.Geometry;
import com.Hayse.go4lunch.domain.entites.map_api.nerbysearch.OpeningHours;
import com.Hayse.go4lunch.domain.entites.map_api.nerbysearch.Result;
import com.Hayse.go4lunch.services.firebase.WorkmateRepository;
import com.Hayse.go4lunch.services.google_map.google_api.NearBySearchRepository;
import com.Hayse.go4lunch.services.location.LocationRepository;
import com.Hayse.go4lunch.ui.view_state.HomeViewState;
import com.Hayse.go4lunch.ui.view_state.HomeWrapperViewState;
import com.google.android.libraries.places.api.model.Place;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class HomeRestaurantSharedViewModel extends ViewModel {
    private final static String TAG = "HomeSharedViewModel";
    private final LocationRepository locationRepository;
    private final NearBySearchRepository nearBySearchRepository;
    private final WorkmateRepository workmateRepository;
    private int count = 0;
    private final MediatorLiveData<HomeWrapperViewState> homeWrapperViewStateMediatorLiveData = new MediatorLiveData<>();

    ////////////////////////////////////////////////////
    private final MutableLiveData<Place> placeAutocompleteSelected = new MutableLiveData<>(null);

    @SuppressLint("MissingPermission")
    public HomeRestaurantSharedViewModel(
            @NonNull LocationRepository locationRepository,
            @NonNull NearBySearchRepository nearBySearchRepository,
            @NonNull WorkmateRepository workmateRepository
    ) {
        this.locationRepository = locationRepository;
        this.nearBySearchRepository = nearBySearchRepository;
        this.workmateRepository = workmateRepository;
        locationRepository.startLocationRequest();
        setViewState();
    }

    @SuppressLint("MissingPermission")
    public void startLocationRequest(){
        locationRepository.startLocationRequest();
    }

    public void setViewState() {
        LiveData<List<Workmate>> workmatesLiveData = workmateRepository.getAllWorkmateRt();
        LiveData<Location> userLocationLiveData = locationRepository.getLocationLiveData();
        LiveData<List<Result>> nearbySearchListLiveData = Transformations.switchMap(locationRepository.getLocationLiveData(), nearBySearchRepository::getRestaurantLiveData);

        homeWrapperViewStateMediatorLiveData.addSource(nearbySearchListLiveData, resultList -> combine(
                resultList,
                userLocationLiveData.getValue(),
                workmatesLiveData.getValue()));

        homeWrapperViewStateMediatorLiveData.addSource(workmateRepository.getAllWorkmateRt(), workmates -> combine(
                nearbySearchListLiveData.getValue(),
                userLocationLiveData.getValue(),
                workmates));

        homeWrapperViewStateMediatorLiveData.addSource(userLocationLiveData, location -> combine(
                nearbySearchListLiveData.getValue(),
                location,
                workmatesLiveData.getValue()));
    }

    public LiveData<Location> getLocationLiveData() {
        return locationRepository.getLocationLiveData();
    }

    public LiveData<Workmate> getUserData() {
        return workmateRepository.getRealTimeUserData();
    }

    public LiveData<List<Result>> getRestaurant(Location location) {
        return nearBySearchRepository.getRestaurantLiveData(location);
    }

    public void onPredictionClick(@Nullable Place place) {
        Log.d(TAG, "onPredictionClick: " + place);
        this.placeAutocompleteSelected.setValue(place);
        this.placeAutocompleteSelected.postValue(null);
    }

    public LiveData<Place> getPrediction() {
        return placeAutocompleteSelected;
    }

    @SuppressLint("NewApi")
    private void combine(@Nullable List<Result> restaurants,
                         @Nullable Location location,
                         @Nullable List<Workmate> workmates
    ) {
        if (location != null && workmates != null) {
            if (restaurants != null) {
                homeWrapperViewStateMediatorLiveData.setValue(map(
                        restaurants,
                        workmates,
                        location));
            }
        }
    }

    private HomeWrapperViewState map(
            @NonNull List<Result> results,
            @NonNull List<Workmate> workmates,
            @NonNull Location location
    ) {
        List<HomeViewState> homeStatesList = new ArrayList<>();
            for (Result restaurant :
                    results) {
                String placeId = restaurant.getPlaceId();
                if (workmates.size() > 1) {
                    for (Workmate workmate :
                            workmates) {
                        if (workmate.getPlaceId() != null) {
                            if (placeId.equals(workmate.getPlaceId())) {
                                count++;
                            }
                        }
                    }
                }
                homeStatesList.add(mapHomeViewState(restaurant, count));
                count = 0;
            }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Collections.sort(homeStatesList, Comparator.comparingInt(HomeViewState::getDistance));
        }
        return new HomeWrapperViewState(homeStatesList,location);
    }

    private HomeViewState mapHomeViewState(
            Result restaurant,
            int numberWorkmate) {
        String placeId = restaurant.getPlaceId();
        String imageUri = restaurant.getPhotos().get(0).getPhotoReference();
        String restaurantName = restaurant.getName();
        String address = restaurant.getVicinity();
        Geometry geometry = restaurant.getGeometry();
        OpeningHours openingHours = restaurant.getOpeningHours();
        float rating = restaurant.getRating();
        int distance = distance(locationRepository.getLastUserLocation(),
                restaurant.getGeometry().getLocation().getLat(),
                restaurant.getGeometry().getLocation().getLng());
        return new HomeViewState(placeId, imageUri, restaurantName, address, geometry, openingHours, rating, numberWorkmate, distance);
    }

    private int distance(Location userLocation, double lat, double lng) {
        float[] results = new float[1];
        Location.distanceBetween(
                userLocation.getLatitude(),
                userLocation.getLongitude(),
                lat,
                lng,
                results);
        return (int) results[0];
    }

    public LiveData<HomeWrapperViewState> getHomeViewStateLiveData() {
        return homeWrapperViewStateMediatorLiveData;
    }

    public void sortByProximity() {
        HomeWrapperViewState homeWrapperViewState = homeWrapperViewStateMediatorLiveData.getValue();
        if (homeWrapperViewState != null) {
            List<HomeViewState> sortedList = new ArrayList<>(homeWrapperViewState.getHomeViewState());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Collections.sort(sortedList, Comparator.comparingInt(HomeViewState::getDistance));
            }
            HomeWrapperViewState updatedHomeWrapperViewState = new HomeWrapperViewState(sortedList, homeWrapperViewState.getLocation());
            homeWrapperViewStateMediatorLiveData.setValue(updatedHomeWrapperViewState);
        }
    }

    public void sortByWorkmateNumber() {
        HomeWrapperViewState homeWrapperViewState = homeWrapperViewStateMediatorLiveData.getValue();
        if (homeWrapperViewState != null) {
            List<HomeViewState> sortedList = new ArrayList<>(homeWrapperViewState.getHomeViewState());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Collections.sort(sortedList, Comparator.comparingInt(HomeViewState::getWorkmateNumber).reversed());
            }
            HomeWrapperViewState updatedHomeWrapperViewState = new HomeWrapperViewState(sortedList, homeWrapperViewState.getLocation());
            homeWrapperViewStateMediatorLiveData.setValue(updatedHomeWrapperViewState);
        }
    }

    public boolean isUserLogged(){
        return workmateRepository.isUserLogged();
    }

    public void sortByRestaurantRating() {
        HomeWrapperViewState homeWrapperViewState = homeWrapperViewStateMediatorLiveData.getValue();
        if (homeWrapperViewState != null) {
            List<HomeViewState> sortedList = new ArrayList<>(homeWrapperViewState.getHomeViewState());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Collections.sort(sortedList, Comparator.comparingDouble(HomeViewState::getRating).reversed());
            }
            HomeWrapperViewState updatedHomeWrapperViewState = new HomeWrapperViewState(sortedList, homeWrapperViewState.getLocation());
            homeWrapperViewStateMediatorLiveData.setValue(updatedHomeWrapperViewState);
        }
    }
}