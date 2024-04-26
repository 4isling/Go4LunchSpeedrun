package com.Hayse.go4lunch.view_model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.text.Editable;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.Hayse.go4lunch.domain.entites.FavRestaurant;
import com.Hayse.go4lunch.domain.entites.Workmate;
import com.Hayse.go4lunch.services.firebase.FavRepository;
import com.Hayse.go4lunch.services.firebase.WorkmateRepository;
import com.Hayse.go4lunch.ui.viewmodel.ProfileViewModel;
import com.Hayse.go4lunch.utils.LiveDataTestUtils;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class ProfileViewModelTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Mock
    private WorkmateRepository workmateRepository;

    @Mock
    private FavRepository favRepository;

    private ProfileViewModel viewModel;

    @Before
    public void setUp() {
        MutableLiveData<Workmate> userData = new MutableLiveData<>();
        userData.setValue(new Workmate()); // ensure it's not null

        when(workmateRepository.getRealTimeUserData()).thenReturn(userData);
        viewModel = new ProfileViewModel(workmateRepository, favRepository);
    }


    @Test
    public void testGetFavList() {
        MutableLiveData<List<FavRestaurant>> expectedList = new MutableLiveData<>();
        when(favRepository.getFavList()).thenReturn(expectedList);

        LiveData<List<FavRestaurant>> result = viewModel.getFavList();

        verify(favRepository).getFavList();
        assertEquals(expectedList, result);
    }

    @Test
    public void testRemoveFav() {
        FavRestaurant favRestaurant = new FavRestaurant();

        viewModel.removeFav(favRestaurant);

        verify(favRepository).deleteFavRestaurant(favRestaurant);
    }
    @Test
    public void testSaveProfile() {
        Editable name = mock(Editable.class);
        Editable email = mock(Editable.class);
        when(name.toString()).thenReturn("John Doe");
        when(email.toString()).thenReturn("john@example.com");

        boolean result = viewModel.saveProfile(name, email);

        assertTrue(result);
        verify(workmateRepository).updateWorkmate(null, "John Doe", null, null, null, null, null);
        verify(workmateRepository).updateWorkmate(null, null, "john@example.com", null, null, null, null);
    }

    @Test
    public void testSetName() throws InterruptedException {
        Workmate user = new Workmate();
        user.setName("Initial Name");
        MutableLiveData<Workmate> liveData = new MutableLiveData<>(user);
        when(workmateRepository.getRealTimeUserData()).thenReturn(liveData);

        viewModel.setUser(user);
        viewModel.setName("New Name");

        assertEquals("New Name", LiveDataTestUtils.getValue(viewModel.getUser()).getName());
    }


    @Test
    public void testSetEmail() throws InterruptedException {
        String newEmail = "new@example.com";
        viewModel.setUser(LiveDataTestUtils.getValue(workmateRepository.getRealTimeUserData())); // Ensure user is set
        viewModel.setEmail(newEmail);

        Workmate updatedUser = LiveDataTestUtils.getValue(viewModel.getUser());
        assertEquals(newEmail, updatedUser.getEmail());
    }
}