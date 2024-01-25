package com.Hayse.go4lunch.ui.viewmodel;

import android.annotation.SuppressLint;
import android.location.Location;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.Hayse.go4lunch.R;
import com.Hayse.go4lunch.domain.entites.map_api.nerbysearch.RestaurantResult;
import com.Hayse.go4lunch.domain.entites.map_api.nerbysearch.Result;
import com.Hayse.go4lunch.domain.usecases.GetNearBySearchResultUseCase;
import com.Hayse.go4lunch.services.google_map.GoogleMapStreams;
import com.Hayse.go4lunch.services.google_map.LocationRepository;
import com.Hayse.go4lunch.services.google_map.google_api.RestaurantRepository;
import com.Hayse.go4lunch.services.permission_checker.PermissionChecker;
import com.Hayse.go4lunch.ui.view_state.MapViewState;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.observers.DisposableObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.subjects.BehaviorSubject;

public class MapViewModel extends ViewModel {
    private final static String TAG = "MapViewModel";

    private LocationRepository locationRepository;
    private RestaurantRepository restaurantRepository;
    private PermissionChecker permissionChecker;
    private GetNearBySearchResultUseCase getNearBySearchResultUseCase;
    private final Location defaultLocation = new Location("48.888053, 2.343312");
    private final MutableLiveData<Boolean> hasGpsPermissionLiveData = new MutableLiveData<>();

    ////////////////////////////////////////////////////
    private LiveData<List<Result>> resultMutableLiveData = new MutableLiveData<>(null);

    private List<Result> listReturn;

    private MutableLiveData<Location> locationMutableLiveData = new MutableLiveData<>();

    private MutableLiveData<MapViewState> mapViewStateLiveData = new MutableLiveData<>();

    private Observable<List<Result>> restaurantListObservable = BehaviorSubject.create();



    @SuppressLint("MissingPermission")
    public MapViewModel(
            @NonNull PermissionChecker permissionChecker,
            @NonNull LocationRepository locationRepository,
            @NonNull RestaurantRepository restaurantRepository
    ) {
        this.locationRepository = locationRepository;
        this.restaurantRepository = restaurantRepository;
        locationRepository.startLocationRequest();

        GoogleMapStreams googleMapStreams =  new GoogleMapStreams(restaurantRepository, locationRepository);

         Transformations.map(locationRepository.getLocationLiveData(), location -> {
             restaurantListObservable = googleMapStreams.streamFetchResultsNearbySearchApi();
                return new MutableLiveData<>();
        });

    }


    public LiveData<Location> getLocationMutableLiveData(){
        return locationRepository.getLocationLiveData();
    }

    public LiveData<List<Result>> getRestaurant(Location location){
        return restaurantRepository.getRestaurantLiveData(location);
    }
    public LiveData<List<Result>> getListResultLiveData() {
        return resultMutableLiveData;
    }

    public Observable<List<Result>> getRestaurantListObservable() {
        return restaurantListObservable;
    }
}