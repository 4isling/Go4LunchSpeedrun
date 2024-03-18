package com.Hayse.go4lunch.ui.fragments;

import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.Hayse.go4lunch.R;
import com.Hayse.go4lunch.databinding.FragmentRestaurantBinding;
import com.Hayse.go4lunch.ui.activitys.RestaurantDetailActivity;
import com.Hayse.go4lunch.ui.adapter.RestaurantItemByViewStateAdapter;
import com.Hayse.go4lunch.ui.view_state.HomeViewState;
import com.Hayse.go4lunch.ui.viewmodel.HomeRestaurantSharedViewModel;
import com.Hayse.go4lunch.ui.viewmodel.ViewModelFactory;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class RestaurantListFragment extends Fragment {
    private final String TAG = "RestaurantListFragment";
    private HomeRestaurantSharedViewModel viewModel;
    private RestaurantItemByViewStateAdapter adapter;
    private FragmentRestaurantBinding binding;

    private final AtomicBoolean isViewStateSet = new AtomicBoolean(false);
    private Location location;

    private TextView noRestaurant;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(requireActivity(), ViewModelFactory.getInstance()).get(HomeRestaurantSharedViewModel.class);
        binding = FragmentRestaurantBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        noRestaurant = binding.listNoRestaurants;
        initRecyclerView();
        setFloationActionButton();
        return root;
    }

    private void setFloationActionButton() {
        binding.floatingActionButton.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(requireContext(), v);
            popupMenu.inflate(R.menu.sort_menu);
            popupMenu.setOnMenuItemClickListener(item -> {
                if (item.getItemId() == R.id.sort_by_proximity) {
                    viewModel.sortByProximity();
                    return true;
                } else if (item.getItemId() == R.id.sort_by_workmate_number) {
                    viewModel.sortByWorkmateNumber();
                    return true;
                }else if (item.getItemId() == R.id.sort_by_rate) {
                    viewModel.sortByRestaurantRating();
                    return true;
                }else {
                    return true;
                }
            });
            popupMenu.show();
        });
    }

    private void initRecyclerView() {
        Log.d(TAG, "initRecyclerView: ");
        RecyclerView recyclerView = binding.restaurantList;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        adapter = new RestaurantItemByViewStateAdapter();
        recyclerView.setAdapter(adapter);
        subscribeToObservables();
        adapter.setOnItemClickListener(restaurant -> {
            Log.d(TAG, "initRecyclerView: click restaurantItem");
                    RestaurantListFragment.this.startActivity(RestaurantDetailActivity.navigate(
                            RestaurantListFragment.this.getContext(),
                            restaurant.getPlaceId()));
                }
                
            
        );
    }

    private void subscribeToObservables() {
        Log.d(TAG, "subscribeToObservables: ");
        //HomeViewState
        viewModel.getHomeViewStateLiveData().observe(getViewLifecycleOwner(), viewState ->{
            Log.d(TAG, "subscribeToObservables: viewState "+viewState.toString());
            setUserLocation(viewState.getLocation());
            updateRecyclerViewByViewState(viewState.getHomeViewState());
        });

        viewModel.getPrediction().observe(getViewLifecycleOwner(), place -> {
            if (place != null) {
                Log.d(TAG, "subscribeToObservables: " + place.getId());
                RestaurantListFragment.this.startActivity(RestaurantDetailActivity.navigate(
                        RestaurantListFragment.this.getContext(),
                        place.getId()));
            }else {
                Log.d(TAG, "subscribeToObservables: place autocomplete is null");
            }
        });
    }

    private void updateRecyclerViewByViewState(List<HomeViewState> restaurants){
        if (restaurants == null) {
            noRestaurant.setVisibility(View.VISIBLE);
            binding.restaurantList.setVisibility(View.GONE);
            Log.d(TAG, "updateRecyclerView: restaurantsList == null");
        } else {
            Log.d(TAG, "updateRecyclerView: restaurantList != null");
            noRestaurant.setVisibility(View.GONE);
            binding.restaurantList.setVisibility(View.VISIBLE);
            adapter.submitList(restaurants);
        }
    }

    private void setUserLocation(Location location){
        this.location = location;
        Log.d(TAG, "setUserLocation: yserLocation");

    }


    private void removeObserver() {
        viewModel.getLocationLiveData().removeObservers(getViewLifecycleOwner());
        viewModel.getRestaurant(location).removeObservers(getViewLifecycleOwner());
    }

    @Override
    public void onDestroyView() {
        removeObserver();
        binding = null;
        super.onDestroyView();
    }

    @Override
    public void onResume() {
        super.onResume();
        binding.getRoot();
    }
}