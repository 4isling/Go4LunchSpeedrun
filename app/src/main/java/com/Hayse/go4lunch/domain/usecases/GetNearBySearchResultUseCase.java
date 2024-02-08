package com.Hayse.go4lunch.domain.usecases;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.Hayse.go4lunch.domain.entites.map_api.nerbysearch.RestaurantResult;
import com.Hayse.go4lunch.services.google_map.LocationRepository;
import com.Hayse.go4lunch.services.google_map.google_api.NearBySearchRepository;

public class GetNearBySearchResultUseCase {
    public static final String RESTAURANT = "restaurant";
    public static final String RADIUS = "1000";

    private LocationRepository locationRepository;

    private NearBySearchRepository nearBySearchRepository;

    // RETRIEVE NEARBY RESULTS FROM LOCATION
    public GetNearBySearchResultUseCase(LocationRepository pLocationRepository,
                                        NearBySearchRepository pNearBySearchRepository) {
        nearBySearchRepository = pNearBySearchRepository;
        locationRepository = pLocationRepository;
    }

    public LiveData<RestaurantResult> invoke() {
        return Transformations.switchMap(locationRepository.getLocationLiveData(), input -> {
            if (input != null) {
                String locationString = input.getLatitude() + "," + input.getLongitude();
                return nearBySearchRepository.getRestaurantListLiveData(RESTAURANT, locationString, RADIUS);

            } else {
                return new MutableLiveData<>();
            }
        });
    }
}
