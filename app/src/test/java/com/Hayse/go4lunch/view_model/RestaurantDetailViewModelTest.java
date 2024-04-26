package com.Hayse.go4lunch.view_model;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.test.filters.SmallTest;

import com.Hayse.go4lunch.domain.entites.FavRestaurant;
import com.Hayse.go4lunch.domain.entites.Workmate;
import com.Hayse.go4lunch.domain.entites.map_api.detail.Result;
import com.Hayse.go4lunch.services.firebase.FavRepository;
import com.Hayse.go4lunch.services.firebase.WorkmateRepository;
import com.Hayse.go4lunch.services.google_map.google_api.DetailRepository;
import com.Hayse.go4lunch.ui.viewmodel.RestaurantDetailViewModel;
import com.Hayse.go4lunch.utils.DetailFakeResult;
import com.Hayse.go4lunch.utils.FakeWorkmates;
import com.Hayse.go4lunch.utils.LiveDataTestUtils;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
@SmallTest
public class RestaurantDetailViewModelTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Mock
    private DetailRepository detailRepository;
    @Mock
    private WorkmateRepository workmateRepository;
    @Mock
    private FavRepository favRepository;

    private RestaurantDetailViewModel viewModel;

    @Before
    public void setup() {
        viewModel = new RestaurantDetailViewModel(detailRepository, workmateRepository, favRepository);
        String placeId = "fakePlaceID";
        Result fakeResult = new Result();
        fakeResult.setName("Test Restaurant");

        MutableLiveData<Result> resultLiveData = new MutableLiveData<>(fakeResult);
        when(detailRepository.getDetail(placeId)).thenReturn(resultLiveData);

        viewModel.init(placeId);
    }

    @Test
    public void testGetRestaurantDetail() throws InterruptedException {
        Result expected = new Result();
        expected.setName("Test Restaurant");
        MutableLiveData<Result> liveData = new MutableLiveData<>(expected);
        when(detailRepository.getDetail("fakePlaceID")).thenReturn(liveData);

        viewModel.init("fakePlaceID");
        LiveData<Result> resultLiveData = viewModel.getRestaurantLiveData();

        assertEquals(expected, LiveDataTestUtils.getValue(resultLiveData));
    }

    @Test
    public void testGetListWorkmateLiveData() throws InterruptedException {
        List<Workmate> expectedWorkmates = Arrays.asList(new Workmate(), new Workmate());
        MutableLiveData<List<Workmate>> liveData = new MutableLiveData<>(expectedWorkmates);
        when(workmateRepository.getWorkmateByRestaurant("123")).thenReturn(liveData);

        viewModel.init("123");
        LiveData<List<Workmate>> workmateLiveData = viewModel.getListWorkmateLiveData();

        assertEquals(expectedWorkmates, LiveDataTestUtils.getValue(workmateLiveData));
    }

    @Test
    public void testGetUserData() throws InterruptedException {
        Workmate expectedWorkmate = new Workmate();
        MutableLiveData<Workmate> liveData = new MutableLiveData<>(expectedWorkmate);
        when(workmateRepository.getRealTimeUserData()).thenReturn(liveData);

        LiveData<Workmate> userData = viewModel.getUserData();

        assertEquals(expectedWorkmate, LiveDataTestUtils.getValue(userData));
    }

    @Test
    public void testGetUserFavList() throws InterruptedException {
        List<FavRestaurant> expectedFavRestaurants = Arrays.asList(new FavRestaurant(), new FavRestaurant());
        MutableLiveData<List<FavRestaurant>> liveData = new MutableLiveData<>(expectedFavRestaurants);
        when(favRepository.getFavList()).thenReturn(liveData);

        LiveData<List<FavRestaurant>> favList = viewModel.getUserFavList();

        assertEquals(expectedFavRestaurants, LiveDataTestUtils.getValue(favList));
    }
}