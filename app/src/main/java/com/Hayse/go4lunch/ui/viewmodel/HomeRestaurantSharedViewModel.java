package com.Hayse.go4lunch.ui.viewmodel;

import android.annotation.SuppressLint;
import android.app.Application;
import android.location.Location;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.Hayse.go4lunch.domain.entites.Workmate;
import com.Hayse.go4lunch.domain.entites.map_api.autocomplete.AutocompleteResponse;
import com.Hayse.go4lunch.domain.entites.map_api.autocomplete.Prediction;
import com.Hayse.go4lunch.domain.entites.map_api.nerbysearch.Result;
import com.Hayse.go4lunch.domain.usecases.GetNearBySearchResultUseCase;
import com.Hayse.go4lunch.services.firebase.WorkmateRepository;
import com.Hayse.go4lunch.services.google_map.LocationRepository;
import com.Hayse.go4lunch.services.google_map.google_api.AutocompleteRepository;
import com.Hayse.go4lunch.services.google_map.google_api.NearBySearchRepository;
import com.Hayse.go4lunch.services.permission_checker.PermissionChecker;

import org.checkerframework.checker.units.qual.Luminance;

import java.util.ArrayList;
import java.util.List;

public class HomeRestaurantSharedViewModel extends ViewModel {
    private final static String TAG = "MapViewModel";
    private final Application application;
    private LocationRepository locationRepository;
    private NearBySearchRepository nearBySearchRepository;
    private WorkmateRepository workmateRepository;
    private AutocompleteRepository autocompleteRepository;
    private PermissionChecker permissionChecker;
    private GetNearBySearchResultUseCase getNearBySearchResultUseCase;
    private final Location defaultLocation = new Location("48.888053, 2.343312");
    private final MutableLiveData<Boolean> hasGpsPermissionLiveData = new MutableLiveData<>();

    ////////////////////////////////////////////////////
    private LiveData<List<Result>> resultLiveData = new MutableLiveData<>(null);

    private  LiveData<com.Hayse.go4lunch.domain.entites.map_api.detail.Result> detailResult;




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
    }


    public LiveData<Location> getLocationMutableLiveData(){
        return locationRepository.getLocationLiveData();
    }

    public LiveData<List<Result>> getRestaurant(Location location){
        return nearBySearchRepository.getRestaurantLiveData(location);
    }

    public LiveData<List<Workmate>> getWorkmates() {
        return workmateRepository.getAllWorkmate();
    }

    private List<Prediction> getPredictionList(){
        List<Prediction> predictionsList = new ArrayList<>();

        //@todo prediction list
        return predictionsList;
    }
}