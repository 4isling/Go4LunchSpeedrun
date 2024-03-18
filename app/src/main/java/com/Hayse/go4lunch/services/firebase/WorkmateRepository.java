package com.Hayse.go4lunch.services.firebase;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.work.WorkManager;

import com.Hayse.go4lunch.MainApplication;
import com.Hayse.go4lunch.domain.entites.Workmate;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class WorkmateRepository {
    private static final String TAG = "WorkmateRepository";
    private final LiveData<List<Workmate>> listOfWorkmates;
    private static FirebaseHelper mFirebaseHelper;

    private final LiveData<Workmate>  userData;

    public WorkmateRepository(FirebaseHelper firebaseHelper){
        mFirebaseHelper = firebaseHelper;
        userData = mFirebaseHelper.getFirestoreUserDataRT();
        listOfWorkmates = mFirebaseHelper.getRtWorkmates();
    }

    public LiveData<List<Workmate>> getAllWorkmateRt(){
        return listOfWorkmates;
    }

    public LiveData<List<Workmate>> getWorkmateByRestaurant(String placeId) {
        return mFirebaseHelper.getWorkmateByPlaceId(placeId);
    }

    public LiveData<Workmate> getRealTimeUserData(){
        return userData;
    }

    public void deleteUser() {
        mFirebaseHelper.deleteUser();
    }

    public void updateWorkmate(@Nullable String avatarUrl,
                               @Nullable String name,
                               @Nullable String email,
                               @Nullable String placeId,
                               @Nullable String restaurantName,
                               @Nullable String restaurantAddress,
                               @Nullable String restaurantTypeOfFood) {
        mFirebaseHelper.updateUserData(avatarUrl,name,email,placeId, restaurantName, restaurantAddress, restaurantTypeOfFood);
    }

    public boolean isUserLogged() {
        return mFirebaseHelper.isUserLogged();
    }
}