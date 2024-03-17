package com.Hayse.go4lunch.ui.activitys;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;

import com.Hayse.go4lunch.R;
import com.Hayse.go4lunch.databinding.ActivitySigninBinding;
import com.Hayse.go4lunch.services.firebase.FirebaseHelper;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.Arrays;
import java.util.List;

public class SignInActivity extends AppCompatActivity {
    private static final int RC_SIGN_IN = 666;
    private static final String TAG = "SignInActivity";

    private final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseAuth.AuthStateListener listener;
    private FirebaseHelper firebaseHelper;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivitySigninBinding binding = ActivitySigninBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Log.d(TAG, "onCreate: ");
        FirebaseApp.initializeApp(this);
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                        return;
                    }

                    // Get new FCM registration token
                    String token = task.getResult();
                });
        firebaseHelper = FirebaseHelper.getInstance();

        listener = firebaseAuth -> {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user != null) {
                Log.d(TAG, "listener: user Logged");
                userId = user.getUid();
                checkUserDataAndStartMainActivity();
            } else {
                Log.d(TAG, "onCreate: listener user Not Logged");
            }
        };

        if (firebaseAuth.getCurrentUser() != null) {
            userId = firebaseAuth.getCurrentUser().getUid();
            Log.d(TAG, "onCreate: userUid = " + userId);
            checkUserDataAndStartMainActivity();
        } else {
            Log.d(TAG, "onCreate: initFirebaseAuth call");
            initFirebaseAuth();
        }
    }

    @Override
    protected void onStop() {
        if (listener != null) firebaseAuth.removeAuthStateListener(listener);
        super.onStop();
    }

    private void checkUserDataAndStartMainActivity() {
        firebaseHelper.getUserDataFireStoreByUID(userId).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot documentSnapshot = task.getResult();
                if (documentSnapshot.exists()) {
                    Log.d(TAG, "listener userFirestore exist");
                    Toast.makeText(getApplicationContext(), getString(R.string.string_welcome_back), Toast.LENGTH_SHORT).show();
                } else {
                    Log.d(TAG, "listener userFirestore do not exist");
                    firebaseHelper.setNewWorkmate();
                    Toast.makeText(getApplicationContext(), getString(R.string.string_welcome_new_user), Toast.LENGTH_SHORT).show();
                }
                startMainActivity();
            } else {
                Log.d(TAG, "listener error");
                Toast.makeText(getApplicationContext(), "an error occurred", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void startMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private final ActivityResultLauncher<Intent> signInLauncher = registerForActivityResult(
            new FirebaseAuthUIActivityResultContract(),
            this::onSignInResult
    );

    private void initFirebaseAuth() {
        Log.d(TAG, "initFirebaseAuth: ");

        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.GoogleBuilder().build(),
                new AuthUI.IdpConfig.TwitterBuilder().build(),
                new AuthUI.IdpConfig.EmailBuilder().setRequireName(true).build()
        );

        Intent signInIntent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .setIsSmartLockEnabled(false, true)
                .build();
        signInLauncher.launch(signInIntent);
    }

    private void onSignInResult(FirebaseAuthUIAuthenticationResult result) {
        Log.d(TAG, "onSignInResult: ");
        IdpResponse response = result.getIdpResponse();
        if (response != null) {
            if (result.getResultCode() == RESULT_OK) {
                if (response != null && response.isNewUser()) {
                    response.getEmail();
                    firebaseHelper.setNewWorkmate();
                }
                checkUserDataAndStartMainActivity();
            } else if (result.getResultCode() == RESULT_CANCELED) {
                Toast.makeText(this, "Sign-in was cancelled", Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "onSignInResult: signIn failed", result.getIdpResponse().getError());

        }
    }
}