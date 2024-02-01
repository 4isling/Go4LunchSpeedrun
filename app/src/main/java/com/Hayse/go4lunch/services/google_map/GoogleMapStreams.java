package com.Hayse.go4lunch.services.google_map;

import android.annotation.SuppressLint;
import android.location.Location;

import com.Hayse.go4lunch.domain.entites.map_api.nerbysearch.Result;
import com.Hayse.go4lunch.services.google_map.google_api.RestaurantRepository;
import com.Hayse.go4lunch.ui.viewmodel.ViewModelFactory;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.core.ObservableSource;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class GoogleMapStreams {
    private RestaurantRepository restaurantRepository;
    private Disposable disposable;
    private LocationRepository locationRepository;

    @SuppressLint("MissingPermission")
    public GoogleMapStreams(RestaurantRepository pRestaurantRepository, LocationRepository pLocationRepository){
        restaurantRepository = pRestaurantRepository;
        locationRepository = pLocationRepository;
    }

    private Observable<Location> streamGetUserLocation(){
        return Observable.create(emitter -> locationRepository.getLastUserLocation());
    }

    public Observable<List<Result>> streamFetchResultsNearbySearchApi(){
         return streamGetUserLocation().map(location -> restaurantRepository.getRestaurantList(location));
    }
}
