package com.Hayse.go4lunch.ui.activitys;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.Hayse.go4lunch.databinding.ActivityRestaurantDetailBinding;
import com.Hayse.go4lunch.ui.viewmodel.RestaurantDetailViewModel;

public class RestaurantDetailActivity extends AppCompatActivity {

    private ActivityRestaurantDetailBinding binding;
    private RestaurantDetailViewModel viewModel;

    public static final String PLACE_ID = "PLACE_ID";
    public static Intent navigate(Context context, String placeId){
        Intent intent = new Intent(context, RestaurantDetailActivity.class);
        intent.putExtra(PLACE_ID, placeId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        binding = ActivityRestaurantDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}
