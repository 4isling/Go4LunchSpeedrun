package com.Hayse.go4lunch.ui.viewmodel;

import android.location.Location;

import androidx.lifecycle.ViewModel;

import com.Hayse.go4lunch.services.google_map.LocationRepository;
import com.Hayse.go4lunch.services.google_map.google_api.AutocompleteRepository;

public class MainActivityViewModel extends ViewModel {
    private AutocompleteRepository autocompleteRepository;
    private LocationRepository locationRepository;
    private final Location defaultLocation = new Location("48.888053, 2.343312");


}
