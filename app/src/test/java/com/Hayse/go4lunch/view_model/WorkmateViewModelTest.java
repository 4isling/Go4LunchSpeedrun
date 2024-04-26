package com.Hayse.go4lunch.view_model;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.Hayse.go4lunch.domain.entites.Workmate;
import com.Hayse.go4lunch.services.firebase.WorkmateRepository;
import com.Hayse.go4lunch.ui.viewmodel.WorkmateViewModel;
import com.Hayse.go4lunch.utils.FakeWorkmates;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class WorkmateViewModelTest {

    @Rule
    public final InstantTaskExecutorRule mInstantTaskExecutorRule = new InstantTaskExecutorRule();

    @Mock
    private WorkmateRepository repository;

    private WorkmateViewModel viewModel;

    @Mock
    private Observer<List<Workmate>> workmatesObserver;

    @Before
    public void setup() {
        LiveData<List<Workmate>> mockLiveData = FakeWorkmates.workmatesLiveData();
        when(repository.getAllWorkmateRt()).thenReturn(mockLiveData);
        viewModel = new WorkmateViewModel(repository);
    }

    @Test
    public void testGetWorkmates(){
        viewModel.getWorkmatesRt().observeForever(workmatesObserver);
        verify(repository).getAllWorkmateRt();  // Ensure repository method is called
        verify(workmatesObserver).onChanged(FakeWorkmates.workmates());  // Verify that the correct data is emitted
    }
}