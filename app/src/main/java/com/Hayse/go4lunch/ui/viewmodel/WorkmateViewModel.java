package com.Hayse.go4lunch.ui.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.Hayse.go4lunch.domain.entites.Workmate;
import com.Hayse.go4lunch.services.firebase.WorkmateRepository;

import java.util.List;

public class WorkmateViewModel extends ViewModel {
    private final WorkmateRepository repository;

    public WorkmateViewModel(WorkmateRepository repository){
        this.repository = repository;
    }

    public LiveData<List<Workmate>> getWorkmatesRt(){
        return repository.getAllWorkmateRt();
    }
}