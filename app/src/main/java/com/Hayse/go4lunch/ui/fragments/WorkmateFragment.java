package com.Hayse.go4lunch.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.Hayse.go4lunch.databinding.FragmentWorkmateBinding;
import com.Hayse.go4lunch.ui.adapter.WorkmateAdapter;
import com.Hayse.go4lunch.ui.viewmodel.WorkmateViewModel;

public class WorkmateFragment extends Fragment {
    private WorkmateViewModel workmateViewModel;
    //private RestaurantListViewModel restaurantListViewModel;
    private WorkmateAdapter adapter;
    private RecyclerView recyclerView;
    private FragmentWorkmateBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        workmateViewModel = new ViewModelProvider(this).get(WorkmateViewModel.class);
        //restaurantViewModel = new ViewModelProvider(this).get(RestaurantViewModel.class);

        binding = FragmentWorkmateBinding.inflate(inflater, container, false);

        View root = binding.getRoot();

        initRecyclerView();
        return root;
    }

    /**
     * initialise the recyclerView with binding a
     */
    private void initRecyclerView(){
        recyclerView = binding.workmateList;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        adapter = new WorkmateAdapter();
        recyclerView.setAdapter(adapter);
        workmateViewModel.getAllWorkmates().observe(getViewLifecycleOwner(), workmates -> {
            adapter.submitList(workmates);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}