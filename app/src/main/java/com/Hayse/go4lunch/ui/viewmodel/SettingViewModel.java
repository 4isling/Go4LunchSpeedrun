package com.Hayse.go4lunch.ui.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.Hayse.go4lunch.domain.entites.Workmate;
import com.Hayse.go4lunch.services.firebase.WorkmateRepository;

import java.util.List;

public class SettingViewModel extends ViewModel {
    private final WorkmateRepository workmateRepository;
    public SettingViewModel(
            @NonNull WorkmateRepository workmateRepository
    ){
        this.workmateRepository = workmateRepository;
    }

    public void deleteUserAccount() {
        workmateRepository.deleteUser();
    }
}