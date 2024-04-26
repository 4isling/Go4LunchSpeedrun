package com.Hayse.go4lunch.ui.activitys;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.Hayse.go4lunch.R;
import com.Hayse.go4lunch.databinding.ActivitySigninBinding;
import com.Hayse.go4lunch.services.firebase.FirebaseHelper;

import com.Hayse.go4lunch.ui.viewmodel.SignInViewModel;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;

public class SignInActivity extends AppCompatActivity {
    private static final String TAG = "SignInActivity";

    private ActivitySigninBinding binding;
    private SignInViewModel viewModel;

    private final ActivityResultLauncher<Intent> signInLauncher = registerForActivityResult(
            new FirebaseAuthUIActivityResultContract(), this::onSignInResult);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySigninBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(SignInViewModel.class);

        observeAuthenticationState();

        viewModel.getFirebaseToken().observe(this, token -> {
            if (token == null) {
                Log.w(TAG, "Fetching FCM registration token failed");
                return;
            }
            Log.d(TAG, "FCM Token: " + token);
        });
    }

    private void observeAuthenticationState() {
        viewModel.getAuthenticationState().observe(this, user -> {
            if (user != null) {
                Log.d(TAG, "User is logged in with UID: " + user.getUid());
                checkUserDataAndStartMainActivity(user);
            } else {
                Log.d(TAG, "No user logged, starting Firebase Auth UI");
                initFirebaseAuth();
            }
        });
    }

    private void checkUserDataAndStartMainActivity(FirebaseUser user) {
        FirebaseHelper.getInstance().getUserDataFireStoreByUID(user.getUid()).addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult().exists()) {
                Log.d(TAG, "User data exists, navigating to MainActivity");
                startMainActivity();
            } else {
                Log.d(TAG, "User data does not exist, creating new user data");
                FirebaseHelper.getInstance().setNewWorkmate(this);
                startMainActivity();
            }
        });
    }

    private void startMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void initFirebaseAuth() {
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.GoogleBuilder().build(),
                new AuthUI.IdpConfig.EmailBuilder().setRequireName(true).build(),
                new AuthUI.IdpConfig.TwitterBuilder().build());

        Intent signInIntent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .setIsSmartLockEnabled(false)
                .build();
        signInLauncher.launch(signInIntent);
    }

    private void onSignInResult(FirebaseAuthUIAuthenticationResult result) {
        IdpResponse response = result.getIdpResponse();
        if (result.getResultCode() == RESULT_OK) {
            Log.d(TAG, "Sign-in successful");
            if (response != null && response.isNewUser()) {
                Log.d(TAG, "New user: " + response.getEmail());
            }
            // Additional user checks or setup can be handled here
        } else if (result.getResultCode() == RESULT_CANCELED) {
            Toast.makeText(this, R.string.sign_in_cancelled, Toast.LENGTH_SHORT).show();
        } else {
            Log.e(TAG, "Sign-in failed", response != null ? response.getError() : null);
            Toast.makeText(this, R.string.sign_in_failed, Toast.LENGTH_SHORT).show();
        }
    }
}