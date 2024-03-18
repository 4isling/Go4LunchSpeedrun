package com.Hayse.go4lunch.view_model;

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
        MockitoAnnotations.initMocks(this);
        viewModel = new RestaurantDetailViewModel(detailRepository, workmateRepository, favRepository);
        DetailFakeResult detailFakeResult = new DetailFakeResult();
        Result fakeResult = detailFakeResult.getFakeDetailResult();

        String placeId = "fakePlaceID";
        viewModel.init(placeId);

        when(detailRepository.getDetail(placeId)).thenReturn(detailFakeResult.getFakeDetailResultLiveData());
    }

    @Test
    public void testGetRestaurantDetail() {
        Result result = new Result();
        result.setName("Test Restaurant");
        LiveData<Result> liveData = new MutableLiveData<>(result);
        when(detailRepository.getDetail("fakePlaceID")).thenReturn(liveData);

        viewModel.init("fakePlaceID");
        LiveData<Result> resultLiveData = viewModel.getRestaurantLiveData();

        assert resultLiveData.equals(liveData);
    }

    @Test
    public void testGetListWorkmateLiveData() {
        List<Workmate> workmates = Arrays.asList(new Workmate(), new Workmate());
        LiveData<List<Workmate>> liveData = new MutableLiveData<>(workmates);
        when(workmateRepository.getWorkmateByRestaurant("123")).thenReturn(liveData);

        viewModel.init("123");
        LiveData<List<Workmate>> workmateLiveData = viewModel.getListWorkmateLiveData();

        assert workmateLiveData.equals(liveData);
    }

    @Test
    public void testGetUserData() {
        Workmate workmate = new Workmate();
        LiveData<Workmate> liveData = new MutableLiveData<>(workmate);
        when(workmateRepository.getRealTimeUserData()).thenReturn(liveData);

        LiveData<Workmate> userData = viewModel.getUserData();

        assert userData.equals(liveData);
    }

    @Test
    public void testGetUserFavList() {
        List<FavRestaurant> favRestaurants = Arrays.asList(new FavRestaurant(), new FavRestaurant());
        LiveData<List<FavRestaurant>> liveData = new MutableLiveData<>(favRestaurants);
        when(favRepository.getFavList()).thenReturn(liveData);

        LiveData<List<FavRestaurant>> favList = viewModel.getUserFavList();

        assert favList.equals(liveData);
    }
}