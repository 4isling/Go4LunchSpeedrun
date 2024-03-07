package com.Hayse.go4lunch.ui.activitys;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.Hayse.go4lunch.MainApplication;
import com.Hayse.go4lunch.R;
import com.Hayse.go4lunch.databinding.ActivitySettingBinding;
import com.Hayse.go4lunch.ui.viewmodel.SettingViewModel;
import com.Hayse.go4lunch.ui.viewmodel.ViewModelFactory;
import com.google.firebase.messaging.FirebaseMessaging;

public class SettingActivity extends AppCompatActivity {
    private ActivitySettingBinding binding;
    private Toolbar toolbar;
    private ViewModelFactory viewModelFactory;
    private SettingViewModel viewModel;

    private Application application;
    private String CHANNEL_ID;

    // Declare the launcher at the top of your Activity/Fragment:
    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    // FCM SDK (and your app) can post notifications.
                    Toast.makeText(this, R.string.alow_notification_toast, Toast.LENGTH_SHORT).show();
                } else {
                    // TODO: Inform user that that your app will not show notifications.
                    Toast.makeText(this, "Go4Lunch won't send you any notification", Toast.LENGTH_SHORT).show();
                }
            });
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        this.application = MainApplication.getApplication();
        CHANNEL_ID = application.getString(R.string.CLOUD_MESSAGE_ID);
        this.initViewModel();
        this.configureToolbar();
        this.configureSettingUi();
        FirebaseMessaging.getInstance().getToken();
    }

    private void initViewModel() {
        this.viewModelFactory = ViewModelFactory.getInstance();
        this.viewModel = new ViewModelProvider(this, viewModelFactory).get(SettingViewModel.class);
    }

    private void configureToolbar() {
        toolbar = binding.activitySettingToolbar;
        toolbar.setTitle(R.string.settings);
        toolbar.setOnClickListener(v -> onBackPressed());
    }
    @SuppressLint("ClickableViewAccessibility")
    private void configureSettingUi() {
        binding.notificationTitle.setText(R.string.notification_title);
        binding.notificationTitle.setTextSize(16);
        binding.notificationChoice.setText(R.string.manage_notification);
        if (binding.notificationSwitch.isActivated()) {
            binding.notificationSwitch.setText(R.string.on);
        } else {
            binding.notificationSwitch.setText(R.string.off);
        }
        binding.notificationSwitch.setOnTouchListener((v, event) -> {
            if (binding.notificationSwitch.isActivated()) {
                this.askNotificationPermission();
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                        PackageManager.PERMISSION_GRANTED) {
                    binding.notificationSwitch.setText(R.string.on);
                } else {
                    binding.notificationSwitch.setActivated(false);
                    binding.notificationSwitch.setText(R.string.off);
                }
            }
            else{
                binding.notificationSwitch.setText(R.string.off);

            }
            return false;
        });
    }



    //Notification permission for api>=33
    private void askNotificationPermission() {
        // This is only necessary for API level >= 33 (TIRAMISU)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                    PackageManager.PERMISSION_GRANTED) {
                // FCM SDK (and your app) can post notifications.
                binding.notificationSwitch.setText(R.string.on);
            } else if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                // TODO: display an educational UI explaining to the user the features that will be enabled
                //       by them granting the POST_NOTIFICATION permission. This UI should provide the user
                //       "OK" and "No thanks" buttons. If the user selects "OK," directly request the permission.
                //       If the user selects "No thanks," allow the user to continue without notifications.
            } else {
                // Directly ask for the permission
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
            }
        }
    }

    //notification channel for api<33
    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is not in the Support Library.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, getString(R.string.app_name), importance);
            channel.setDescription("description");
            // Register the channel with the system. You can't change the importance
            // or other notification behaviors after this.
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

}
