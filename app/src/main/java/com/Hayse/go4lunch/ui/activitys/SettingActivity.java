package com.Hayse.go4lunch.ui.activitys;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import com.Hayse.go4lunch.R;
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
        this.configureSettingUi();
    }

    private void initViewModel() {
        this.viewModelFactory = ViewModelFactory.getInstance();
        this.viewModel = new ViewModelProvider(this, viewModelFactory).get(SettingViewModel.class);
    }

    private void configureToolbar() {
        toolbar = binding.activitySettingToolbar;
        toolbar.setTitle(R.string.settings);
        toolbar.setOnClickListener(v -> onBackPressed());
    }
    private void configureSettingUi() {
        binding.languageChoice.setClickable(true);
        binding.languageChoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //@todo open dialoge to chose language
            }
        });

        binding.notificationChoice.setClickable(true);
        binding.notificationChoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //@todo open notification setting
            }
        });
    }

}
