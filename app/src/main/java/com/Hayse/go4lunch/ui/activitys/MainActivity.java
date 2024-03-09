package com.Hayse.go4lunch.ui.activitys;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.PointerIcon;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.ui.AppBarConfiguration;

import com.Hayse.go4lunch.MainApplication;
import com.Hayse.go4lunch.R;
import com.Hayse.go4lunch.databinding.ActivityMainBinding;
import com.Hayse.go4lunch.domain.entites.Workmate;
import com.Hayse.go4lunch.ui.fragments.MapRestaurantFragment;
import com.Hayse.go4lunch.ui.fragments.RestaurantListFragment;
import com.Hayse.go4lunch.ui.fragments.WorkmateFragment;
import com.Hayse.go4lunch.ui.viewmodel.HomeRestaurantSharedViewModel;
import com.Hayse.go4lunch.ui.viewmodel.ViewModelFactory;
import com.Hayse.go4lunch.ui.viewmodel.WorkmateViewModel;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static GoogleMap mGoogleMap;
    private static final String TAG = "MainActivity";
    private static final int ERROR_DIALOG_REQUEST = 9001;
    private ActivityMainBinding binding;
    private NavigationView drawerNavView;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private AppBarConfiguration appBarConfig;

    private LiveData<Workmate> userData;

    boolean mGPSEnable;
    private ViewModelFactory viewModelFactory;

    private WorkmateViewModel workmateViewModel;

    private HomeRestaurantSharedViewModel homeRestaurantSharedViewModel;
    private WorkmateFragment workmateFragment;

    private MapRestaurantFragment mapFragment;

    private RestaurantListFragment listFragment;

    private PlacesClient placesClient;

    AutocompleteSupportFragment autocompleteSupportFragment;

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
        if (checkUserLocationPermission()) {
            if (isGoogleServiceOK()) {
                replaceFragment(mapFragment);
                //init place
                if (!Places.isInitialized()){
                    Places.initialize(getApplicationContext(), API_KEY);
                    placesClient = Places.createClient(this);
                    this.configureAutocompleteFragment();
                }
                this.initUserDataUI();
            }
        }
    }

    private boolean checkUserLocationPermission() {
        Log.d(TAG, "checkUserLocationPermission: ");
        LocationManager mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mGPSEnable = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!mGPSEnable) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    0
            );
            mGPSEnable = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        }
        return mGPSEnable;
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
        this.viewModelFactory = ViewModelFactory.getInstance();
        this.homeRestaurantSharedViewModel = new ViewModelProvider(this, viewModelFactory).get(HomeRestaurantSharedViewModel.class);
        this.workmateViewModel = new ViewModelProvider(this, viewModelFactory).get(WorkmateViewModel.class);
    }

    private void initUserDataUI() {
        View header = binding.mainDrawerNavView.getHeaderView(0);
        ImageView userAvatar = header.findViewById(R.id.avatar_user_header);
        TextView userName = header.findViewById(R.id.user_name_header);
        TextView userEmail = header.findViewById(R.id.email_user_header);
        homeRestaurantSharedViewModel.getUserData().observe(this, userData ->{
            if (userData != null){
                if (!Objects.equals(userData.getAvatarUrl(), "")){
                    Glide.with(this)
                            .load(userData.getAvatarUrl())
                            .apply(RequestOptions.circleCropTransform())
                            .into(userAvatar);
                }
                if (userData.getName()!=null){
                    userName.setText(userData.getName());
                }
                if (userData.getEmail()!= null){
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
            //@todo autocomplete integration
            if (autocompleteSupportFragment.isVisible()){
                autocompleteSupportFragment.setActivityMode(AutocompleteActivityMode.OVERLAY);
                autocompleteSupportFragment.getActivity();
            }else {
                autocompleteSupportFragment.onDestroy();
            }

        });
        setSupportActionBar(binding.activityMainToolbar);
    }

    /**
     * AutocompleteSupportFragment embedding
     */
    private void configureAutocompleteFragment(){
        List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG);
        // Start the autocomplete intent.
        Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields)
                .build(this);

        //Autocomplete Support fragment
        autocompleteSupportFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        autocompleteSupportFragment.setPlaceFields(fields);
        autocompleteSupportFragment.setTypesFilter(Arrays.asList("restaurant"));
        homeRestaurantSharedViewModel.getLocationLiveData().observe(this, location -> {
            if (location != null){
                //@todo verif si ça marche
                autocompleteSupportFragment.setLocationRestriction(defineRectangularBounds(location));

            }
        });
        // @todo Set up a PlaceSelectionListener to handle the response.
        autocompleteSupportFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                homeRestaurantSharedViewModel.onPredictionClick(place);
                Log.i(TAG, "Place: " + place.getName() + ", " + place.getId());
                autocompleteSupportFragment.onDestroy();
            }

            @Override
            public void onError(@NonNull Status status) {
                // TODO: Handle the error.
                Log.e(TAG, "An error occurred: " + status);
            }
        });
        autocompleteSupportFragment.setMenuVisibility(false);
    }

    private RectangularBounds defineRectangularBounds(Location location) {

        LatLng northEast = new LatLng(location.getLatitude()-0.5, location.getLongitude()-0.5);
        LatLng southWest = new LatLng(location.getLatitude()+0.5, location.getLongitude()+0.5);

        return RectangularBounds.newInstance(northEast,southWest);
    }

    public void onSearchCalled() {
        // Set the fields to specify which types of place data to return.
        List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG);
        // Start the autocomplete intent.
        Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields)
                .build(this);
    }

    private void configureDrawerLayout() {
        Log.d(TAG, "configureDrawerLayout: ");
        DrawerLayout drawerLayout = binding.drawerLayout;
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav_drawer, R.string.close_nav_drawer);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    private void configureDrawerNavView() {
        Log.d(TAG, "configureDrawerNavView: ");
        this.drawerNavView = binding.mainDrawerNavView;
        drawerNavView.setNavigationItemSelectedListener(this);
    }

    private void configureBottomNavView() {
        Log.d(TAG, "configureBottomNavView: ");
        /*
        View navMap = findViewById(R.id.navigation_map);
        View navList = findViewById(R.id.navigation_restaurant);
        View navWorkmate = findViewById(R.id.navigation_workmate);
        appBarConfig = new AppBarConfiguration.Builder(
                R.id.navigation_map,
                R.id.navigation_restaurant,
                R.id.navigation_workmate)
                .build();*/
        binding.bottomNavView.setItemIconTintList(ContextCompat.getColorStateList(getApplicationContext(),R.color.bottom_navigation_item_colors));
        binding.bottomNavView.setItemTextColor(ContextCompat.getColorStateList(getApplicationContext(),R.color.bottom_navigation_item_colors));
        binding.bottomNavView.setOnItemSelectedListener(item -> {
            int pItem = item.getItemId();
            if (pItem == R.id.navigation_map) {
                Log.d(TAG, "configureBottomNavView: map");

                replaceFragment(mapFragment);
                return true;
            } else if (pItem == R.id.navigation_restaurant) {
                Log.d(TAG, "configureBottomNavView: list");

                replaceFragment(listFragment);
                Toast.makeText(this, "not implemented yet", Toast.LENGTH_SHORT);
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

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.your_lunch) {
            homeRestaurantSharedViewModel.getUserData().observe(this, userData ->{
                if (userData.getPlaceId() != null){
                    this.startActivity(RestaurantDetailActivity.navigate(getApplicationContext(),userData.getPlaceId()));
                }else{
                    Toast.makeText(getApplicationContext(), getString(R.string.no_restaurant_your_lunch), Toast.LENGTH_SHORT);
                }
            });
            return true;
        } else if (id == R.id.settings) {
            final Intent intentSetting = new Intent(this, SettingActivity.class);
            startActivity(intentSetting);
            return true;
        } else if (id == R.id.logout) {
            return true;
        }
        this.drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}
