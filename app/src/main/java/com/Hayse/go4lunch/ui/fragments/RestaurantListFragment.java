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
import com.Hayse.go4lunch.ui.activitys.RestaurantDetailActivity;
import com.Hayse.go4lunch.ui.adapter.RestaurantAdapter;
import com.Hayse.go4lunch.ui.viewmodel.HomeRestaurantSharedViewModel;
import com.Hayse.go4lunch.ui.viewmodel.ViewModelFactory;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public class RestaurantListFragment extends Fragment {
    private String TAG = "com.Hayse.go4lunch.ui.fragments.RestaurantListFragment";
    private HomeRestaurantSharedViewModel viewModel;
    private RestaurantAdapter adapter;
    private RecyclerView recyclerView;
    private FragmentRestaurantBinding binding;

    @NonNull
    private TextView noRestaurant;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this, ViewModelFactory.getInstance()).get(HomeRestaurantSharedViewModel.class);
        binding = FragmentRestaurantBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        noRestaurant = binding.listNoRestaurants;
        initRecyclerView();
        return root;
    }

    private void initRecyclerView() {
        Log.d(TAG, "initRecyclerView: ");
        recyclerView = binding.restaurantList;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        adapter = new RestaurantAdapter();
        recyclerView.setAdapter(adapter);
        subscribeToObservables();
        adapter.setOnItemClickListener(restaurant -> RestaurantListFragment.this.startActivity(RestaurantDetailActivity.navigate(
                RestaurantListFragment.this.getContext(),
                restaurant.getPlaceId()
        )));
    }
    private void subscribeToObservables() {
        Log.d(TAG, "subscribeToObservables: ");
        viewModel.getLocationMutableLiveData().observe(
                getViewLifecycleOwner(), location -> {
                    if(location!= null){
                        LatLng latLng = new LatLng(location.getLatitude(),location.getLongitude());
                        viewModel.getRestaurant(location).observe(getViewLifecycleOwner(), this::updateRecyclerView);
                    }
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
