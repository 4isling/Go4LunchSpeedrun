package com.Hayse.go4lunch.ui.viewmodel;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.Hayse.go4lunch.domain.entites.Workmate;
import com.Hayse.go4lunch.domain.entites.map_api.detail.Result;
import com.Hayse.go4lunch.services.firebase.WorkmateRepository;
import com.Hayse.go4lunch.services.google_map.google_api.DetailRepository;
import com.Hayse.go4lunch.ui.view_state.DetailViewState;

import java.util.List;

public class RestaurantDetailViewModel extends ViewModel {
    private DetailViewState viewState;
    private WorkmateRepository workmateRepository;
    private DetailRepository detailRepository;

    public RestaurantDetailViewModel(DetailRepository detailRepository,
                                     WorkmateRepository workmateRepository){
        this.workmateRepository = workmateRepository;
        this.detailRepository = detailRepository;
    }

    public void init(String placeId){
        LiveData<Result> restaurantLiveData = detailRepository.getDetail(placeId);
        LiveData<List<Workmate>> listWorkmateLiveData = workmateRepository.getWorkmateByRestaurant(placeId);
    }

    private void combine(@Nullable Result restaurant,
                         @Nullable List<Workmate> workmates){
        if(restaurant != null){

        }
    }
}
