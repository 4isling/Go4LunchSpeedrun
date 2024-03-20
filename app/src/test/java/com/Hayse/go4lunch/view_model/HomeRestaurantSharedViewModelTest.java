package com.Hayse.go4lunch.view_model;

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

    private static final double EXPECTED_LATITUDE =37.422026;
    private static final double EXPECTED_LONGITUDE = -122.083979;
    private final Application application = Mockito.mock(Application.class);
    private final LocationRepository locationRepository = Mockito.mock(LocationRepository.class);
    private final NearBySearchRepository nearBySearchRepository = Mockito.mock(NearBySearchRepository.class);
    private final WorkmateRepository workmateRepository = Mockito.mock(WorkmateRepository.class);
    private final FirebaseHelper firebaseHelper = Mockito.mock(FirebaseHelper.class);
    private final Location location = Mockito.mock(Location.class);
    private HomeRestaurantSharedViewModel homeRestaurantSharedViewModel;
    private final MutableLiveData<RestaurantResult> nearbySearchResult = new MutableLiveData<>();
    private final MutableLiveData<Location> locationMutableLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<Workmate>> workmateList = new MutableLiveData<>();
    private final MutableLiveData<Workmate> user = new MutableLiveData<>();

    @Before
    public void setup() {
        Mockito.doReturn("Open").when(application).getString(R.string.is_open);
        Mockito.doReturn("Closed").when(application).getString(R.string.is_close);
        workmateList.setValue(FakeWorkmates.workmates());
        Mockito.doReturn(EXPECTED_LATITUDE).when(location).getLatitude();
        Mockito.doReturn(EXPECTED_LONGITUDE).when(location).getLongitude();
        Mockito.doReturn(0F).when(location).distanceTo(any());

        Mockito.doReturn(locationMutableLiveData).when(locationRepository).getLocationLiveData();
        Mockito.doReturn(nearbySearchResult).when(nearBySearchRepository).getRestaurantLiveData(location);
        Mockito.doReturn(workmateList).when(workmateRepository).getAllWorkmateRt();

        locationMutableLiveData.setValue(location);

        homeRestaurantSharedViewModel = new HomeRestaurantSharedViewModel(locationRepository, nearBySearchRepository, workmateRepository);
    }




    @Test
    public void testGetLocationLiveData() {
        homeRestaurantSharedViewModel.getLocationLiveData();
        verify(locationRepository, times(1)).getLocationLiveData();
    }

    @Test
    public void testGetRestaurant() {
        homeRestaurantSharedViewModel.getRestaurant(location);
        verify(nearBySearchRepository, times(1)).getRestaurantLiveData(location);
    }

    @Test
    public void testOnPredictionClick() {
        Place place = mock(Place.class);
        homeRestaurantSharedViewModel.onPredictionClick(place);
    }

    @Test
    public void testGetUserData(){
        // Given
        Workmate expectedWorkmate = FakeWorkmates.getUserData().getValue();
        when(workmateRepository.getRealTimeUserData()).thenReturn(user);
        user.setValue(expectedWorkmate);

        // When
        LiveData<Workmate> actualUserData = homeRestaurantSharedViewModel.getUserData();

        // Then
        assertNotNull(actualUserData);
        assertEquals(expectedWorkmate, actualUserData.getValue());
        verify(workmateRepository, times(1)).getRealTimeUserData();
    }

}