package com.Hayse.go4lunch.services.permission_checker;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.POST_NOTIFICATIONS;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Application;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

public class PermissionChecker {

    @NonNull
    private final Application application;

    public PermissionChecker(@NonNull Application application){
        this.application = application;
    }

    /***
     * check if permission to user location is enable
     * @return boolean
     *
     */
    public boolean hasLocationPermission(){
        return ContextCompat.checkSelfPermission(application, ACCESS_FINE_LOCATION) == PERMISSION_GRANTED;
    }


    public boolean hasPostNotification(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            return ContextCompat.checkSelfPermission(application, POST_NOTIFICATIONS) == PERMISSION_GRANTED;
        }else {
            NotificationManager notificationManager = (NotificationManager) application.getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return notificationManager.isNotificationPolicyAccessGranted();
            }
        }
        return true;
    }

}
