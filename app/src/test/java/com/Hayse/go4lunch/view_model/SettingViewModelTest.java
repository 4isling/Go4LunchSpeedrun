package com.Hayse.go4lunch.view_model;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.verify;

import com.Hayse.go4lunch.services.firebase.WorkmateRepository;
import com.Hayse.go4lunch.ui.viewmodel.SettingViewModel;

public class SettingViewModelTest {

    @Mock
    private WorkmateRepository workmateRepository;

    private SettingViewModel viewModel;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        viewModel = new SettingViewModel(workmateRepository);
    }

    @Test
    public void deleteUserAccount_shouldCallDeleteUserOnRepository() {
        viewModel.deleteUserAccount();
        verify(workmateRepository).deleteUser();
    }
}

