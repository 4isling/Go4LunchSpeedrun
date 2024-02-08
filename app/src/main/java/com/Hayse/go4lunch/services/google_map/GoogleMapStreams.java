package com.Hayse.go4lunch.services.google_map;

import android.annotation.SuppressLint;
import android.location.Location;

import com.Hayse.go4lunch.domain.entites.map_api.nerbysearch.Result;
import com.Hayse.go4lunch.services.google_map.google_api.NearBySearchRepository;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;

public class GoogleMapStreams {
    private NearBySearchRepository nearBySearchRepository;
    private Disposable disposable;
    private LocationRepository locationRepository;

    @SuppressLint("MissingPermission")
    public GoogleMapStreams(NearBySearchRepository pNearBySearchRepository, LocationRepository pLocationRepository){
        nearBySearchRepository = pNearBySearchRepository;
        locationRepository = pLocationRepository;
    }

    private Observable<Location> streamGetUserLocation(){
        return Observable.create(emitter -> locationRepository.getLastUserLocation());
    }

    public Observable<List<Result>> streamFetchResultsNearbySearchApi(){
         return streamGetUserLocation().map(location -> nearBySearchRepository.getRestaurantList(location));
    }
}
