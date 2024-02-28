package com.Hayse.go4lunch.services.firebase;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.Hayse.go4lunch.domain.entites.FavRestaurant;
import com.Hayse.go4lunch.domain.entites.Workmate;

import java.util.ArrayList;
import java.util.List;

public class FavRepository {
    private static final String TAG = "FavRepository";
    private final MutableLiveData<List<FavRestaurant>> favList = new MutableLiveData<>();
    private static FavRepository sFavRepository;
    private static FirebaseHelper mFirebaseHelper;

    private final String userId;

    private final MutableLiveData<Workmate> user = new MutableLiveData<>();

    public static FavRepository getInstance(){
        if(sFavRepository == null){
            sFavRepository = new FavRepository();
        }
        return sFavRepository;
    }

    public FavRepository(){
        mFirebaseHelper = FirebaseHelper.getInstance();
        userId =  mFirebaseHelper.getUserUID();

    }

    public LiveData<List<FavRestaurant>> getFavList(){
        return mFirebaseHelper.getUserFavList(userId);
    }

    public void updateFavRestaurant(FavRestaurant favRestaurant) {
        favRestaurant.setUser_id(userId);
        favRestaurant.setId();
        mFirebaseHelper.updateFavRestaurant(favRestaurant);
    }
}
