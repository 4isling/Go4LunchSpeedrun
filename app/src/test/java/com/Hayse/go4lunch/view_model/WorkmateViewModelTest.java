package com.Hayse.go4lunch.view_model;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.Hayse.go4lunch.domain.entites.Workmate;
import com.Hayse.go4lunch.services.firebase.WorkmateRepository;
import com.Hayse.go4lunch.ui.viewmodel.WorkmateViewModel;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;


public class WorkmateViewModelTest {

    @Rule
    public final InstantTaskExecutorRule  mInstantTaskExecutorRule = new InstantTaskExecutorRule();

    @Mock
    private WorkmateRepository repository;

    @Mock
    private Observer<List<Workmate>> listWorkmates

    @Mock
    private WorkmateViewModel viewModel;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        viewModel = new WorkmateViewModel() {
            @Override
            public WorkmateRepository getRepository() {
                return repository;
            }
        };
    }

}
