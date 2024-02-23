package com.Hayse.go4lunch.services.firebase;

import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.Hayse.go4lunch.domain.entites.FavRestaurant;
import com.Hayse.go4lunch.domain.entites.Workmate;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.MetadataChanges;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

public class FirebaseHelper {
    private static FirebaseHelper sFirebaseHelper;
    private static String TAG = "FirebaseHelper";
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String userUID;

    public final CollectionReference workmateRef = db.collection("workmates");

    public final CollectionReference favRestaurantRef = db.collection("favorite_restaurant");

    public static FirebaseHelper getInstance() {
        Log.d(TAG, "getInstance: ");
        if (sFirebaseHelper == null) {
            sFirebaseHelper = new FirebaseHelper();
        }
        return sFirebaseHelper;
    }

    /**
     * Get Data From user OAuth account to make a Workmate object
     *
     * @return
     */
    public Workmate getUserDataOAuth() {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        Workmate workmate = new Workmate(Objects.requireNonNull(firebaseAuth.getCurrentUser()).getPhotoUrl().toString(),
                firebaseAuth.getCurrentUser().getDisplayName(),
                firebaseAuth.getCurrentUser().getUid());
        userUID = workmate.getId();
        Log.d(TAG, "getUserData: userUID" + userUID);
        return workmate;
    }

    public MutableLiveData<Workmate> getFirestoreUserDataRT() {
        MutableLiveData<Workmate> realTimeUserData = new MutableLiveData<>();
        workmateRef.document(userUID).addSnapshotListener(MetadataChanges.INCLUDE, new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            Log.w(TAG, "onEvent: listener failed", error);
                            return;
                        }
                        realTimeUserData.postValue(value.toObject(Workmate.class));
                    }
                }
        );
        return realTimeUserData;
    }

    public String getUserUID() {
        return userUID;
    }

    public Task<QuerySnapshot> getAllWorkmate() {
        return workmateRef.get();
    }

    public void setNewWorkmate() {
        Log.d(TAG, "setNewWorkmate: ");
        Workmate uData = getUserDataOAuth();
        Log.d(TAG, "setNewWorkmate: uData" + uData);
        Map<String, Object> workmate = new HashMap<>();
        workmate.put("id", uData.getId());
        workmate.put("avatarUrl", uData.getAvatarUrl());
        workmate.put("name", uData.getName());
        workmate.put("placeId", "");
        workmate.put("restaurantAddress", "");
        workmate.put("restaurantName", "");
        workmate.put("restaurantTypeOfFood", "");
        workmateRef.document(uData.getId())
                .set(workmate)
                .addOnSuccessListener(aVoid -> Log.d(TAG, "Document successfully written!: user:" + uData.getName() + " added"))
                .addOnFailureListener(e -> Log.w(TAG, "Error adding document", e));
    }


    public Task<QuerySnapshot> updateUserData(@Nullable String avatarUrl,
                                              @Nullable String placeId,
                                              @Nullable String restaurantName,
                                              @Nullable String restaurantAddress,
                                              @Nullable String restaurantTypeOfFood) {
        Workmate uData = getUserDataOAuth();
        Map<String, Object> updateData = new HashMap<>();
        if (avatarUrl != null) {
            updateData.put("avatarUrl", avatarUrl);
        }
        if (placeId != null) {
            updateData.put("placeId", placeId);
        }
        if (restaurantName != null) {
            updateData.put("restaurantName", restaurantName);
        }
        if (restaurantAddress != null) {
            updateData.put("restaurantAddress", restaurantAddress);
        }
        if (restaurantTypeOfFood != null) {
            updateData.put("restaurantTypeOfFood", restaurantTypeOfFood);
        }
        workmateRef.document(uData.getId())
                .update(updateData)
                .addOnSuccessListener(aVoid -> Log.d(TAG, "DocumentSnapshot successfully updated!"))
                .addOnFailureListener(e -> Log.w(TAG, "Error updating document", e));
        return null;
    }

    /**
     * @param userUID
     * @return true if user with this Id exist
     */
    public Boolean checkIfUserIdExist(String userUID) {
        final boolean[] result = new boolean[1];
        workmateRef.document("userUID").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    result[0] = true;
                    Log.d(TAG, "onComplete:DocumentExist: UserData: " + document.getData());
                } else {
                    result[0] = false;
                    Log.d(TAG, "onComplete:no such Document");
                }
            } else {
                Log.d(TAG, "onComplete: Failed with", task.getException());
            }
        });
        return result[0];
    }

    public MutableLiveData<List<Workmate>> getWorkmateByPlaceId(String placeId) {
        MutableLiveData<List<Workmate>> workmatesResult = new MutableLiveData<>(new ArrayList<>());
        workmateRef.whereEqualTo("placeId", placeId).addSnapshotListener((value, error) -> {
            if (error != null) {
                Log.d(TAG, "listen failed", error);
                return;
            }
            List<Workmate> workmates = new ArrayList<>();
            if (!value.isEmpty()) {
                Log.d(TAG, "getWorkmateByPlaceId: not empty");
                for (DocumentSnapshot doc : value.getDocuments()) {
                    workmates.add(doc.toObject(Workmate.class));
                }
            } else {
                Log.d(TAG, "getWorkmateByPlaceId: is empty");
            }
            workmatesResult.setValue(workmates);
        });
        return workmatesResult;
    }

    public Task<DocumentSnapshot> getUserDataFireStore() {
        Workmate userData = new Workmate();
        return workmateRef.document(userUID).get();
    }

    public MutableLiveData<List<FavRestaurant>> getUserFavList(String userID) {
        MutableLiveData<List<FavRestaurant>> favRestaurantResult = new MutableLiveData<>();
        favRestaurantRef.whereEqualTo("user_id", userID).addSnapshotListener(((value, error) -> {
            if (error != null) {
                Log.e(TAG, "getUserFavList: ", error);
                return;
            }
            List<FavRestaurant> favRestaurantsList = new ArrayList<>();
            if (!value.isEmpty()) {
                Log.d(TAG, "getUserFavList: not empty");
                for (DocumentSnapshot doc : value.getDocuments()) {
                    favRestaurantsList.add(doc.toObject(FavRestaurant.class));
                }
            } else {
                Log.d(TAG, "getUserFavList: is empty");
            }
            favRestaurantResult.setValue(favRestaurantsList);
        }));
        return favRestaurantResult;
    }

    private Boolean checkIfFavRestaurantExist(String placeId) {
        final boolean[] result = new boolean[1];
        favRestaurantRef.document(userUID + placeId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot documentSnapshot = task.getResult();
                if (documentSnapshot.exists()) {
                    result[0] = true;
                    Log.d(TAG, "onComplete:DocumentExist: UserData: " + documentSnapshot.getData());
                } else {
                    result[0] = false;
                    Log.d(TAG, "onComplete:no such Document");
                }
            } else {
                Log.d(TAG, "onComplete: Failed with", task.getException());
            }
        });
        return result[0];
    }

    /**
     * add favRestaurant to database if it dont exist delete it if it exist
     *
     * @param favRestaurant
     * @return true if restaurant added in db false if it isn't
     */
    public boolean updateFavRestaurant(FavRestaurant favRestaurant) {
        favRestaurant.setUser_id(userUID);

        AtomicBoolean restaurantAdded = new AtomicBoolean(false);
        if (!checkIfFavRestaurantExist(favRestaurant.getPlace_id())) {
            favRestaurantRef.document(favRestaurant.getId())
                    .set(favRestaurant)
                    .addOnSuccessListener(aVoid -> {
                        Log.d(TAG, "updateFavRestaurant: restaurantAdded");
                        restaurantAdded.set(true);
                    })
                    .addOnFailureListener(e -> {
                        Log.d(TAG, "updateFavRestaurant: add fav Failed");
                        restaurantAdded.set(false);
                    });
        } else {
            favRestaurantRef.document(favRestaurant.getId()).delete()
                    .addOnSuccessListener(aVoid -> {
                        Log.d(TAG, "updateFavRestaurant: favRestaurant deleted");
                        restaurantAdded.set(false);
                    })
                    .addOnFailureListener(e -> {
                        Log.d(TAG, "updateFavRestaurant: failed to delete favRestaurant");
                        restaurantAdded.set(true);
                    });
        }
        return restaurantAdded.get();
    }
}