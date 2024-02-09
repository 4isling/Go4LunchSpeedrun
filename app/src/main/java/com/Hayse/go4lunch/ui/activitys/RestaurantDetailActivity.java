package com.Hayse.go4lunch.ui.activitys;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.Hayse.go4lunch.databinding.ActivityRestaurantDetailBinding;
import com.Hayse.go4lunch.ui.adapter.WorkmateAdapter;
import com.Hayse.go4lunch.ui.view_state.DetailViewState;
import com.Hayse.go4lunch.ui.viewmodel.RestaurantDetailViewModel;
import com.Hayse.go4lunch.ui.viewmodel.ViewModelFactory;
import com.bumptech.glide.Glide;

public class RestaurantDetailActivity extends AppCompatActivity {

    private ActivityRestaurantDetailBinding binding;
    private ViewModelFactory viewModelFactory;

    private RestaurantDetailViewModel viewModel;
    private RecyclerView recyclerView;
    private WorkmateAdapter adapter;

    private DetailViewState viewState;


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
        initViewModel();
        initRestaurantDetailUI();
        initWorkmatesUI();
    }

    private void initViewModel(){
        this.viewModelFactory = ViewModelFactory.getInstance();
        this.viewModel = new ViewModelProvider(this, viewModelFactory).get(RestaurantDetailViewModel.class);
        this.viewModel.init(PLACE_ID);
    }
    private void initRestaurantDetailUI(){
        viewModel.getRestaurantDetail().observe(this, restaurantInfo ->{
            Glide.with(binding.detailPicture.getContext())
                    .load(restaurantInfo.getPhotos())
                    .into(binding.detailPicture);
            binding.restaurantDetailName.setText(restaurantInfo.getName());
            binding.restaurantDetailAddress.setText(restaurantInfo.getAdrAddress());
            binding.restaurantDetailsRating.setRating((float) (restaurantInfo.getRating() * 3) / 5);
            binding.callIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(restaurantInfo.getFormattedPhoneNumber()!= null){
                        
                    } else if (restaurantInfo.getInternationalPhoneNumber()!= null) {
                        
                    }
                }
            });

        });
    }
    private void initWorkmatesUI(){
        recyclerView = binding.detailRecyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        adapter = new WorkmateAdapter();
        recyclerView.setAdapter(adapter);
        viewModel.getListWorkmateLiveData().observe(this, workmates -> {
            adapter.submitList(workmates);
        });
        
    }
}
