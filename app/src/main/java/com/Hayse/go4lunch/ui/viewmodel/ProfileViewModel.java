package com.Hayse.go4lunch.ui.viewmodel;

import android.text.Editable;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.Hayse.go4lunch.domain.entites.FavRestaurant;
import com.Hayse.go4lunch.domain.entites.Workmate;
import com.Hayse.go4lunch.services.firebase.FavRepository;
import com.Hayse.go4lunch.services.firebase.WorkmateRepository;

import java.util.List;

public class ProfileViewModel extends ViewModel {
    private final WorkmateRepository workmateRepository;
    private final FavRepository favRepository;
    private final MutableLiveData<Workmate> user = new MutableLiveData<>();

    public ProfileViewModel(
            @NonNull WorkmateRepository workmateRepository,
            @NonNull FavRepository favRepository) {
        this.favRepository = favRepository;
        this.workmateRepository = workmateRepository;
    }

    public LiveData<List<FavRestaurant>> getFavList() {
        return favRepository.getFavList();
    }

    public void removeFav(FavRestaurant favRestaurant) {
        favRepository.deleteFavRestaurant(favRestaurant);
    }

    public boolean saveProfile(Editable name, Editable emailFieldText) {
        if (name != null && emailFieldText != null) {
            if (!name.toString().equals("")){
                workmateRepository.updateWorkmate(null,
                        name.toString(),
                        null,
                        null,
                        null,
                        null,
                        null);
            }if (!emailFieldText.toString().equals("")){
                workmateRepository.updateWorkmate(null,
                        null,
                        emailFieldText.toString(),
                        null,
                        null,
                        null,
                        null);
            }
            return true;
        } else {
            return false;
        }
    }

    public LiveData<Workmate> getUser() {
        return workmateRepository.getRealTimeUserData();
    }

    public void setUser(Workmate user){
        if (user!= null){
            this.user.setValue(user);
        }

    }

    public void setEmail(String email) {
        Workmate currentUser = user.getValue();
        if (currentUser != null) {
            currentUser.setEmail(email);
            user.postValue(currentUser);
        }
    }

    public void setName(String name) {
        Workmate currentUser = user.getValue();
        if (currentUser != null) {
            currentUser.setName(name);
            user.postValue(currentUser);
        }
    }
}
