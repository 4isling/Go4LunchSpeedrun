package com.Hayse.go4lunch.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.Hayse.go4lunch.R;
import com.Hayse.go4lunch.domain.entites.map_api.nerbysearch.Result;

public class RestaurantAdapter extends ListAdapter<Result, RestaurantAdapter.ViewHolder> {

    public RestaurantAdapter(){
        super(new RestaurantItemCallback());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_restaurant,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView restaurantName;
        private final TextView restaurantAddress;
        private final TextView restaurantOpenHour;
        private final TextView restaurantFoodType;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            restaurantName = itemView.findViewById(R.id.restaurant_item_name);
            restaurantAddress = itemView.findViewById(R.id.restaurant_item_address);
            restaurantOpenHour = itemView.findViewById(R.id.restaurant_item_time);
            restaurantFoodType = itemView.findViewById(R.id.restaurant_item_food);

        }

        public void bind(Result restaurant) {
            restaurantName.setText(restaurant.getName());
            restaurantAddress.setText(restaurant.getVicinity());
            restaurantOpenHour.setText(restaurant.getOpeningHours().toString());
            restaurantFoodType.setText(restaurant.getTypes().toString());
        }
    }

    private static class RestaurantItemCallback extends DiffUtil.ItemCallback<Result>{
        @Override
        public boolean areItemsTheSame(@NonNull Result oldItem, @NonNull Result newItem) {
            return oldItem.getName().equals(newItem.getName()) && oldItem.getGeometry().equals(newItem.getGeometry());
        }

        @Override
        public boolean areContentsTheSame(@NonNull Result oldItem, @NonNull Result newItem){
            return oldItem.equals(newItem);
        }
    }
}
