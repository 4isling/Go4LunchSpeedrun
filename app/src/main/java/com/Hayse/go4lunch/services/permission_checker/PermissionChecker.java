package com.Hayse.go4lunch.services.permission_checker;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;

import android.app.Application;

import androidx.annotation.NonNull;
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
}
