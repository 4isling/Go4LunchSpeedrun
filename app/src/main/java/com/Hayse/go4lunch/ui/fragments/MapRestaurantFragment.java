package com.Hayse.go4lunch.ui.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.Hayse.go4lunch.R;
import com.Hayse.go4lunch.databinding.FragmentMapBinding;
import com.Hayse.go4lunch.domain.entites.map_api.nerbysearch.Result;
import com.Hayse.go4lunch.services.permission_checker.PermissionUtils;
import com.Hayse.go4lunch.ui.viewmodel.MapViewModel;
import com.Hayse.go4lunch.ui.viewmodel.ViewModelFactory;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;
import java.util.Objects;

public class MapRestaurantFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMyLocationClickListener {

    private FragmentMapBinding binding;
    private MapViewModel mapViewModel;

    private static final String TAG = MapRestaurantFragment.class.getSimpleName();
    private GoogleMap mapsView;

    private GoogleMapOptions mapOptions;

    SupportMapFragment supportMapFragment;

    // The entry point to the Fused Location Provider.
    private FusedLocationProviderClient fusedLocationProviderClient;

    private final LatLng defaultLocation = new LatLng(48.888053, 2.343312);
    private static final int DEFAULT_ZOOM = 15;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 999;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    private boolean permissionDenied = false;
    int REQ_PERMISSION = 100;


    // The geographical location where the device is currently located. That is, the last-known
    // location retrieved by the Fused Location Provider.
    private Location lastKnownLocation;

    //Keys for storing activity state
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: ");
        binding = FragmentMapBinding.inflate(getLayoutInflater());
        View root = binding.getRoot();
        if(getLocationPermission()) {
            this.mapOptions = new GoogleMapOptions();
            initMap();
            this.fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
        }
    }

    /**
     * if user already authorised location permission
     * @return true
     * else ask user permission and
     * @return true
     * if it as bin given
     * else
     * @return false
     */
    private boolean getLocationPermission() {
        //request location permission
        if (ActivityCompat.checkSelfPermission(this.getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this.getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQ_PERMISSION);
            if (ActivityCompat.checkSelfPermission(this.getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                Log.d(TAG, "getLocationPermission: return true2");
                return true;
            }else {
                Log.d(TAG, "getLocationPermission: return false");
                return false;
            }
        }else {
            Log.d(TAG, "getLocationPermission: return true1");
            return true;
        }
    }

    private void initMap() {
        Log.d(TAG, "initMap: ");
        this.mapOptions = new GoogleMapOptions();
        this.mapOptions.mapType(GoogleMap.MAP_TYPE_NORMAL)
                .compassEnabled(true)
                .rotateGesturesEnabled(true)
                .tiltGesturesEnabled(true);

        this.supportMapFragment = SupportMapFragment.newInstance(mapOptions);
        requireActivity().getSupportFragmentManager().beginTransaction().add(R.id.fragment_container_view,supportMapFragment).commit();
        if (supportMapFragment != null) {

            Log.d(TAG, "initMap: supportMapFragment != null");
            (supportMapFragment).getMapAsync(this);
            this.mapOptions.mapType(GoogleMap.MAP_TYPE_NORMAL)
                    .compassEnabled(true)
                    .rotateGesturesEnabled(true)
                    .tiltGesturesEnabled(true);
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        Log.d(TAG, "onMapReady: ");
        this.mapsView = googleMap;
        mapsView.setOnMyLocationButtonClickListener(this);
        mapsView.setOnMyLocationClickListener(this);
        enableMyLocation();
    }

    /**
     * Enables the My Location layer if the fine location permission has been granted.
     */
    @SuppressLint("MissingPermission")
    private void enableMyLocation() {
        // [START maps_check_location_permission]
        Log.d(TAG, "enableMyLocation: ");
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "enableMyLocation: access user location enable");
            if (mapsView != null) {
                Log.d(TAG, "enableMyLocation: mapView != null");
                mapsView.setMyLocationEnabled(true);
            }
        } else {
            Log.d(TAG, "enableMyLocation: location permission not enable");
            // Permission to access the location is missing. Show rationale and request permission
            PermissionUtils.requestPermission((AppCompatActivity) getActivity(), LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
        }
        // [END maps_check_location_permission]
        updateLocationUI();
    }


    /**
     * move camera on user
     * @return
     */
    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(getContext(), "MyLocation button clicked", Toast.LENGTH_SHORT).show();
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        return false;
    }


    @Override
    public void onMyLocationClick(@NonNull Location location) {
        Toast.makeText(getContext(), "Current location:\n" + location, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
    }



    @Override
    public void onResume() {
        super.onResume();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void getMapViewModel() {
        mapViewModel = new ViewModelProvider(this, ViewModelFactory.getInstance()).get(MapViewModel.class);
    }

    private void setUpMarker() {
        mapViewModel.getRestaurantMapViewStateLiveData().observe(getViewLifecycleOwner(), restaurantMapViewState -> {
            displayMarker(restaurantMapViewState.getRestaurantMapResult());
        });
    }

    private void displayMarker(List<Result> results) {

        for (Result result : results) {
            LatLng latLngMarker = new LatLng(
                    (result.getGeometry()
                            .getLocation()
                            .getLat()),
                    (result.getGeometry()
                            .getLocation()
                            .getLng()));
            //get LatLong to Marker
            mapsView.addMarker(new MarkerOptions()
                    .position(latLngMarker)
                    .icon(BitmapDescriptorFactory.defaultMarker())
                    .title(result.getName()));
            //show marker
            LatLng latLngResult = new LatLng(
                    (result.getGeometry()
                            .getLocation()
                            .getLat()),
                    (result.getGeometry()
                            .getLocation()
                            .getLng()));
            //set position marker
            mapsView.moveCamera(CameraUpdateFactory.newLatLng(latLngResult));
            mapsView.animateCamera(CameraUpdateFactory.newLatLngZoom(latLngResult, 14));
            mapsView.getUiSettings().setAllGesturesEnabled(true);
            mapsView.getUiSettings().setZoomGesturesEnabled(true);
        }
    }



    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        this.permissionDenied = true;
        if (requestCode == PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {
            // If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                permissionDenied = false;
                enableMyLocation();
            } else {
                Toast.makeText(getContext(), "Permission not granted", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (mapsView != null) {
            outState.putParcelable(KEY_CAMERA_POSITION, mapsView.getCameraPosition());
            outState.putParcelable(KEY_LOCATION, lastKnownLocation);
        }
        super.onSaveInstanceState(outState);
    }



    /**
     * Updates the map's UI settings based on whether the user has granted location permission.
     */
    // [START maps_current_place_update_location_ui]

    @SuppressLint("MissingPermission")
    private void updateLocationUI() {
        if (mapsView == null) {
            Log.d(TAG, "updateLocationUI: mapsView == null");
            return;
        }
        try {
            if (!permissionDenied) {
                Log.d(TAG, "updateLocationUI: permission guaranteed");
                mapsView.setMyLocationEnabled(true);
                mapsView.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                Log.d(TAG, "updateLocationUI: permission denied");
                mapsView.setMyLocationEnabled(false);
                mapsView.getUiSettings().setMyLocationButtonEnabled(false);
                lastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }
}