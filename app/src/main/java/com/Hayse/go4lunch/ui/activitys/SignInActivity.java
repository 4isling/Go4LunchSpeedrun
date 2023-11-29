package com.Hayse.go4lunch.ui.activitys;


import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.Hayse.go4lunch.R;
import com.Hayse.go4lunch.databinding.ActivitySigninBinding;
import com.Hayse.go4lunch.services.firebase.FirebaseHelper;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.Identity;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.signin.internal.SignInClientImpl;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class SignInActivity extends AppCompatActivity {
    protected GoogleSignInOptions gso;
    protected GoogleSignInClient gsc;

    private static final int RC_SIGN_IN = 666;
    private static final String TAG = "SignInActivity";

    private final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();;

    private FirebaseAuth.AuthStateListener listener;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivitySigninBinding binding = ActivitySigninBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Log.d(TAG, "onCreate: ");
        FirebaseApp.initializeApp(this);
        boolean isMain = isMainProcess(this);
        listener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

            }
        };
    }

    @Override
    protected void onStart(){
        super.onStart();
        firebaseAuth.addAuthStateListener(listener);
        initFirebaseAuth();
    }

    @Override
    protected void onStop() {
        if(listener != null)firebaseAuth.removeAuthStateListener(listener);
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
            this.initFirebaseAuth();
    }
    private boolean isMainProcess(Context context) {
        if (null == context) {
            return true;
        }
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

        int pid = android.os.Process.myPid();
        for (ActivityManager.RunningAppProcessInfo processInfo : manager.getRunningAppProcesses()) {
            if ("APPLICATION_ID".equals(processInfo.processName) && pid == processInfo.pid) {
                return true;
            }
        }
        return false;
    }

    private final ActivityResultLauncher<Intent> signInLauncher = registerForActivityResult(
            new FirebaseAuthUIActivityResultContract(),
            this::onSignInResult
    );

    private void initFirebaseAuth(){
        if(firebaseAuth.getCurrentUser() != null){
            startActivity(new Intent(this, MainActivity.class));
            this.finish();
        }else {
            List<AuthUI.IdpConfig> providers = Arrays.asList(
                    new AuthUI.IdpConfig.GoogleBuilder().build(),
                    new AuthUI.IdpConfig.TwitterBuilder().build(),
                    new AuthUI.IdpConfig.EmailBuilder().build()
            );

            //remplace signInFirebase
            Intent signInIntent = AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setAvailableProviders(providers)
                    .setIsSmartLockEnabled(false, true)
                    .build();
            signInLauncher.launch(signInIntent);
        }
    }

    private void onSignInResult(FirebaseAuthUIAuthenticationResult result) {
        IdpResponse response = result.getIdpResponse();
        if(result.getResultCode() == RESULT_OK){
            //on User SignIn = true;
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            Log.d(TAG, "onActivityResult:" + user.getEmail());

            if(Objects.requireNonNull(user.getMetadata()).getCreationTimestamp() == user.getMetadata().getLastSignInTimestamp()){
                FirebaseHelper.getInstance().setNewWorkmate();
                Toast.makeText(this,"@string/welcome_new_user", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this,"@string/welcome_back", Toast.LENGTH_SHORT).show();

            }
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }else{
            if(response==null){
                Log.d(TAG, "onActivityResult: User cancel sign in request");
            }else{
                Log.d(TAG, "onActivityResult:", response.getError());
            }
        }
    }


    protected FirebaseUser getCurrentUser(){
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    protected boolean isCurrentUserLogged() {
        return (this.getCurrentUser() != null);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode == RC_SIGN_IN){
            if(resultCode == RESULT_OK){
                //on User signin = true;
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                Log.d(TAG, "onActivityResult:" + Objects.requireNonNull(user).getEmail());

                if(Objects.requireNonNull(user.getMetadata()).getCreationTimestamp() == user.getMetadata().getLastSignInTimestamp()){

                    Toast.makeText(this,"@string/welcome_new_user", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(this,"@string/welcome_back", Toast.LENGTH_SHORT).show();
                }
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);

            }else{
                IdpResponse response = IdpResponse.fromResultIntent(data);
                if(response==null){
                    Log.d(TAG, "onActivityResult:User cancel sign in request");
                }else{
                    Log.d(TAG, "onActivityResult:", response.getError());
                }
            }
        }
    }
}
