package com.Hayse.go4lunch.ui.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.Hayse.go4lunch.domain.entites.FavRestaurant;
import com.Hayse.go4lunch.domain.entites.Workmate;
import com.Hayse.go4lunch.domain.entites.map_api.detail.Result;
import com.Hayse.go4lunch.services.firebase.FavRepository;
import com.Hayse.go4lunch.services.firebase.WorkmateRepository;
import com.Hayse.go4lunch.services.google_map.google_api.DetailRepository;

import java.util.List;

public class RestaurantDetailViewModel extends ViewModel {
    private final WorkmateRepository workmateRepository;

    private final FavRestaurant favRestaurant = new FavRestaurant();
    private final FavRepository favRepository;
    private final DetailRepository detailRepository;

    private String placeId;

    LiveData<Result> restaurantLiveData;

    public RestaurantDetailViewModel(DetailRepository detailRepository,
                                     WorkmateRepository workmateRepository,
                                     FavRepository favRepository) {
        this.workmateRepository = workmateRepository;
        this.detailRepository = detailRepository;
        this.favRepository = favRepository;
    }

    public void init(String placeId) {
        this.placeId = placeId;
        this.restaurantLiveData = getRestaurantDetail();
    }


    public LiveData<Result> getRestaurantDetail() {
        return detailRepository.getDetail(placeId);
    }

    public LiveData<List<Workmate>> getListWorkmateLiveData() {
        return workmateRepository.getWorkmateByRestaurant(placeId);
    }

    public LiveData<Workmate> getUserData() {
        return workmateRepository.getRealTimeUserData();
    }

    public LiveData<List<FavRestaurant>> getUserFavList() {
        return favRepository.getFavList();
    }


    public void onClickFav() {
        if (restaurantLiveData.getValue() != null) {
            favRestaurant.setPlace_id(restaurantLiveData.getValue().getPlaceId());
            favRestaurant.setRestaurant_address(restaurantLiveData.getValue().getFormattedAddress());
            favRestaurant.setRestaurant_name(restaurantLiveData.getValue().getName());
            favRestaurant.setRestaurant_phone(restaurantLiveData.getValue().getFormattedPhoneNumber());
            favRestaurant.setRestaurant_rating(restaurantLiveData.getValue().getRating());
            favRestaurant.setRestaurant_website(restaurantLiveData.getValue().getWebsite());
            favRestaurant.setRestaurant_pic(restaurantLiveData.getValue().getPhotos().get(0).getPhotoReference());
            favRepository.updateFavRestaurant(favRestaurant);
        }
    }

    public void onClickRestaurantChoice() {
        if (restaurantLiveData.getValue() != null) {
            if (getUserData() != null){
                if (getUserData().getValue() != null){
                    if (!getUserData().getValue().getPlaceId().equals(restaurantLiveData.getValue().getPlaceId())){
                        workmateRepository.updateWorkmate(null,null,null, restaurantLiveData.getValue().getPlaceId(),
                                restaurantLiveData.getValue().getName(),
                                restaurantLiveData.getValue().getFormattedAddress(),
                                null
                        );
                    }else {
                        workmateRepository.updateWorkmate(null,
                                null,
                                null,
                                "",
                                "",
                                "",
                                null);
                    }
                }
            }
        }
    }
}
