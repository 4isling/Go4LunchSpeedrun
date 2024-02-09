package com.Hayse.go4lunch.ui.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.Hayse.go4lunch.MainApplication;
import com.Hayse.go4lunch.services.firebase.WorkmateRepository;
import com.Hayse.go4lunch.services.google_map.LocationRepository;
import com.Hayse.go4lunch.services.google_map.RetrofitService;
import com.Hayse.go4lunch.services.google_map.google_api.DetailRepository;
import com.Hayse.go4lunch.services.google_map.google_api.NearBySearchRepository;
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
    private final DetailRepository detailRepository;

    @NonNull
    private final NearBySearchRepository nearBySearchRepository;

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
                            new NearBySearchRepository(
                                    RetrofitService.getGMapsApi()
                            ),
                            new WorkmateRepository(),
                            new DetailRepository(
                                    RetrofitService.getGMapsApi()
                            )
                    );
                }
            }
        }
        return factory;
    }

    private ViewModelFactory(
            @NonNull PermissionChecker permissionChecker,
            @NonNull LocationRepository locationRepository,
            @NonNull NearBySearchRepository nearBySearchRepository,
            @NonNull WorkmateRepository workmateRepository,
            @NonNull DetailRepository detailRepository

    ) {
        this.nearBySearchRepository = nearBySearchRepository;
        this.permissionChecker = permissionChecker;
        this.locationRepository = locationRepository;
        this.workmateRepository = workmateRepository;
        this.detailRepository = detailRepository;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(WorkmateViewModel.class)) {
            return (T) new WorkmateViewModel();
        }
        if (modelClass.isAssignableFrom(HomeRestaurantSharedViewModel.class)) {
            return (T) new HomeRestaurantSharedViewModel(
                    permissionChecker,
                    locationRepository,
                    nearBySearchRepository
                    );
        }
        if (modelClass.isAssignableFrom(RestaurantDetailViewModel.class)){
            return (T) new RestaurantDetailViewModel(
                    detailRepository,
                    workmateRepository
            );
        }
        throw new IllegalArgumentException("Unknown ViewModel class: " + modelClass);
    }

}