package com.Hayse.go4lunch.services.firebase;

import android.util.Log;

import androidx.annotation.NonNull;
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
    private final MutableLiveData<List<Workmate>> listOfWorkmate = new MutableLiveData<>();

    private static WorkmateRepository sWorkmateRepository;

    private MutableLiveData<Workmate> userData = new MutableLiveData<>();
    private static FirebaseHelper mFirebaseHelper;


    public static WorkmateRepository getInstance(){
        if(sWorkmateRepository == null){
            sWorkmateRepository = new WorkmateRepository();
        }
        return sWorkmateRepository;
    }

    public WorkmateRepository(){
        mFirebaseHelper = FirebaseHelper.getInstance();
        // Uncomment this method to populate your firebase database, it will upload some data
        // Comment it again after the first launch
    }

    public MutableLiveData<List<Workmate>> getAllWorkmate(){
        Log.d(TAG, "getAllWorkmate: ");
        mFirebaseHelper.getAllWorkmate().addOnCompleteListener(task->{
            if(task.isSuccessful()){
                Log.d(TAG, "getAllWorkmate: task.isSuccessful");
                ArrayList<Workmate> workmate = new ArrayList<>();
                for(QueryDocumentSnapshot document : task.getResult()){
                    Log.d(TAG, "getAllWorkmate: "+task.getResult().getDocuments());
                    workmate.add(document.toObject(Workmate.class));
                }
                listOfWorkmate.postValue(workmate);
            } else {
                Log.e("WorkmateRepError", "Error getting documents", task.getException());
            }
        }).addOnFailureListener(new OnFailureListener(){
            @Override
            public void onFailure(@NonNull Exception e){
                //handle error
                Log.d("WorkmateRepError", "Error getting documents", e);
                listOfWorkmate.postValue(new ArrayList<>());
            }
        });
        Log.d(TAG, "getAllWorkmate: before return");
        return listOfWorkmate;
    }
    public LiveData<List<Workmate>> getListOfWorkmate(){
        return mFirebaseHelper.getRtWorkmates();
    }

    public LiveData<List<Workmate>> getWorkmateByRestaurant(String placeId) {
        return mFirebaseHelper.getWorkmateByPlaceId(placeId);
    }

    public MutableLiveData<Workmate> getUserData(){
        mFirebaseHelper.getUserDataFireStore().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                    userData.postValue(task.getResult().toObject(Workmate.class));
            }
        });
        return userData;
    }

    public LiveData<Workmate> getRealTimeUserData(){
        return mFirebaseHelper.getFirestoreUserDataRT();
    }

    public void updateRestaurantChoice(String placeId, String name, String address) {
        mFirebaseHelper.getUserDataFireStore().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                Workmate user = task.getResult().toObject(Workmate.class);
                if (!Objects.equals(user.getPlaceId(), placeId) || user.getPlaceId().equals("")){
                    Log.d(TAG, "updateRestaurantChoice: addRestaurantChoice");
                    mFirebaseHelper.updateUserData(null, placeId, name,address,null);
                }else{
                    Log.d(TAG, "updateRestaurantChoice: suppressRestaurantChoice");
                    mFirebaseHelper.updateUserData(null,"","","",null);
                }
            }
        });

    }
}
