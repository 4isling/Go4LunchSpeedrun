package com.Hayse.go4lunch.ui.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.Hayse.go4lunch.databinding.FragmentRestaurantBinding;
import com.Hayse.go4lunch.services.permission_checker.PermissionChecker;
import com.Hayse.go4lunch.ui.adapter.RestaurantAdapter;

public class RestaurantListFragment extends Fragment {
    private FragmentRestaurantBinding binding;
    private RestaurantAdapter adapter;
    //private RestaurantViewModel viewModel;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentRestaurantBinding.inflate(inflater,container, false);
        View root = binding.getRoot();
        //initViewModel();
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        if(new PermissionChecker(getActivity().getApplication()).hasLocationPermission()) {
            initRecyclerView();
            //    getBaseList();
        }else{
            Log.d("RestaurantListFragment", "no location permission");
           // getLocationPermission();
        }
    }

    private void initRecyclerView(){
        adapter = new RestaurantAdapter();
        binding.restaurantList.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.restaurantList.setAdapter(adapter);
        binding.restaurantList.setHasFixedSize(true);
       /* viewModel.getRestaurantsViewStateLiveData().observe(getViewLifecycleOwner(), restaurantsListViewState -> {
            adapter.submitList(restaurantsListViewState.getRestaurants());
        });*/
    }

}
