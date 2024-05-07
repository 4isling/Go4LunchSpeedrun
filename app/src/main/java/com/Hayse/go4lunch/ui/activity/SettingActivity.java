package com.Hayse.go4lunch.ui.activity;


import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import com.Hayse.go4lunch.MainApplication;
import com.Hayse.go4lunch.R;
import com.Hayse.go4lunch.databinding.ActivitySettingBinding;
import com.Hayse.go4lunch.services.permission_checker.PermissionChecker;
import com.Hayse.go4lunch.ui.viewmodel.SettingViewModel;
import com.Hayse.go4lunch.ui.viewmodel.ViewModelFactory;
import com.google.firebase.messaging.FirebaseMessaging;

public class SettingActivity extends AppCompatActivity {
    private static final String TAG = "SettingActivity";
    private ActivitySettingBinding binding;
    private SettingViewModel viewModel;
    private Application application;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: SettingActivity");
        binding = ActivitySettingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        this.application = MainApplication.getApplication();
        this.initViewModel();
        this.configureToolbar();
        this.configButton();
        FirebaseMessaging.getInstance().getToken();
    }
    

    public static Intent navigate(Context context) {
        return new Intent(context, SettingActivity.class);
    }

    private void initViewModel() {
        ViewModelFactory viewModelFactory = ViewModelFactory.getInstance();
        this.viewModel = new ViewModelProvider(this, viewModelFactory).get(SettingViewModel.class);
    }

    private void configureToolbar() {
        Toolbar toolbar = binding.activitySettingToolbar;
        toolbar.setTitle(R.string.settings);
        toolbar.setOnClickListener(v -> onBackPressed());
    }

    private void configButton() {
        binding.globalSettingButton.setOnClickListener(v -> showGlobalPermissionDialog());
        binding.deleteAccountButton.setOnClickListener(v -> showDeleteAccountConfirmationDialog());
        binding.changeProfileButton.setOnClickListener(v -> {
            this.startActivity(ProfileActivity.navigate(getApplicationContext()));
        });
    }

    private void showDeleteAccountConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.delete_account_confirmation_title);
        builder.setMessage(R.string.delete_account_confirmation_message);
        builder.setPositiveButton(R.string.delete, (dialog, which) -> deleteUserAccount());
        builder.setNegativeButton(R.string.cancel, (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    private void showGlobalPermissionDialog() {
        Log.d(TAG, "showGlobalPermissionDialog: ");
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.go_to_app_settings);
        builder.setPositiveButton(R.string.yes, (dialog, which) -> {
                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                    Intent intent = new Intent()
                            .setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                            .setData(uri);
                    startActivity(intent);
                });
        builder.setNegativeButton(R.string.cancel, (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    private void deleteUserAccount() {
        viewModel.deleteUserAccount();
    }
    private void unSubObserver() {
        Log.d(TAG, "unSubObserver: ");
    }

    @Override
    protected void onResume() {
        PermissionChecker permissionChecker = new PermissionChecker(application);
        permissionChecker.hasPostNotification();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy: ");
        unSubObserver();
        super.onDestroy();
    }
}