package com.Hayse.go4lunch.ui.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.Hayse.go4lunch.R;
import com.Hayse.go4lunch.databinding.FragmentMapBinding;
import com.Hayse.go4lunch.services.permission_checker.PermissionChecker;
import com.Hayse.go4lunch.ui.activitys.RestaurantDetailActivity;
import com.Hayse.go4lunch.ui.view_state.HomeViewState;
import com.Hayse.go4lunch.ui.viewmodel.HomeRestaurantSharedViewModel;
import com.Hayse.go4lunch.ui.viewmodel.ViewModelFactory;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;
import java.util.Objects;

import io.reactivex.rxjava3.disposables.Disposable;

public class MapRestaurantFragment extends Fragment {

    private FragmentMapBinding binding;
    private HomeRestaurantSharedViewModel homeRestaurantSharedViewModel;

    private static final String TAG = MapRestaurantFragment.class.getSimpleName();
    private GoogleMap mapsView;

    SupportMapFragment supportMapFragment;

    int REQ_PERMISSION = 100;

    private final Disposable disposable = null;
    private Location lastKnownLocation;
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: ");
        getMapViewModel();
    }

    private void getMapViewModel() {
        homeRestaurantSharedViewModel = new ViewModelProvider(requireActivity(), ViewModelFactory.getInstance()).get(HomeRestaurantSharedViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentMapBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        initMap();
    }

    /**
     * if user already authorised location permission
     *
     * @return true
     * if it as bin given
     * else
     * @return false
     */
    private void getLocationPermission() {
        //request location permission
        if (ActivityCompat.checkSelfPermission(this.requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this.requireActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQ_PERMISSION);
            if (ActivityCompat.checkSelfPermission(this.requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "getLocationPermission: return true2");
            } else {
                Log.d(TAG, "getLocationPermission: return false");
            }
        } else {
            Log.d(TAG, "getLocationPermission: return true1");
        }
    }

    private void initMap() {
        Log.d(TAG, "initMap: ");
        GoogleMapOptions mapOptions = new GoogleMapOptions();
        mapOptions.mapType(GoogleMap.MAP_TYPE_NORMAL)
                .compassEnabled(true)
                .rotateGesturesEnabled(true)
                .tiltGesturesEnabled(true);
        this.supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map_view);
        //requireActivity().getSupportFragmentManager().beginTransaction().add(R.id.fragment_container_view, supportMapFragment).commit();

        this.supportMapFragment.getMapAsync(googleMap -> {
            Log.d(TAG, "onMapReady: ");
            mapsView = googleMap;
            PermissionChecker permissionChecker = new PermissionChecker(getActivity().getApplication());
            if (permissionChecker.hasLocationPermission()) {
                Log.d(TAG, "onMapReady: location permission guaranteed");
                subscribeToObservables();
            }
        });
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        if (mapsView != null) {
            outState.putParcelable(KEY_CAMERA_POSITION, mapsView.getCameraPosition());
            outState.putParcelable(KEY_LOCATION, lastKnownLocation);
        }
        super.onSaveInstanceState(outState);
    }


    private void subscribeToObservables() {

        homeRestaurantSharedViewModel.getHomeViewStateLiveData().observe(getViewLifecycleOwner(), homeWrapperViewState -> {
            updateUserLocation(homeWrapperViewState.getLocation());
            updateUi(homeWrapperViewState.getHomeViewState());
        });
        homeRestaurantSharedViewModel.getPrediction().observe(getViewLifecycleOwner(), place -> {
            Log.d(TAG, "subscribeToObservables: ");
            if (place != null) {
                Log.d(TAG, "subscribeToObservables: " + place);
                if (place.getLatLng() != null) {
                    //mapsView.addMarker();
                    mapsView.moveCamera(CameraUpdateFactory.newLatLng(place.getLatLng()));
                    mapsView.animateCamera(CameraUpdateFactory.newLatLngZoom(place.getLatLng(), 15));
                    MarkerOptions markerOpt = new MarkerOptions()
                            .position(place.getLatLng())
                            .title(place.getName())
                            .snippet(place.getAddress());
                    Marker marker = mapsView.addMarker(markerOpt);
                    if (marker != null) {
                        marker.showInfoWindow();
                    }
                    if (marker != null) {
                        marker.setTag(place.getId());
                    }
                    mapsView.setOnInfoWindowClickListener(info -> requireContext().startActivity(RestaurantDetailActivity.navigate(
                            requireContext(),
                            (String) info.getTag())));
                }
            } else {
                Log.d(TAG, "subscribeToObservables: autocomplete place is null");
            }
        });
    }

    private void updateUi(List<HomeViewState> itemList) {
        if (itemList != null) {
            if (!itemList.isEmpty()) {
                for (HomeViewState item : itemList) {
                    Log.d(TAG, "displayMarker: result : " + item.getRestaurantName());

                    LatLng latLngMarker = new LatLng(
                            (item.getGeometry()
                                    .getLocation()
                                    .getLat()),
                            (item.getGeometry()
                                    .getLocation()
                                    .getLng()));
                    MarkerOptions markerOpt = new MarkerOptions()
                            .position(latLngMarker)
                            .title(item.getRestaurantName())
                            .snippet(item.getAddress());
                    //get LatLong to Marker
                    BitmapDescriptor icon;
                    if (item.getWorkmateNumber() > 0) {
                        icon = getBitmapDescriptor(R.drawable.map_marker_icon_green);
                    } else {
                        icon = getBitmapDescriptor(R.drawable.map_marker_icon_red);
                    }
                    markerOpt.icon(icon);
                    Marker marker = mapsView.addMarker(markerOpt);
                    assert marker!=null;
                            marker.setTag(item.getPlaceId());
                    mapsView.setOnMarkerClickListener(m -> {
                        m.showInfoWindow();
                        return m.isInfoWindowShown();
                    });
                }
            }
        }
        mapsView.setOnInfoWindowClickListener(marker -> requireContext().startActivity(RestaurantDetailActivity.navigate(
                requireContext(),
                (String) marker.getTag())));
    }

    private void infoWindowClick(Marker marker, HomeViewState item){
        if (marker.isInfoWindowShown()){
            if (item!= null){
                MapRestaurantFragment.this.startActivity(RestaurantDetailActivity.navigate(
                        MapRestaurantFragment.this.getContext(),
                        item.getPlaceId()));
            }
        }else {
            Log.d(TAG, "infoWindowClick: infoWindow hiden");
        }
    }

    private BitmapDescriptor getBitmapDescriptor(int id) {
        @SuppressLint("UseCompatLoadingForDrawables") Drawable vectorDrawable = requireContext().getDrawable(id);
        int h = 108;
        int w = 108;
        vectorDrawable.setBounds(0, 0, w, h);
        Bitmap bm = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bm);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bm);
    }

    @SuppressLint("MissingPermission")
    private void updateUserLocation(Location location) {
        if (location != null) {
            try {
                boolean permissionDenied = false;
                if (!permissionDenied) {
                    mapsView.setMyLocationEnabled(true);
                    mapsView.getUiSettings().setMyLocationButtonEnabled(true);
                    mapsView.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(),location.getLongitude())));
                    mapsView.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(),location.getLongitude()), 15));
                    return;
                } else {
                    Log.d(TAG, "updateLocationUI: permission denied");
                    lastKnownLocation = null;
                    getLocationPermission();
                }
            } catch (SecurityException e) {
                Log.e("Exception: %s", e.getMessage());
            }
        }else {
            Toast.makeText(getContext(), getString(R.string.enable_location_in_settings),Toast.LENGTH_SHORT).show();
        }
    }

    private void unsubscribeToObservables() {
        // homeRestaurantSharedViewModel.getLocationLiveData().removeObservers(this);
        homeRestaurantSharedViewModel.getHomeViewStateLiveData().removeObservers(getViewLifecycleOwner());
        homeRestaurantSharedViewModel.getPrediction().removeObservers(getViewLifecycleOwner());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unsubscribeToObservables();
        binding = null;
    }

    @Override
    public void onStop() {
        super.onStop();
        unsubscribeToObservables();
    }
}