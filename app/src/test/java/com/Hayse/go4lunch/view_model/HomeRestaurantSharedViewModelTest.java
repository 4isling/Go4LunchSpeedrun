package com.Hayse.go4lunch.view_model;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

import android.location.Location;

import com.Hayse.go4lunch.services.firebase.WorkmateRepository;
import com.Hayse.go4lunch.services.google_map.google_api.NearBySearchRepository;
import com.Hayse.go4lunch.services.location.LocationRepository;
import com.Hayse.go4lunch.ui.viewmodel.HomeRestaurantSharedViewModel;
import com.google.android.libraries.places.api.model.Place;

public class HomeRestaurantSharedViewModelTest {

    @Mock
    private LocationRepository locationRepository;

    @Mock
    private NearBySearchRepository nearBySearchRepository;

    @Mock
    private WorkmateRepository workmateRepository;

    @Mock
    private HomeRestaurantSharedViewModel viewModel;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        viewModel = new HomeRestaurantSharedViewModel(locationRepository, nearBySearchRepository, workmateRepository);
    }

    @Test
    public void testStartLocationRequest() {
        viewModel.startLocationRequest();
        verify(locationRepository, times(1)).startLocationRequest();
    }

    @Test
    public void testGetLocationLiveData() {
        viewModel.getLocationLiveData();
        verify(locationRepository, times(1)).getLocationLiveData();
    }

    @Test
    public void testGetRestaurant() {
        Location location = mock(Location.class);
        viewModel.getRestaurant(location);
        verify(nearBySearchRepository, times(1)).getRestaurantLiveData(location);
    }

    @Test
    public void testOnPredictionClick() {
        Place place = mock(Place.class);
        viewModel.onPredictionClick(place);
    }

}