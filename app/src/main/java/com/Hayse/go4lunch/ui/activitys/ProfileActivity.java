package com.Hayse.go4lunch.ui.activitys;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.Hayse.go4lunch.databinding.ActivityProfileBinding;
import com.Hayse.go4lunch.ui.adapter.FavRestaurantAdapter;
import com.Hayse.go4lunch.ui.fragments.RestaurantListFragment;
import com.Hayse.go4lunch.ui.viewmodel.ProfileViewModel;
import com.Hayse.go4lunch.ui.viewmodel.ViewModelFactory;

public class ProfileActivity extends AppCompatActivity {

    private final String TAG = "ProfileActivity";
    private Toolbar toolbar;
    private EditText nameField;
    private EditText emailField;
    private RecyclerView recyclerView;
    private ProfileViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        com.Hayse.go4lunch.databinding.ActivityProfileBinding binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setViewModel();
        this.toolbar = binding.toolbarProfil;
        this.nameField = binding.nameField;
        this.emailField = binding.emailField;
        ImageView avatarImage = binding.avatarImage;
        this.recyclerView = binding.favoriteRestaurantRecyclerView;
        ImageButton buttonSave = binding.saveButton;
        setupToolbar();
        setupTextFields();
        setupRecyclerView();
        avatarImage.setOnClickListener(v -> {
            Toast.makeText(this, "Not implemented", Toast.LENGTH_SHORT).show();
        });
        buttonSave.setOnClickListener(v -> onSaveButton());
    }

    public static Intent navigate(Context context) {
        return new Intent(context, ProfileActivity.class);
    }

    private void setViewModel(){
        ViewModelFactory viewModelFactory = ViewModelFactory.getInstance();
        this.viewModel = new ViewModelProvider(this, viewModelFactory).get(ProfileViewModel.class);
    }
    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        FavRestaurantAdapter adapter = new FavRestaurantAdapter();
        recyclerView.setAdapter(adapter);
        viewModel.getFavList().observe(this, favRestaurants -> {
            Log.d(TAG, "setupRecyclerView: favRestaurantsObserverTrigger");
            if (favRestaurants != null){
                if (favRestaurants.isEmpty()){
                    recyclerView.setVisibility(View.GONE);
                }else{
                    recyclerView.setVisibility(View.VISIBLE);
                    adapter.submitList(favRestaurants);
                }
            }
        });
        adapter.setOnFavRestaurantItemClickListener(favRestaurant -> {
            this.startActivity(RestaurantDetailActivity.navigate(
                    getApplicationContext(),
                    favRestaurant.getPlace_id()));
        });
        adapter.setOnDeleteBtnClickListener(favRestaurant -> {
            viewModel.removeFav(favRestaurant);
        });
    }

    private void setupTextFields() {
        nameField.setSingleLine();
        emailField.setSingleLine();
        viewModel.getUser().observe(this, user ->{
            Log.d(TAG, "setupTextFields: user observerTrigger");
            if (user!= null){
                nameField.setText(user.getName());
                emailField.setText(user.getEmail());
            }
        });
        nameField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                viewModel.setName(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Ignore
            }
        });
        emailField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                viewModel.setEmail(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Ignore
            }
        });
    }


    private void setupToolbar() {
        toolbar.setOnClickListener(v->onBackPressed());
        toolbar.setBackgroundColor(Color.RED);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setOnClickListener(v -> {
            onBackPressed();
        });
    }

    private void removeListener(){
        Log.d(TAG, "removeListener: remove observer");
        viewModel.getFavList().removeObservers(this);
        viewModel.getUser().removeObservers(this);
    }
    public void onSaveButton(){
         boolean canSave = viewModel.saveProfile(nameField.getText(), emailField.getText());
         if (canSave){
             removeListener();
             onBackPressed();
             Toast.makeText(this, "Profile saved", Toast.LENGTH_SHORT).show();
         }else{
             Toast.makeText(this, "Can't save profile yb", Toast.LENGTH_SHORT).show();
         }
    }
}
