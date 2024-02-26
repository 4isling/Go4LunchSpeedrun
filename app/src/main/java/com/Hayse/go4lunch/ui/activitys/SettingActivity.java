package com.Hayse.go4lunch.ui.activitys;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import com.Hayse.go4lunch.databinding.ActivitySettingBinding;
import com.Hayse.go4lunch.ui.viewmodel.SettingViewModel;
import com.Hayse.go4lunch.ui.viewmodel.ViewModelFactory;

public class SettingActivity extends AppCompatActivity {
    private ActivitySettingBinding binding;
    private Toolbar toolbar;
    private ViewModelFactory viewModelFactory;
    private SettingViewModel viewModel;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        this.initViewModel();
        this.configureToolbar();
    }
    private void initViewModel() {
        this.viewModelFactory = ViewModelFactory.getInstance();
        this.viewModel = new ViewModelProvider(this, viewModelFactory).get(SettingViewModel.class);
    }

    private void configureToolbar() {
        toolbar = binding.activitySettingToolbar;
        toolbar.setOnClickListener(v -> onBackPressed());
    }


}
