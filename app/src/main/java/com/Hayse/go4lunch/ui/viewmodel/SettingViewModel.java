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
    LiveData<List<Workmate>> workmates;
    private final MutableLiveData<Boolean> notificationEnabled = new MutableLiveData<>(true);
    private final WorkmateRepository workmateRepository;
    public SettingViewModel(
            @NonNull WorkmateRepository workmateRepository
    ){
        this.workmateRepository = workmateRepository;
        workmates = workmateRepository.getAllWorkmate();
    }

    public void deleteUserAccount() {
        workmateRepository.deleteUser();
    }
}