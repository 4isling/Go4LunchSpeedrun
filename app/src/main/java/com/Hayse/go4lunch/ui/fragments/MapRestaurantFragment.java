package com.Hayse.go4lunch.ui.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.Hayse.go4lunch.R;
import com.Hayse.go4lunch.databinding.FragmentMapBinding;
import com.Hayse.go4lunch.domain.entites.Workmate;
import com.Hayse.go4lunch.domain.entites.map_api.nerbysearch.Result;
import com.Hayse.go4lunch.services.permission_checker.PermissionChecker;
import com.Hayse.go4lunch.ui.activitys.RestaurantDetailActivity;
import com.Hayse.go4lunch.ui.view_state.HomeViewState;
import com.Hayse.go4lunch.ui.view_state.HomeWrapperViewState;
import com.Hayse.go4lunch.ui.viewmodel.HomeRestaurantSharedViewModel;
import com.Hayse.go4lunch.ui.viewmodel.ViewModelFactory;
import com.Hayse.go4lunch.util.SvgToBitmap;
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

import io.reactivex.rxjava3.disposables.Disposable;

public class MapRestaurantFragment extends Fragment {

    private FragmentMapBinding binding;
    private HomeRestaurantSharedViewModel homeRestaurantSharedViewModel;

    private static final String TAG = MapRestaurantFragment.class.getSimpleName();
    private GoogleMap mapsView;

    private GoogleMapOptions mapOptions;

    SupportMapFragment supportMapFragment;


    private final boolean permissionDenied = false;
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
                    && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "getLocationPermission: return true2");
                return true;
            } else {
                Log.d(TAG, "getLocationPermission: return false");
                return false;
            }
        } else {
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
        this.supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map_view);
        //requireActivity().getSupportFragmentManager().beginTransaction().add(R.id.fragment_container_view, supportMapFragment).commit();

        this.supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @SuppressLint("MissingPermission")
            @Override
            public void onMapReady(@NonNull GoogleMap googleMap) {
                Log.d(TAG, "onMapReady: ");
                mapsView = googleMap;
                PermissionChecker permissionChecker = new PermissionChecker(getActivity().getApplication());
                if (permissionChecker.hasLocationPermission()) {
                    Log.d(TAG, "onMapReady: location permission guaranteed");
                    subscribeToObservables();
                }
            }
        });
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
    private void updateLocationUI(List<Result> resultList) {
        if (mapsView == null) {
            Log.d(TAG, "updateLocationUI: mapsView == null");
            return;
        }
        try {
            if (!permissionDenied) {
                Log.d(TAG, "updateLocationUI: permission guaranteed");
                mapsView.setMyLocationEnabled(true);
                mapsView.getUiSettings().setMyLocationButtonEnabled(true);
                if (resultList != null) {
                    displayMarker(resultList);
                }

            } else {
                Log.d(TAG, "updateLocationUI: permission denied");
                lastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    private void displayMarker(@NonNull List<Result> results) {
        Log.d(TAG, "displayMarker: ");
        for (Result result : results) {
            Log.d(TAG, "displayMarker: result : " + result.getName());
            LatLng latLngMarker = new LatLng(
                    (result.getGeometry()
                            .getLocation()
                            .getLat()),
                    (result.getGeometry()
                            .getLocation()
                            .getLng()));
            //get LatLong to Marker
            Marker restaurant = mapsView.addMarker(new MarkerOptions()
                    .position(latLngMarker)
                    .icon(BitmapDescriptorFactory.defaultMarker())
                    .title(result.getName()));
            restaurant.showInfoWindow();
            //show marker
            LatLng latLngResult = new LatLng(
                    (result.getGeometry()
                            .getLocation()
                            .getLat()),
                    (result.getGeometry()
                            .getLocation()
                            .getLng()));
            mapsView.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(@NonNull Marker marker) {
                    return false;
                }
            });
            //set position marker
            mapsView.getUiSettings().setAllGesturesEnabled(true);
            mapsView.getUiSettings().setZoomGesturesEnabled(true);
        }
    }


    private void subscribeToObservables() {
        /*homeRestaurantSharedViewModel.getLocationLiveData().observe(
                this, location -> {
                    if (location != null) {
                        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                        mapsView.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                        mapsView.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                    }
                    homeRestaurantSharedViewModel.getRestaurant(location).observe(this, this::updateLocationUI);
                   // homeRestaurantSharedViewModel.getWorkmates().observe(this, this::updateMarkers);
                }
        );*/
        homeRestaurantSharedViewModel.getHomeViewStateLiveData().observe(getViewLifecycleOwner(), homeWrapperViewState -> {
            updateUi(homeWrapperViewState.getHomeViewState());
            updateUserLocation(homeWrapperViewState.getLocation());
        });
        homeRestaurantSharedViewModel.getPrediction().observe(getViewLifecycleOwner(), place -> {
            Log.d(TAG, "subscribeToObservables: ");
            if (place != null) {
                Log.d(TAG, "subscribeToObservables: " + place);
                if (place.getLatLng() != null) {
                    //mapsView.addMarker();
                    mapsView.moveCamera(CameraUpdateFactory.newLatLng(place.getLatLng()));
                    mapsView.animateCamera(CameraUpdateFactory.newLatLngZoom(place.getLatLng(), 15));
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
        Drawable vectorDrawable = requireContext().getDrawable(id);
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
                if (!permissionDenied) {
                    mapsView.setMyLocationEnabled(true);
                    mapsView.getUiSettings().setMyLocationButtonEnabled(true);
                    return;
                } else {
                    Log.d(TAG, "updateLocationUI: permission denied");
                    lastKnownLocation = null;
                    getLocationPermission();
                }
            } catch (SecurityException e) {
                Log.e("Exception: %s", e.getMessage());
            }
        }
    }

    private void unsubscribeToObservables() {
        // homeRestaurantSharedViewModel.getLocationLiveData().removeObservers(this);
        homeRestaurantSharedViewModel.getHomeViewStateLiveData().removeObservers(getViewLifecycleOwner());
        homeRestaurantSharedViewModel.getPrediction().removeObservers(getViewLifecycleOwner());
    }

    @Override
    public void onResume() {
        super.onResume();
        subscribeToObservables();
        binding.getRoot();
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