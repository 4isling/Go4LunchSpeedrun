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
    public final InstantTaskExecutorRule  mInstantTaskExecutorRule = new InstantTaskExecutorRule();

    @Mock
    private WorkmateRepository repository;

    @Mock
    private Observer<List<Workmate>> listWorkmates;

    @Mock
    private WorkmateViewModel viewModel;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(repository.getAllWorkmateRt()).thenReturn(FakeWorkmates.workmatesLiveData());
        viewModel = new WorkmateViewModel(repository);;
    }

    @Test
    public void testGetWorkmates(){
        viewModel.getWorkmatesRt().observeForever(workmatesObserver);
        verify(repository, times(1)).getAllWorkmateRt();
        verify(workmatesObserver, times(1)).onChanged(FakeWorkmates.workmates());
    }

    private final Observer<List<Workmate>> workmatesObserver = Mockito.mock(Observer.class);
}
