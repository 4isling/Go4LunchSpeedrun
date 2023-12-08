package com.Hayse.go4lunch.ui.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.Hayse.go4lunch.MainApplication;
import com.Hayse.go4lunch.services.firebase.WorkmateRepository;
import com.Hayse.go4lunch.services.google_map.LocationRepository;
import com.Hayse.go4lunch.services.google_map.RetrofitService;
import com.Hayse.go4lunch.services.google_map.google_api.RestaurantRepository;
import com.Hayse.go4lunch.services.permission_checker.PermissionChecker;
import com.google.android.gms.location.LocationServices;

public class ViewModelFactory implements ViewModelProvider.Factory {
    private static volatile ViewModelFactory factory;
    private static Application application;
    @NonNull
    private final PermissionChecker permissionChecker;
    @NonNull
    private final LocationRepository locationRepository;
    @NonNull
    private final WorkmateRepository workmateRepository;

    @NonNull
    private final RestaurantRepository restaurantRepository;

    public static ViewModelFactory getInstance() {
        if (factory == null) {
            application = MainApplication.getApplication();
            synchronized (ViewModelFactory.class) {
                if (factory == null) {
                    factory = new ViewModelFactory(
                            new PermissionChecker(
                                    application
                            ),
                            new LocationRepository(
                                    LocationServices.getFusedLocationProviderClient(
                                            application
                                    )
                            ),
                            new RestaurantRepository(
                                    RetrofitService.getGMapsApi()
                            ),
                            new WorkmateRepository()


                    );
                }
            }
        }
        return factory;
    }

    private ViewModelFactory(
            @NonNull PermissionChecker permissionChecker,
            @NonNull LocationRepository locationRepository,
            @NonNull RestaurantRepository restaurantRepository,
            @NonNull WorkmateRepository workmateRepository

    ) {
        this.restaurantRepository = restaurantRepository;
        this.permissionChecker = permissionChecker;
        this.locationRepository = locationRepository;
        this.workmateRepository = workmateRepository;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(WorkmateViewModel.class)) {
            return (T) new WorkmateViewModel();
        }
        /*
        if (modelClass.isAssignableFrom(RestaurantListViewModel.class)) {
            return (T) new RestaurantListViewModel(
                    restaurantRepository,
                    locationRepository,
                    permissionChecker
            );
        }*/
        if (modelClass.isAssignableFrom(MapViewModel.class)) {
            return (T) new MapViewModel(
                    permissionChecker,
                    locationRepository,
                    restaurantRepository
                    );
        }
        throw new IllegalArgumentException("Unknown ViewModel class: " + modelClass);
    }

}