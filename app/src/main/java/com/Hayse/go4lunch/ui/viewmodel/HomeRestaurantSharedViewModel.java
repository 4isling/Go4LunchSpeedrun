package com.Hayse.go4lunch.ui.viewmodel;

import android.annotation.SuppressLint;
import android.app.Application;
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
import com.Hayse.go4lunch.domain.entites.map_api.autocomplete.Prediction;
import com.Hayse.go4lunch.domain.entites.map_api.nerbysearch.Geometry;
import com.Hayse.go4lunch.domain.entites.map_api.nerbysearch.OpeningHours;
import com.Hayse.go4lunch.domain.entites.map_api.nerbysearch.Result;
import com.Hayse.go4lunch.domain.usecases.GetNearBySearchResultUseCase;
import com.Hayse.go4lunch.services.firebase.WorkmateRepository;
import com.Hayse.go4lunch.services.google_map.LocationRepository;
import com.Hayse.go4lunch.services.google_map.google_api.AutocompleteRepository;
import com.Hayse.go4lunch.services.google_map.google_api.NearBySearchRepository;
import com.Hayse.go4lunch.services.permission_checker.PermissionChecker;
import com.Hayse.go4lunch.ui.view_state.HomeViewState;
import com.Hayse.go4lunch.ui.view_state.HomeWrapperViewState;
import com.google.android.libraries.places.api.model.Place;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class HomeRestaurantSharedViewModel extends ViewModel {
    private final static String TAG = "HomeSharedViewModel";
    private final Application application;
    private LocationRepository locationRepository;
    private NearBySearchRepository nearBySearchRepository;
    private WorkmateRepository workmateRepository;
    private int count = 0;
    private AutocompleteRepository autocompleteRepository;
    private PermissionChecker permissionChecker;
    private GetNearBySearchResultUseCase getNearBySearchResultUseCase;
    private final Location defaultLocation = new Location("48.888053, 2.343312");
    private final MutableLiveData<Boolean> hasGpsPermissionLiveData = new MutableLiveData<>();
    private List<HomeViewState> homeViewStateList = new ArrayList<>();
    private final MediatorLiveData<HomeWrapperViewState> homeWrapperViewStateMediatorLiveData = new MediatorLiveData<>();

    ////////////////////////////////////////////////////
    private MutableLiveData<Place> placeAutocompleteSelected = new MutableLiveData<>(null);

    @SuppressLint("MissingPermission")
    public HomeRestaurantSharedViewModel(
            @NonNull Application application,
            @NonNull PermissionChecker permissionChecker,
            @NonNull LocationRepository locationRepository,
            @NonNull NearBySearchRepository nearBySearchRepository,
            @NonNull WorkmateRepository workmateRepository,
            @NonNull AutocompleteRepository autocompleteRepository
    ) {
        this.application = application;
        this.locationRepository = locationRepository;
        this.nearBySearchRepository = nearBySearchRepository;
        this.workmateRepository = workmateRepository;
        this.autocompleteRepository = autocompleteRepository;
        locationRepository.startLocationRequest();
        setViewState();
    }

    public void setViewState() {
        LiveData<List<Workmate>> workmatesLiveData = workmateRepository.getAllWorkmate();
        LiveData<Location> userLocationLiveData = locationRepository.getLocationLiveData();
        LiveData<List<Result>> nearbySearchListLiveData = Transformations.switchMap(locationRepository.getLocationLiveData(), userLocation -> {
            return nearBySearchRepository.getRestaurantLiveData(userLocation);
        });

        homeWrapperViewStateMediatorLiveData.addSource(nearbySearchListLiveData, resultList -> combine(
                resultList,
                userLocationLiveData.getValue(),
                workmatesLiveData.getValue()));

        homeWrapperViewStateMediatorLiveData.addSource(workmateRepository.getAllWorkmate(), workmates -> combine(
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

    public LiveData<List<Workmate>> getWorkmates() {
        return workmateRepository.getAllWorkmate();
    }

    public LiveData<List<Prediction>> getPredictionList(String text) {
        Location location = locationRepository.getLastUserLocation();
        return autocompleteRepository.getPredictionLiveData(location, text);
    }

    public void onPredictionClick(Place place) {
        Log.d(TAG, "onPredictionClick: " + place.toString());
        this.placeAutocompleteSelected.setValue(place);
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
/*if (restaurants != null) {
            for (int i = 0; i< restaurants.size(); i++){
                for (Result restaurant:
                        restaurants) {
                    String placeId = restaurant.getPlaceId();
                    if (workmates!=null){
                        for (Workmate workmate:
                                workmates) {
                            if (workmate.getPlaceId()!= null){
                                if (placeId.equals(workmate.getPlaceId())){
                                    count++;
                                }
                            }
                        }
                    }
                    homeViewStateList.add(mapHomeViewState(restaurant, count));
                    count = 0;
                }
            }
        }
        Collections.sort(homeViewStateList, Comparator.comparingInt(HomeViewState::getDistance));
        homeWrapperViewStateMediatorLiveData.setValue(new HomeWrapperViewState(homeViewStateList));
        homeViewStateList.clear();*/
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
        int workmateNumber = numberWorkmate;
        int distance = distance(locationRepository.getLastUserLocation(),
                restaurant.getGeometry().getLocation().getLat(),
                restaurant.getGeometry().getLocation().getLng());
        return new HomeViewState(placeId, imageUri, restaurantName, address, geometry, openingHours, rating, workmateNumber, distance);
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
}