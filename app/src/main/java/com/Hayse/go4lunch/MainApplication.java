package com.Hayse.go4lunch;

import android.app.Application;

import com.google.firebase.FirebaseApp;

public class MainApplication extends Application {
    private static Application sApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        sApplication = this;
        FirebaseApp.initializeApp(this);
    }
    public static Application getApplication() {
        return sApplication;
    }


}
