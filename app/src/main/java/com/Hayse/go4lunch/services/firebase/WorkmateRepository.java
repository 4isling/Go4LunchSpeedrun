package com.Hayse.go4lunch.services.firebase;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.Hayse.go4lunch.domain.entites.Workmate;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class WorkmateRepository {
    private static final String TAG = "WorkmateRepository";
    private final MutableLiveData<List<Workmate>> listOfWorkmate = new MutableLiveData<>();

    private static WorkmateRepository sWorkmateRepository;
    private static FirebaseHelper mFirebaseHelper;

    private static final String COLLECTION_NAME = "workmates";
    private static final String SUCCESS = "success";
    private static final String ERROR = "error";
    private static final String RESTAURANT_CHOICE_NAME = "restaurantChoiceName";
    private static final String LIKES = "likes";


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
                listOfWorkmate.postValue(null);
            }
        });
        Log.d(TAG, "getAllWorkmate: before return");
        return listOfWorkmate;
    }
    public FirebaseUser getCurrentUser(){
        return FirebaseAuth.getInstance().getCurrentUser();
    }


    public void createWorkmate(){
        FirebaseUser workmate = getCurrentUser();
    }


    public void initData(){

    }
}
