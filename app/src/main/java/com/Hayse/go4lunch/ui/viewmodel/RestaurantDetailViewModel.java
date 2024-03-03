package com.Hayse.go4lunch.ui.viewmodel;

import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.Hayse.go4lunch.MainApplication;
import com.Hayse.go4lunch.domain.entites.FavRestaurant;
import com.Hayse.go4lunch.domain.entites.Workmate;
import com.Hayse.go4lunch.domain.entites.map_api.detail.Result;
import com.Hayse.go4lunch.services.firebase.FavRepository;
import com.Hayse.go4lunch.services.firebase.WorkmateRepository;
import com.Hayse.go4lunch.services.google_map.google_api.DetailRepository;
import com.Hayse.go4lunch.ui.view_state.DetailViewState;

import java.util.ArrayList;
import java.util.List;

public class RestaurantDetailViewModel extends ViewModel {
    private WorkmateRepository workmateRepository;

    private FavRestaurant favRestaurant = new FavRestaurant();
    private FavRepository favRepository;
    private DetailRepository detailRepository;

    private String placeId;

    LiveData<Result> restaurantLiveData;
    LiveData<Workmate> userData;
    MutableLiveData<List<Workmate>> listWorkmateMutableLiveData;

    public RestaurantDetailViewModel(DetailRepository detailRepository,
                                     WorkmateRepository workmateRepository,
                                     FavRepository favRepository){
        this.workmateRepository = workmateRepository;
        this.detailRepository = detailRepository;
        this.favRepository = favRepository;
    }

    public void init(String placeId){
        this.placeId = placeId;
        this.restaurantLiveData = getRestaurantDetail();
    }

    private void combine(@Nullable Result restaurant,
                         @Nullable List<Workmate> workmates){
    }

    public LiveData<Result> getRestaurantDetail(){
        return detailRepository.getDetail(placeId);
    }

    public LiveData<List<Workmate>> getListWorkmateLiveData(){
        return workmateRepository.getWorkmateByRestaurant(placeId);
    }

    public LiveData<Workmate> getUserData() {
        return workmateRepository.getRealTimeUserData();
    }

    public LiveData<List<FavRestaurant>>  getUserFavList(){
        return favRepository.getFavList();
    }

    //todo update userfav list
    public void onClickFav() {
        favRestaurant.setPlace_id(restaurantLiveData.getValue().getPlaceId());
        favRestaurant.setRestaurant_address(restaurantLiveData.getValue().getFormattedAddress());
        favRestaurant.setRestaurant_name(restaurantLiveData.getValue().getName());
        favRestaurant.setRestaurant_phone(restaurantLiveData.getValue().getFormattedPhoneNumber());
        favRestaurant.setRestaurant_rating(restaurantLiveData.getValue().getRating());
        favRestaurant.setRestaurant_website(restaurantLiveData.getValue().getWebsite());
        favRestaurant.setRestaurant_pic(restaurantLiveData.getValue().getPhotos().get(0).getPhotoReference());
        favRepository.updateFavRestaurant(favRestaurant);
    }

    //todo update db.workmate. with restaurantData
    public void onClickRestaurantChoice(){
        workmateRepository.updateRestaurantChoice(restaurantLiveData.getValue().getPlaceId(),
                restaurantLiveData.getValue().getName(),
                restaurantLiveData.getValue().getFormattedAddress()
                );
    }
}
