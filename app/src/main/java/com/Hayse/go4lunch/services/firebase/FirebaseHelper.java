package com.Hayse.go4lunch.services.firebase;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.Hayse.go4lunch.domain.entites.Workmate;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class FirebaseHelper {
    private static FirebaseHelper sFirebaseHelper;
    private static String TAG = "FirebaseHelper";
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String userUID;

    public static FirebaseHelper getInstance(){
        Log.d(TAG, "getInstance: ");
        if(sFirebaseHelper == null){
            sFirebaseHelper = new FirebaseHelper();
        }
        return sFirebaseHelper;
    }

    /**
     * Get Data From user OAuth account to make a Workmate object
     * @return
     */

    public Workmate getUserData(){
        FirebaseAuth firebaseAuth =  FirebaseAuth.getInstance();

        Workmate workmate = new Workmate(Objects.requireNonNull(firebaseAuth.getCurrentUser()).getPhotoUrl().toString(),
                firebaseAuth.getCurrentUser().getDisplayName(),
                firebaseAuth.getCurrentUser().getUid());
        userUID = workmate.getId();
        Log.d(TAG, "getUserData: userUID"+ userUID);
        return workmate;
    }

    public final CollectionReference workmateRef = db.collection("workmates");

    public final CollectionReference favRestaurantRef = db.collection("favorite_restaurant");

    public Task<QuerySnapshot> getAllWorkmate(){
        return workmateRef.get();
    }

    public void setNewWorkmate(){
        Log.d(TAG, "setNewWorkmate: ");
        Workmate uData = getUserData();
        Log.d(TAG, "setNewWorkmate: uData" + uData);
        Map<String, Object> workmate = new HashMap<>();
        workmate.put("id", uData.getId());
        workmate.put("avatarUrl", uData.getAvatarUrl());
        workmate.put("name", uData.getName());
        workmate.put("placeId", "");
        workmate.put("restaurantAddress","");
        workmate.put("restaurantName", "");
        workmate.put("restaurantTypeOfFood", "");
        workmateRef.document(uData.getId())
                .set(workmate)
                .addOnSuccessListener(aVoid -> Log.d(TAG, "Document successfully written!: user:"+uData.getName()+" added"))
                .addOnFailureListener(e -> Log.w(TAG, "Error adding document", e));
    }


    public Task<QuerySnapshot> updateUserData(@Nullable String avatarUrl,
                                              @Nullable String placeId,
                                              @Nullable String restaurantName,
                                              @Nullable String restaurantAddress,
                                              @Nullable String restaurantTypeOfFood){
        Workmate uData = getUserData();
        Map<String, Object> updateData = new HashMap<>();
        if(avatarUrl != null){
            updateData.put("avatarUrl", avatarUrl);
        }
        if(placeId != null){
            updateData.put("placeId", placeId);
        }
        if(restaurantName != null){
            updateData.put("restaurantName", restaurantName);
        }
        if (restaurantAddress != null){
            updateData.put("restaurantAddress", restaurantAddress);
        }
        if (restaurantTypeOfFood != null){
            updateData.put("restaurantTypeOfFood", restaurantTypeOfFood);
        }
        workmateRef.document(uData.getId())
                .update(updateData)
                .addOnSuccessListener(aVoid -> Log.d(TAG, "DocumentSnapshot successfully updated!"))
                .addOnFailureListener(e -> Log.w(TAG, "Error updating document", e));
        return null;
    }

    /**
     *
     * @param userUID
     * @return true if user with this Id exist
     */
    public Boolean checkIfUserIdExist(String userUID){
        final boolean[] result = new boolean[1];
        workmateRef.document("userUID").get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                DocumentSnapshot document = task.getResult();
                if(document.exists()){
                    result[0] = true;
                    Log.d(TAG, "onComplete:DocumentExist: UserData: "+ document.getData());
                } else {
                    result[0] = false;
                    Log.d(TAG, "onComplete:no such Document");
                }
            }else {
                Log.d(TAG, "onComplete: Failed with", task.getException());
            }
        });
        return result[0];
    }
/*
    public static Task<Void> createUser(Workmate workmate){
        return FirebaseFirestore
    }*/
    private void saveNewWorkmate(Workmate workmate){

    }

    public Task<QuerySnapshot> getWorkmateByPlaceId(String placeId) {
        return workmateRef.whereEqualTo("placeId", placeId)
                .get();
    }
}
