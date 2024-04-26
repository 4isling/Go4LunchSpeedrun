package com.Hayse.go4lunch.view_model;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

import android.app.Application;
import android.location.Location;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.Hayse.go4lunch.R;
import com.Hayse.go4lunch.domain.entites.Workmate;
import com.Hayse.go4lunch.domain.entites.map_api.nerbysearch.RestaurantResult;
import com.Hayse.go4lunch.domain.entites.map_api.nerbysearch.Result;
import com.Hayse.go4lunch.services.firebase.FirebaseHelper;
import com.Hayse.go4lunch.services.firebase.WorkmateRepository;
import com.Hayse.go4lunch.services.google_map.RetrofitService;
import com.Hayse.go4lunch.services.google_map.google_api.GMapsApi;
import com.Hayse.go4lunch.services.google_map.google_api.NearBySearchRepository;
import com.Hayse.go4lunch.services.location.LocationRepository;
import com.Hayse.go4lunch.ui.viewmodel.HomeRestaurantSharedViewModel;
import com.Hayse.go4lunch.utils.FakeWorkmates;
import com.google.android.libraries.places.api.model.Place;

import retrofit2.Call;
import retrofit2.Response;

public class HomeRestaurantSharedViewModelTest {
    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Mock
    private LocationRepository locationRepository;
    @Mock
    private NearBySearchRepository nearBySearchRepository;
    @Mock
    private WorkmateRepository workmateRepository;

    private HomeRestaurantSharedViewModel homeRestaurantSharedViewModel;

    private AutoCloseable closeable;

    @Before
    public void setup() {
        closeable = MockitoAnnotations.openMocks(this); // Initialize mocks and setup the auto-closeable

        // Setup the default returns for necessary LiveData objects
        MutableLiveData<Location> locationLiveData = new MutableLiveData<>();
        MutableLiveData<List<Workmate>> workmatesLiveData = new MutableLiveData<>();
        MutableLiveData<List<Result>> nearbySearchLiveData = new MutableLiveData<>();

        locationLiveData.setValue(new Location("provider"));  // Set a default test Location
        workmatesLiveData.setValue(new ArrayList<>());  // Set default empty list of Workmates
        nearbySearchLiveData.setValue(new ArrayList<>());  // Set default empty list of Results

        when(locationRepository.getLocationLiveData()).thenReturn(locationLiveData);
        when(workmateRepository.getAllWorkmateRt()).thenReturn(workmatesLiveData);
        when(nearBySearchRepository.getRestaurantLiveData(any(Location.class))).thenReturn(nearbySearchLiveData);

        // Initialize ViewModel with mocked repositories
        homeRestaurantSharedViewModel = new HomeRestaurantSharedViewModel(locationRepository, nearBySearchRepository, workmateRepository);
    }

    @After
    public void releaseMocks() throws Exception {
        closeable.close();  // Clean up the mocks
    }

    @Test
    public void testGetLocationLiveData() {
        homeRestaurantSharedViewModel.getLocationLiveData();
        verify(locationRepository, times(1)).getLocationLiveData();
    }

    @Test
    public void testGetRestaurant() {
        Location location = new Location("provider");
        homeRestaurantSharedViewModel.getRestaurant(location);
        verify(nearBySearchRepository, times(1)).getRestaurantLiveData(location);
    }

    @Test
    public void testOnPredictionClick() {
        Place place = mock(Place.class);
        homeRestaurantSharedViewModel.onPredictionClick(place);
    }

    @Test
    public void testGetUserData() {
        Workmate expectedWorkmate = new Workmate(); // Create a fake workmate for the test
        MutableLiveData<Workmate> userLiveData = new MutableLiveData<>();
        userLiveData.setValue(expectedWorkmate);

        when(workmateRepository.getRealTimeUserData()).thenReturn(userLiveData);

        LiveData<Workmate> actualUserData = homeRestaurantSharedViewModel.getUserData();
        assertEquals(expectedWorkmate, actualUserData.getValue());
        verify(workmateRepository, times(1)).getRealTimeUserData();
    }
}