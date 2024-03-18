package com.Hayse.go4lunch.ui.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.Hayse.go4lunch.MainApplication;
import com.Hayse.go4lunch.services.firebase.FavRepository;
import com.Hayse.go4lunch.services.firebase.FirebaseHelper;
import com.Hayse.go4lunch.services.firebase.WorkmateRepository;
import com.Hayse.go4lunch.services.location.LocationRepository;
import com.Hayse.go4lunch.services.google_map.RetrofitService;
import com.Hayse.go4lunch.services.google_map.google_api.AutocompleteRepository;
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
    private final FavRepository favRepository;

    @NonNull
    private final DetailRepository detailRepository;

    @NonNull
    private final AutocompleteRepository autocompleteRepository;

    @NonNull
    private final NearBySearchRepository nearBySearchRepository;

    public static ViewModelFactory getInstance() {
        if (factory == null) {
            application = MainApplication.getApplication();
            synchronized (ViewModelFactory.class) {
                if (factory == null) {
                    FirebaseHelper firebaseHelper = FirebaseHelper.getInstance();
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
                            new WorkmateRepository(firebaseHelper),
                            new DetailRepository(
                                    RetrofitService.getGMapsApi()
                            ),
                            new FavRepository(),
                            new AutocompleteRepository(
                                    RetrofitService.getGMapsApi(),
                                    application
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
            @NonNull DetailRepository detailRepository,
            @NonNull FavRepository favRepository,
            @NonNull AutocompleteRepository autocompleteRepository

    ) {
        this.nearBySearchRepository = nearBySearchRepository;
        this.permissionChecker = permissionChecker;
        this.locationRepository = locationRepository;
        this.workmateRepository = workmateRepository;
        this.detailRepository = detailRepository;
        this.favRepository = favRepository;
        this.autocompleteRepository = autocompleteRepository;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(WorkmateViewModel.class)) {
            return (T) new WorkmateViewModel(workmateRepository);
        }
        if (modelClass.isAssignableFrom(HomeRestaurantSharedViewModel.class)) {
            return (T) new HomeRestaurantSharedViewModel(
                    locationRepository,
                    nearBySearchRepository,
                    workmateRepository
                    );
        }
        if (modelClass.isAssignableFrom(RestaurantDetailViewModel.class)){
            return (T) new RestaurantDetailViewModel(
                    detailRepository,
                    workmateRepository,
                    favRepository
            );
        }
        if (modelClass.isAssignableFrom(SettingViewModel.class)){
            return (T) new SettingViewModel(
                    workmateRepository);
        }
        if (modelClass.isAssignableFrom(ProfileViewModel.class)){
            return (T) new ProfileViewModel(
                    workmateRepository,
                    favRepository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class: " + modelClass);
    }

}