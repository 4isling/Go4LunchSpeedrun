package com.Hayse.go4lunch.ui.activitys;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.Hayse.go4lunch.MainApplication;
import com.Hayse.go4lunch.R;
import com.Hayse.go4lunch.databinding.ActivityRestaurantDetailBinding;
import com.Hayse.go4lunch.domain.entites.FavRestaurant;
import com.Hayse.go4lunch.ui.adapter.WorkmateAdapter;
import com.Hayse.go4lunch.ui.viewmodel.RestaurantDetailViewModel;
import com.Hayse.go4lunch.ui.viewmodel.ViewModelFactory;
import com.bumptech.glide.Glide;

import java.util.List;
import java.util.Objects;

public class RestaurantDetailActivity extends AppCompatActivity {
    private final String TAG = "ResDetailActivity: ";
    private ActivityRestaurantDetailBinding binding;
    private String placeId;
    private RestaurantDetailViewModel viewModel;
    private WorkmateAdapter adapter;

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
        binding.activityDetailToolbar.setOnClickListener(v -> onBackPressed());
        initViewModel();
        initRestaurantDetailUI();
        initWorkmatesUI();
        initFavUI();
        initUserDataUI();
        placeId = getIntent().getStringExtra(PLACE_ID);
    }


    private void initViewModel() {
        ViewModelFactory viewModelFactory = ViewModelFactory.getInstance();
        this.viewModel = new ViewModelProvider(this, viewModelFactory).get(RestaurantDetailViewModel.class);
        this.viewModel.init(getIntent().getStringExtra(PLACE_ID));
    }

    private void initRestaurantDetailUI() {
        viewModel.getRestaurantDetail().observe(this, restaurantInfo -> {
            Log.d(TAG, "initRestaurantDetailUI: restaurantInfo trigger");
            if (restaurantInfo != null) {
                if (restaurantInfo.getPhotos().get(0) != null) {
                    String photoRef = restaurantInfo.getPhotos().get(0).getPhotoReference();
                    Glide.with(binding.detailPicture.getContext())
                            .load("https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=" + photoRef + "&key=" + MainApplication.getApplication().getApplicationContext().getResources().getString(R.string.MAPS_API_KEY))
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
                    if (restaurantInfo.getFormattedPhoneNumber() != null || !Objects.equals(restaurantInfo.getFormattedPhoneNumber(), "")) {
                        Intent intent = new Intent(Intent.ACTION_DIAL,Uri.parse("tel:" + restaurantInfo.getFormattedPhoneNumber()));
                        try {
                            Log.d(TAG, "initRestaurantDetailUI: callIconClick");
                            startActivity(intent);
                        } catch (ActivityNotFoundException e) {
                            //
                            Log.d(TAG, "initRestaurantDetailUI: callIconClick", e);
                        }
                    }
                });
            }
            binding.webIcon.setOnClickListener(v -> {
                assert restaurantInfo != null;
                String website = restaurantInfo.getWebsite();
                if (website != null && !website.isEmpty()) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(website));
                    try {
                        Log.d(TAG, "initRestaurantDetailUI: webIconClick");
                        startActivity(intent);
                    } catch (ActivityNotFoundException e) {
                        Log.d(TAG, "initRestaurantDetailUI: webIconClick", e);
                    }
                }
            });
        });
        binding.favButton.setOnClickListener(v -> viewModel.onClickFav());
    }

    private void initWorkmatesUI() {
        RecyclerView recyclerView = binding.detailWorkmateList;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new WorkmateAdapter();
        recyclerView.setAdapter(adapter);
        viewModel.getListWorkmateLiveData().observe(this, workmates -> {
            Log.d(TAG, "initWorkmatesUI: workmates observer trigger");
            adapter.submitList(workmates);
        });
        binding.choseRestaurantButton.setOnClickListener(v -> {
            viewModel.onClickRestaurantChoice();
        });
    }

    private void initUserDataUI() {
        viewModel.getUserData().observe(this, uData -> {
            Log.d(TAG, "initUserDataUI: uData observer trigger");
            if (uData != null){
                if (uData.getPlaceId().equals(placeId)) {
                    binding.choseRestaurantButton.setImageResource(R.drawable.baseline_check_circle_24);
                } else {
                    binding.choseRestaurantButton.setImageResource(R.drawable.baseline_check_circle_outline_24);
                }
            }
        });
    }

    private void initFavUI() {
        viewModel.getUserFavList().observe(this, favRestaurants -> {
            Log.d(TAG, "initFavUI: favRestaurant observer trigger");
            if (favRestaurants != null){
                Log.d(TAG, "initFavUI: favList != null");
                if (!favRestaurants.isEmpty()) {
                    Log.d(TAG, "initFavUI: favlist != empty");
                    if (containSamePlaceId(favRestaurants)) {
                        Log.d(TAG, "initFavUI: baselineStar");
                        binding.favButton.setImageResource(R.drawable.baseline_star_24);
                    } else {
                        Log.d(TAG, "initFavUI: baseline_star_border");
                        binding.favButton.setImageResource(R.drawable.baseline_star_border_24);
                    }
                }else {
                    Log.d(TAG, "initFavUI: favlist is empty");
                    binding.favButton.setImageResource(R.drawable.baseline_star_border_24);
                }
            }
        });
    }

    private boolean containSamePlaceId(List<FavRestaurant> favRestaurants) {
        for (FavRestaurant fav : favRestaurants) {
            if (fav.getPlace_id().equals(placeId)) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy: remove observer");
        viewModel.getListWorkmateLiveData().removeObservers(this);
        viewModel.getRestaurantDetail().removeObservers(this);
        viewModel.getUserData().removeObservers(this);
        super.onDestroy();
    }
}
