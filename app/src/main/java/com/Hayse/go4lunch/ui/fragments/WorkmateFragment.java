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
import com.Hayse.go4lunch.ui.viewmodel.ViewModelFactory;
import com.Hayse.go4lunch.ui.viewmodel.WorkmateViewModel;

public class WorkmateFragment extends Fragment {
    private WorkmateViewModel workmateViewModel;
    private WorkmateAdapter adapter;
    private FragmentWorkmateBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        workmateViewModel = new ViewModelProvider(requireActivity(), ViewModelFactory.getInstance()).get(WorkmateViewModel.class);
        binding = FragmentWorkmateBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        initRecyclerView();
        return root;
    }

    /**
     * initialise the recyclerView with binding a
     */
    private void initRecyclerView(){
        RecyclerView recyclerView = binding.workmateList;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        adapter = new WorkmateAdapter();
        recyclerView.setAdapter(adapter);
        workmateViewModel.getWorkmatesRt().observe(getViewLifecycleOwner(), workmates -> {
            adapter.submitList(workmates);
        });
        //@todo item touch open workmate detail
    }

    private void removeObservers(){
        workmateViewModel.getWorkmatesRt().removeObservers(getViewLifecycleOwner());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        removeObservers();
        binding = null;
    }
}