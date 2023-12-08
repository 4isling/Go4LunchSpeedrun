package com.Hayse.go4lunch.ui.activitys;

import android.Manifest;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.Hayse.go4lunch.R;
import com.Hayse.go4lunch.databinding.ActivityMainBinding;
import com.Hayse.go4lunch.ui.fragments.MapRestaurantFragment;
import com.Hayse.go4lunch.ui.fragments.WorkmateFragment;
import com.Hayse.go4lunch.ui.viewmodel.ViewModelFactory;
import com.Hayse.go4lunch.ui.viewmodel.WorkmateViewModel;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "MainActivity";
    private static final int ERROR_DIALOG_REQUEST = 9001;
    private ActivityMainBinding binding;
    private NavigationView drawerNavView;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private ViewModelFactory viewModelFactory;

    private WorkmateViewModel workmateViewModel;
    private WorkmateFragment workmateFragment;

    private MapRestaurantFragment mapFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        this.checkUserLocationPermission();
        if (this.isGoogleServiceOK()) {
            this.initViewModel();
            this.createFragments();
            this.configureToolbar();
            this.configureBottomNavView();
            this.configureDrawerLayout();
            this.configureDrawerNavView();
        }
    }

    private void checkUserLocationPermission() {
        Log.d(TAG, "checkUserLocationPermission: ");
        ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                0
        );
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
        workmateViewModel = new ViewModelProvider(this, viewModelFactory).get(WorkmateViewModel.class);
    }



    private void createFragments() {
        Log.d(TAG, "createFragments: ");
        this.workmateFragment = new WorkmateFragment();
        this.mapFragment = new MapRestaurantFragment();
    }

    private void configureToolbar() {
        Log.d(TAG, "configureToolbar: ");
        this.toolbar = binding.activityMainToolbar;
        binding.activityMainToolbar.setNavigationIcon(R.drawable.baseline_list_24);
        setSupportActionBar(binding.activityMainToolbar);
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
        binding.bottomNavView.setOnItemSelectedListener(item -> {
            int pItem = item.getItemId();
            if (pItem == R.id.navigation_map) {
                    replaceFragment(mapFragment);
                Log.d(TAG, "configureBottomNavView: map");
                return true;
            } else if (pItem == R.id.navigation_restaurant) {
//                    replaceFragment(restaurantListFragment);
                Toast.makeText(this, "not implemented yet", Toast.LENGTH_SHORT);
                return true;
            }
            else if (pItem == R.id.navigation_workmate) {
                replaceFragment(workmateFragment);
                Log.d(TAG, "configureBottomNavView: workmates");
                return true;
            }

            return false;
        });
}

    private void replaceFragment(Fragment fragment) {
        Log.d(TAG, "replaceFragment: "+ fragment);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.activity_main_frame_layout, fragment);
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

        if(id == R.id.your_lunch ) {
            return true;
        }
        else if (id== R.id.settings) {
            return true;
        }
        else if (id == R.id.logout ) {
            return true;
        }
        this.drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}
