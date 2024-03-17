package com.Hayse.go4lunch.services.firebase;

import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.Hayse.go4lunch.domain.entites.FavRestaurant;
import com.Hayse.go4lunch.domain.entites.Workmate;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FirebaseHelper {
    private static FirebaseHelper sFirebaseHelper;
    private static String TAG = "FirebaseHelper";
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String userUID;

    private final MutableLiveData<Workmate> userInfo = new MutableLiveData<>();

    private boolean firestoreDataAlreadyFetched = false;

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
    private void getUserDataOAuth() {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        Workmate workmate = new Workmate();
        if (firebaseAuth.getCurrentUser().getPhotoUrl() != null) {
            workmate.setAvatarUrl(firebaseAuth.getCurrentUser().getPhotoUrl().toString());
        }
        workmate.setName(firebaseAuth.getCurrentUser().getDisplayName());
        workmate.setEmail(firebaseAuth.getCurrentUser().getEmail());
        workmate.setId(firebaseAuth.getCurrentUser().getUid());
        userUID = workmate.getId();
        Log.d(TAG, "getUserData: userUID" + userUID);
        userInfo.postValue(workmate);
    }

    public MutableLiveData<Workmate> getFirestoreUserDataRT() {
        MutableLiveData<Workmate> realTimeUserData = new MutableLiveData<>();
        if (!firestoreDataAlreadyFetched) {
            workmateRef.document(FirebaseAuth.getInstance().getUid()).addSnapshotListener((value, error) -> {
                if (error != null) {
                    Log.w(TAG, "onEvent: error:", error);
                }
                userInfo.postValue(value.toObject(Workmate.class));
            });
            firestoreDataAlreadyFetched = true;
        }
        return userInfo;
    }

    public String getUserUID() {
        return FirebaseAuth.getInstance().getUid();
    }

    public MutableLiveData<List<Workmate>> getRtWorkmates() {
        MutableLiveData<List<Workmate>> workmates = new MutableLiveData<>(new ArrayList<>());
        if (workmates.getValue() != null) {
            if (workmates.getValue().isEmpty()) {
                workmateRef.addSnapshotListener((value, error) -> {
                    if (error != null) {
                        Log.w(TAG, "GetRtWorkmateListener", error);
                    }
                    if (value != null && !value.isEmpty()) {
                        Log.d(TAG, "getWorkmate: not empty");
                        List<Workmate> workmatesResult = value.toObjects(Workmate.class);
                        workmates.postValue(workmatesResult);
                    }
                });
            }
        }
        return workmates;
    }

    public void setNewWorkmate() {
        Log.d(TAG, "setNewWorkmate: ");
        getUserDataOAuth();
        Workmate uData = userInfo.getValue();
        Log.d(TAG, "setNewWorkmate: uData" + uData);
        Map<String, Object> workmate = new HashMap<>();
        workmate.put("id", uData.getId());
        workmate.put("avatarUrl", uData.getAvatarUrl());
        workmate.put("name", uData.getName());
        workmate.put("email", uData.getEmail());
        workmate.put("placeId", "");
        workmate.put("restaurantAddress", "");
        workmate.put("restaurantName", "");
        workmate.put("restaurantTypeOfFood", "");
        workmateRef.document(uData.getId())
                .set(workmate)
                .addOnSuccessListener(aVoid -> Log.d(TAG, "Document successfully written!: user:" + uData.getName() + " added"))
                .addOnFailureListener(e -> Log.w(TAG, "Error adding document", e));
    }


    public void updateUserData(@Nullable String avatarUrl,
                               @Nullable String name,
                               @Nullable String email,
                               @Nullable String placeId,
                               @Nullable String restaurantName,
                               @Nullable String restaurantAddress,
                               @Nullable String restaurantTypeOfFood) {

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
        if (name != null) {
            updateData.put("name", name);
        }
        if (email != null) {
            updateData.put("email", email);
        }
        workmateRef.document(FirebaseAuth.getInstance().getUid())
                .update(updateData)
                .addOnSuccessListener(aVoid -> Log.d(TAG, "DocumentSnapshot successfully updated!"))
                .addOnFailureListener(e -> Log.w(TAG, "Error updating document", e));
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
        workmateRef.whereEqualTo("placeId", placeId).addSnapshotListener((snapshots, error) -> {
            if (error != null) {
                Log.d(TAG, "listen failed: ", error);
                return;
            }
            if (!snapshots.isEmpty()) {
                Log.d(TAG, "getWorkmateByPlaceId: not empty");
                List<Workmate> workmates = snapshots.toObjects(Workmate.class);
                workmatesResult.postValue(workmates);
            } else {
                Log.d(TAG, "getWorkmateByPlaceId: is empty");
                workmatesResult.postValue(new ArrayList<>());
            }
        });
        return workmatesResult;
    }

    public Task<QuerySnapshot> getWorkmateForNotification(String placeId) {
        return workmateRef.whereEqualTo("placeId", placeId).get();
    }

    public Task<DocumentSnapshot> getUserDataFireStore() {
        return workmateRef.document(FirebaseAuth.getInstance().getUid()).get();
    }

    public Task<DocumentSnapshot> getUserDataFireStoreByUID(String userID) {
        return workmateRef.document(FirebaseAuth.getInstance().getUid()).get();
    }

    public MutableLiveData<List<FavRestaurant>> getUserFavList(String userID) {
        MutableLiveData<List<FavRestaurant>> favRestaurantResult = new MutableLiveData<>();
        if (favRestaurantResult.getValue()== null){
            favRestaurantRef.whereEqualTo("user_id", userID).addSnapshotListener(((value, error) -> {
                if (error != null) {
                    Log.e(TAG, "getUserFavList: ", error);
                    return;
                }
                List<FavRestaurant> favRestaurantsList = new ArrayList<>();
                if (value != null && !value.isEmpty()) {
                    Log.d(TAG, "getUserFavList: not empty");
                    for (DocumentSnapshot doc : value.getDocuments()) {
                        favRestaurantsList.add(doc.toObject(FavRestaurant.class));
                    }
                }
                favRestaurantResult.postValue(favRestaurantsList);
            }));
        }
        return favRestaurantResult;
    }

    /**
     * add favRestaurant to database if it dont exist delete it if it exist
     *
     * @param favRestaurant
     * @return true if restaurant added in db false if it isn't
     */
    public void updateFavRestaurant(FavRestaurant favRestaurant) {
        favRestaurantRef.document(FirebaseAuth.getInstance().getUid() + favRestaurant.getPlace_id()).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot documentSnapshot = task.getResult();
                if (!documentSnapshot.exists()) {
                    favRestaurantRef.document(favRestaurant.getId())
                            .set(favRestaurant)
                            .addOnSuccessListener(aVoid -> {
                                Log.d(TAG, "updateFavRestaurant: restaurantAdded");
                            })
                            .addOnFailureListener(e -> {
                                Log.d(TAG, "updateFavRestaurant: add fav Failed");
                            });
                    Log.d(TAG, "onComplete:DocumentExist: UserData: " + documentSnapshot.getData());
                } else {
                    favRestaurantRef.document(favRestaurant.getId()).delete()
                            .addOnSuccessListener(aVoid -> {
                                Log.d(TAG, "updateFavRestaurant: favRestaurant deleted");
                            })
                            .addOnFailureListener(e -> {
                                Log.d(TAG, "updateFavRestaurant: failed to delete favRestaurant");
                            });
                    Log.d(TAG, "onComplete:no such Document");
                }
            } else {
                Log.d(TAG, "onComplete: Failed with", task.getException());
            }
        });
    }

    public void deleteUser() {
        workmateRef.whereEqualTo("uid", getUserUID()).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    document.getReference().delete();
                }
            } else {
                Log.e(TAG, "Failed to delete user's workmates.", task.getException());
            }
        });
        favRestaurantRef.whereEqualTo("user_id", getUserUID()).addSnapshotListener(((value, error) -> {
                    if (error != null) {
                        Log.e(TAG, "getUserFavList: ", error);
                        return;
                    }
                    if (!value.isEmpty()) {
                        Log.d(TAG, "getUserFavList: not empty");
                        for (DocumentSnapshot doc : value.getDocuments()) {
                            doc.getReference().delete();
                        }
                    } else {
                        Log.d(TAG, "getUserFavList: is empty");
                    }
                })
        );
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            user.delete()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User account deleted successfully.");
                            // You can sign out the user or perform other actions here
                            FirebaseAuth.getInstance().signOut();
                            // Finish the current activity or navigate to the appropriate screen
                        } else {
                            Log.e(TAG, "Failed to delete user account.", task.getException());
                        }
                    });
        } else {
            Log.e(TAG, "No user is currently signed in.");
        }
    }

    public void deleteFavRestaurant(FavRestaurant favRestaurant) {
        favRestaurantRef.whereEqualTo("id", favRestaurant.getId()).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    document.getReference().delete();
                }
            } else {
                Log.e(TAG, "Failed to delete fav restaurant.", task.getException());
            }
        });
    }
}