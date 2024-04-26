package com.Hayse.go4lunch.ui.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessaging;

public class SignInViewModel extends ViewModel {

    private final MutableLiveData<FirebaseUser> userLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> firebaseToken = new MutableLiveData<>();
    private final FirebaseAuth firebaseAuth;

    public SignInViewModel() {
        firebaseAuth = FirebaseAuth.getInstance();
        fetchFirebaseMessagingToken();

        // Listening to authentication state changes
        FirebaseAuth.AuthStateListener authStateListener = firebaseAuth -> {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            userLiveData.postValue(user);
        };
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    public LiveData<FirebaseUser> getAuthenticationState() {
        return userLiveData;
    }

    public LiveData<String> getFirebaseToken() {
        return firebaseToken;
    }

    private void fetchFirebaseMessagingToken() {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        firebaseToken.postValue(task.getResult());
                    } else {
                        firebaseToken.postValue(null);
                    }
                });
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        // Remove listeners if needed or cleanup resources
    }
}

