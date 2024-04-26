package com.Hayse.go4lunch.view_model;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;

import com.Hayse.go4lunch.services.firebase.WorkmateRepository;
import com.Hayse.go4lunch.ui.viewmodel.SettingViewModel;


@RunWith(MockitoJUnitRunner.class)
public class SettingViewModelTest {

    @Mock
    private WorkmateRepository workmateRepository;

    private SettingViewModel viewModel;

    @Before
    public void setUp() {
        // Initialize the ViewModel with the mocked repository
        viewModel = new SettingViewModel(workmateRepository);
    }

    @Test
    public void testDeleteUserAccount_callsDeleteUser() {
        // Act
        viewModel.deleteUserAccount();

        // Assert
        verify(workmateRepository).deleteUser();  // Verify if deleteUser was called on the repository
    }
}

