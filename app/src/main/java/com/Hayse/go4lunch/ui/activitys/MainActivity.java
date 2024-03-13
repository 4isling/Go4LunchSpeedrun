package com.Hayse.go4lunch.ui.activitys;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.Hayse.go4lunch.MainApplication;
import com.Hayse.go4lunch.R;
import com.Hayse.go4lunch.databinding.ActivityMainBinding;
import com.Hayse.go4lunch.services.permission_checker.PermissionUtils;
import com.Hayse.go4lunch.ui.fragments.MapRestaurantFragment;
import com.Hayse.go4lunch.ui.fragments.RestaurantListFragment;
import com.Hayse.go4lunch.ui.fragments.WorkmateFragment;
import com.Hayse.go4lunch.ui.viewmodel.HomeRestaurantSharedViewModel;
import com.Hayse.go4lunch.ui.viewmodel.ViewModelFactory;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "MainActivity";
    private static final int ERROR_DIALOG_REQUEST = 9001;
    private ActivityMainBinding binding;
    private Toolbar toolbar;
    private HomeRestaurantSharedViewModel homeRestaurantSharedViewModel;
    private WorkmateFragment workmateFragment;

    private MapRestaurantFragment mapFragment;

    private RestaurantListFragment listFragment;
    private DrawerLayout drawerLayout;
    AutocompleteSupportFragment autocompleteSupportFragment;

    private static final int AUTOCOMPLETE_REQUEST_CODE = 147;
    ActivityResultLauncher<Intent> startAutocomplete = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent rIntent = result.getData();
                    if (rIntent != null) {
                        Place place = Autocomplete.getPlaceFromIntent(rIntent);
                        homeRestaurantSharedViewModel.onPredictionClick(place);
                        homeRestaurantSharedViewModel.onPredictionClick(null);
                        Log.i(TAG, "Place: ${place.getName()}, ${place.getId()}");
                    }
                } else if (result.getResultCode() == Activity.RESULT_CANCELED) {
                    // The user canceled the operation.
                    Log.i(TAG, "User canceled autocomplete");
                }
            });



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String API_KEY = MainApplication.getApplication().getResources().getString(R.string.MAPS_API_KEY);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        this.initViewModel();
        this.createFragments();
        this.configureToolbar();
        this.configureBottomNavView();
        this.configureDrawerLayout();
        this.configureDrawerNavView();
        if (ContextCompat.checkSelfPermission(MainApplication.getApplication(), ACCESS_FINE_LOCATION) == PERMISSION_GRANTED) {
            Log.d(TAG, "onCreate: location permission granted");
            if (isGoogleServiceOK()) {
                replaceFragment(mapFragment);
                if (!Places.isInitialized()) {
                    Places.initialize(getApplicationContext(), API_KEY);
                }
                this.initUserDataUI();
            }
        } else {
            Log.d(TAG, "onCreate: location permission disable");
            PermissionUtils.requestPermission(this, 1,
                    Manifest.permission.ACCESS_FINE_LOCATION, false);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (PermissionUtils.isPermissionGranted(permissions, grantResults,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                // Permission granted, proceed with location-based functionality
                homeRestaurantSharedViewModel.startLocationRequest();
                recreate();
            } else {
                // Permission denied, display a dialog explaining the rationale for the permission
                PermissionUtils.PermissionDeniedDialog.newInstance(true).show(
                        getSupportFragmentManager(), "dialog");
                // Add error handling here
            }
        }
    }

    private boolean isGoogleServiceOK() {
        Log.d(TAG, "isServicesOK: checking google services version");
        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(MainActivity.this);
        if (available == ConnectionResult.SUCCESS) {
            //user can make map requests
            Log.d(TAG, "isServicesOK: Google Play services is working");

            return true;
        } else if (GoogleApiAvailability.getInstance().isUserResolvableError(available)) {
            Log.d(TAG, "isServicesOK: an error occured but can be fixed");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(MainActivity.this, available, ERROR_DIALOG_REQUEST);
            assert dialog != null;
            dialog.show();
        } else {
            Toast.makeText(this, "You can't make map request", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    private void initViewModel() {
        Log.d(TAG, "initViewModel: ");
        ViewModelFactory viewModelFactory = ViewModelFactory.getInstance();
        this.homeRestaurantSharedViewModel = new ViewModelProvider(this, viewModelFactory).get(HomeRestaurantSharedViewModel.class);
    }

    private void initUserDataUI() {
        View header = binding.mainDrawerNavView.getHeaderView(0);
        ImageView userAvatar = header.findViewById(R.id.avatar_user_header);
        TextView userName = header.findViewById(R.id.user_name_header);
        TextView userEmail = header.findViewById(R.id.email_user_header);
        homeRestaurantSharedViewModel.getUserData().observe(this, userData -> {
            if (userData != null) {
                if (!Objects.equals(userData.getAvatarUrl(), "")) {
                    Glide.with(this)
                            .load(userData.getAvatarUrl())
                            .apply(RequestOptions.circleCropTransform())
                            .into(userAvatar);
                }
                if (userData.getName() != null) {
                    userName.setText(userData.getName());
                }
                if (userData.getEmail() != null) {
                    userEmail.setText(userData.getEmail());
                }
            }
        });
    }

    private void createFragments() {
        Log.d(TAG, "createFragments: ");
        this.mapFragment = new MapRestaurantFragment();
        this.listFragment = new RestaurantListFragment();
        this.workmateFragment = new WorkmateFragment();
    }

    private void configureToolbar() {
        Log.d(TAG, "configureToolbar: ");
        this.toolbar = binding.activityMainToolbar;
        binding.activityMainToolbar.setNavigationIcon(R.drawable.baseline_menu_24);
        binding.activityMainToolbarSearchIcon.setClickable(true);
        binding.activityMainToolbarSearchIcon.setOnClickListener(v -> {
            homeRestaurantSharedViewModel.getLocationLiveData().observe(this, location -> {
                if (location != null) {

                    this.autocompleteSupportFragment = (AutocompleteSupportFragment)
                            getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);
                    List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG);
                    // Start the autocomplete intent.
                    Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields)
                            .setLocationBias(defineRectangularBounds(location))
                            .setTypesFilter(Collections.singletonList("restaurant"))
                            .build(this);

                    startAutocomplete.launch(intent);
                }
            });
        });
        setSupportActionBar(binding.activityMainToolbar);
    }


    private RectangularBounds defineRectangularBounds(Location location) {
        final double LAT_LNG_OFFSET = 0.5; // Define as constant
        LatLng northEast = new LatLng(location.getLatitude() - LAT_LNG_OFFSET, location.getLongitude() - LAT_LNG_OFFSET);
        LatLng southWest = new LatLng(location.getLatitude() + LAT_LNG_OFFSET, location.getLongitude() + LAT_LNG_OFFSET);
        return RectangularBounds.newInstance(northEast, southWest);
    }

    private void configureDrawerLayout() {
        Log.d(TAG, "configureDrawerLayout: ");
        this.drawerLayout = binding.drawerLayout;
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav_drawer, R.string.close_nav_drawer);
        this.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    private void configureDrawerNavView() {
        Log.d(TAG, "configureDrawerNavView: ");
        NavigationView drawerNavView = binding.mainDrawerNavView;
        drawerNavView.setNavigationItemSelectedListener(this);
    }

    private void configureBottomNavView() {
        Log.d(TAG, "configureBottomNavView: ");
        binding.bottomNavView.setItemIconTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.bottom_navigation_item_colors));
        binding.bottomNavView.setItemTextColor(ContextCompat.getColorStateList(getApplicationContext(), R.color.bottom_navigation_item_colors));
        binding.bottomNavView.setOnItemSelectedListener(item -> {
            int pItem = item.getItemId();
            if (pItem == R.id.navigation_map) {
                Log.d(TAG, "configureBottomNavView: map");
                replaceFragment(mapFragment);
                return true;
            } else if (pItem == R.id.navigation_restaurant) {
                Log.d(TAG, "configureBottomNavView: list");

                replaceFragment(listFragment);
                return true;
            } else if (pItem == R.id.navigation_workmate) {

                Log.d(TAG, "configureBottomNavView: workmates");
                replaceFragment(workmateFragment);

                return true;
            }
            return false;
        });
    }

    private void replaceFragment(Fragment fragment) {
        Log.d(TAG, "replaceFragment: " + fragment);
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment currentFragment = fragmentManager.findFragmentById(R.id.activity_main_frame_layout);

        if (currentFragment instanceof MapRestaurantFragment && fragment instanceof MapRestaurantFragment ||
                currentFragment instanceof RestaurantListFragment && fragment instanceof RestaurantListFragment ||
                currentFragment instanceof WorkmateFragment && fragment instanceof WorkmateFragment) {
            return;
        }

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.activity_main_frame_layout, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        if (this.binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            this.binding.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            finish();
        }
    }

    private void showLogOutDialog() {
        Log.d(TAG, "showLogoutDialog: ");
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.exit_application))
                .setMessage(getString(R.string.sure_logout))
                .setPositiveButton(getString(R.string.yes), (dialog, which) -> {
                    FirebaseAuth.getInstance().signOut();
                    finishAffinity(); // closes the entire application
                })
                .setNegativeButton(getString(R.string.no), (dialog, which) -> dialog.dismiss())
                .show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                Log.i(TAG, "Place: " + place.getName() + ", " + place.getId());
                homeRestaurantSharedViewModel.onPredictionClick(place);
                homeRestaurantSharedViewModel.onPredictionClick(null);
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                Status status = Autocomplete.getStatusFromIntent(data);
                Log.i(TAG, status.getStatusMessage());
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.your_lunch) {
            homeRestaurantSharedViewModel.getUserData().observe(this, userData -> {
                if (userData.getPlaceId() != null && !userData.getPlaceId().equals("")) {
                    this.startActivity(RestaurantDetailActivity.navigate(getApplicationContext(), userData.getPlaceId()));
                } else {
                    Toast.makeText(getApplicationContext(), getString(R.string.no_restaurant_your_lunch), Toast.LENGTH_SHORT).show();
                }
            });
            return true;
        } else if (id == R.id.settings) {
            this.startActivity(SettingActivity.navigate(getApplicationContext()));
            return true;
        } else if (id == R.id.logout) {
            this.showLogOutDialog();
            return true;
        }
        this.drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}