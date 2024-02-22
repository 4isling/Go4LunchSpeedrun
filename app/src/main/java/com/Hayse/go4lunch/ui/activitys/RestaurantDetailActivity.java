package com.Hayse.go4lunch.ui.activitys;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.Hayse.go4lunch.MainApplication;
import com.Hayse.go4lunch.R;
import com.Hayse.go4lunch.databinding.ActivityRestaurantDetailBinding;
import com.Hayse.go4lunch.domain.entites.FavRestaurant;
import com.Hayse.go4lunch.ui.adapter.WorkmateAdapter;
import com.Hayse.go4lunch.ui.view_state.DetailViewState;
import com.Hayse.go4lunch.ui.viewmodel.RestaurantDetailViewModel;
import com.Hayse.go4lunch.ui.viewmodel.ViewModelFactory;
import com.bumptech.glide.Glide;

public class RestaurantDetailActivity extends AppCompatActivity {
private final String TAG = "ResDetailActivity: ";
    private ActivityRestaurantDetailBinding binding;
    private ViewModelFactory viewModelFactory;

    private RestaurantDetailViewModel viewModel;
    private RecyclerView recyclerView;
    private WorkmateAdapter adapter;

    private DetailViewState viewState;


    public static final String PLACE_ID = "PLACE_ID";

    public static Intent navigate(Context context, String placeId) {
        Intent intent = new Intent(context, RestaurantDetailActivity.class);
        intent.putExtra(PLACE_ID, placeId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRestaurantDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initViewModel();
        initRestaurantDetailUI();
        initWorkmatesUI();
    }

    private void initViewModel() {
        this.viewModelFactory = ViewModelFactory.getInstance();
        this.viewModel = new ViewModelProvider(this, viewModelFactory).get(RestaurantDetailViewModel.class);
        this.viewModel.init(getIntent().getStringExtra(PLACE_ID));
    }

    private void initRestaurantDetailUI() {
        viewModel.getRestaurantDetail().observe(this, restaurantInfo -> {
            if (restaurantInfo != null) {
                if (restaurantInfo.getPhotos().get(0) != null) {
                    String photoRef = restaurantInfo.getPhotos().get(0).getPhotoReference();
                    Glide.with(binding.detailPicture.getContext())
                            .load("https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference="+photoRef+"&key="+ MainApplication.getApplication().getApplicationContext().getResources().getString(R.string.MAPS_API_KEY))
                            .into(binding.detailPicture);
                }
                if (restaurantInfo.getName() != null) {
                    binding.restaurantDetailName.setText(restaurantInfo.getName());

                }
                if (restaurantInfo.getFormattedAddress() != null) {
                    binding.restaurantDetailAddress.setText(restaurantInfo.getFormattedAddress());
                }
                binding.restaurantDetailsRating.setRating((float) (restaurantInfo.getRating() * 3) / 5);
                binding.callIcon.setOnClickListener(v -> {
                    if (restaurantInfo.getFormattedPhoneNumber()!= null ||restaurantInfo.getFormattedPhoneNumber() != ""){
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_DIAL);
                        intent.setData(Uri.parse(restaurantInfo.getFormattedPhoneNumber()));
                        try {
                            Log.d(TAG, "initRestaurantDetailUI: callIconClick");
                            startActivity(intent);
                        } catch (ActivityNotFoundException e){
                            //
                            Log.d(TAG, "initRestaurantDetailUI: callIconClick",e);
                        }
                    }
                });
            }
            binding.webIcon.setOnClickListener(v -> {
                if(restaurantInfo.getWebsite() != null || !restaurantInfo.getWebsite().equals("")){
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(restaurantInfo.getWebsite()));

                   try {
                       Log.d(TAG, "initRestaurantDetailUI: webIconClick");
                       startActivity(intent);
                   } catch (ActivityNotFoundException e){
                       Log.d(TAG, "initRestaurantDetailUI: webIconClick",e);
                   }
                }
            });
        });
        binding.favButton.setOnClickListener(v -> viewModel.onClickFav());
    }

    private void initWorkmatesUI() {
        recyclerView = binding.detailRecyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        adapter = new WorkmateAdapter();
        recyclerView.setAdapter(adapter);
        viewModel.getListWorkmateLiveData().observe(this, workmates -> {
            adapter.submitList(workmates);
        });
        binding.choseRestaurantButton.setOnClickListener(v->{
            viewModel.onClickRestaurantChoice();
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        viewModel.getListWorkmateLiveData().removeObservers(this);
    }
}
