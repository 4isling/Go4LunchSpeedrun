package com.Hayse.go4lunch.ui.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.Hayse.go4lunch.databinding.FragmentRestaurantBinding;
import com.Hayse.go4lunch.domain.entites.map_api.nerbysearch.Result;
import com.Hayse.go4lunch.services.permission_checker.PermissionChecker;
import com.Hayse.go4lunch.ui.adapter.RestaurantAdapter;
import com.Hayse.go4lunch.ui.viewmodel.HomeRestaurantSharedViewModel;
import com.Hayse.go4lunch.ui.viewmodel.ViewModelFactory;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public class RestaurantListFragment extends Fragment {
    private String TAG = "com.Hayse.go4lunch.ui.fragments.RestaurantListFragment";
    private FragmentRestaurantBinding binding;
    private RestaurantAdapter adapter;
    private HomeRestaurantSharedViewModel viewModel;
    @NonNull
    private TextView noRestaurant;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentRestaurantBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        noRestaurant = binding.listNoRestaurants;
        viewModel = new ViewModelProvider(this, ViewModelFactory.getInstance()).get(HomeRestaurantSharedViewModel.class);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        if (new PermissionChecker(getActivity().getApplication()).hasLocationPermission()) {
            initRecyclerView();
            //    getBaseList();
        } else {
            Log.d(TAG, "no location permission");
            // getLocationPermission();
        }
    }

    private void initRecyclerView() {
        Log.d(TAG, "initRecyclerView: ");
        adapter = new RestaurantAdapter();
        binding.restaurantList.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.restaurantList.setAdapter(adapter);
        binding.restaurantList.setHasFixedSize(true);
        subscribeToObservables();
        /*
         * @TODO itemTouchListener get detail
         */
        binding.restaurantList.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
                Log.d(TAG, "onInterceptTouchEvent: ");
                return false;
            }

            @Override
            public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
                Log.d(TAG, "onTouchEvent: ");
            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
                Log.d(TAG, "onRequestDisallowInterceptTouchEvent: ");
            }
        });
    }
    private void subscribeToObservables() {
        Log.d(TAG, "subscribeToObservables: ");
        viewModel.getLocationMutableLiveData().observe(
                getViewLifecycleOwner(), location -> {
                    if(location!= null){
                        LatLng latLng = new LatLng(location.getLatitude(),location.getLongitude());
                    }
                    viewModel.getRestaurant(location).observe(getViewLifecycleOwner(), this::updateRecyclerView);
                }
        );

    }

    private void updateRecyclerView(List<Result> resultList){
            if (resultList == null) {
                    noRestaurant.setVisibility(View.VISIBLE);
                    binding.restaurantList.setVisibility(View.GONE);
                    Log.d(TAG, "updateRecyclerView: restaurantsList == null");
            } else {
                Log.d(TAG, "updateRecyclerView: restaurantList != null");
                noRestaurant.setVisibility(View.GONE);
                binding.restaurantList.setVisibility(View.VISIBLE);
                adapter.submitList(resultList);
            }
    }
}
