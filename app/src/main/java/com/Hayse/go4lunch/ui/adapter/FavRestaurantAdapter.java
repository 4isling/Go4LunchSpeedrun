package com.Hayse.go4lunch.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.Hayse.go4lunch.R;
import com.Hayse.go4lunch.domain.entites.FavRestaurant;

public class FavRestaurantAdapter extends ListAdapter<FavRestaurant, FavRestaurantAdapter.ViewHolder> {

    private OnFavRestaurantItemClickListener onFavRestaurantItemClickListener;

    public FavRestaurantAdapter() {
        super(new ListFavRestaurantItemCallback());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FavRestaurantAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_restaurant_profil,parent,false));

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(getItem(position));
        holder.itemView.setOnClickListener(v -> onFavRestaurantItemClickListener.onFavRestaurantItemClick(getItem(position)));
    }

    private static class ListFavRestaurantItemCallback extends DiffUtil.ItemCallback<FavRestaurant> {

        @Override
        public boolean areItemsTheSame(@NonNull FavRestaurant oldItem, @NonNull FavRestaurant newItem) {
            return oldItem.getId().equals(newItem.getId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull FavRestaurant oldItem, @NonNull FavRestaurant newItem) {
            return oldItem.equals(newItem);
        }
    }

    public void setOnFavRestaurantItemClickListener(OnFavRestaurantItemClickListener onFavRestaurantItemClickListener) {
        this.onFavRestaurantItemClickListener = onFavRestaurantItemClickListener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView restaurantName;
        public TextView restaurantAddress;
        public Button deleteButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            restaurantName = itemView.findViewById(R.id.restaurant_name_profile_item);
            restaurantAddress = itemView.findViewById(R.id.restaurant_address_item_profile);
            deleteButton = itemView.findViewById(R.id.remove_button);
        }

        public void bind(FavRestaurant restaurant) {
            if (restaurant != null) {
                if (restaurant.getRestaurant_name() != null) {
                    restaurantName.setText(restaurant.getRestaurant_name());
                } else {
                    restaurantName.setText("");
                }
                if (restaurant.getRestaurant_address() != null) {
                    restaurantAddress.setText(restaurant.getRestaurant_address());
                } else {
                    restaurantAddress.setText("");
                }
                deleteButton.setOnClickListener(v -> {

                });
            }
        }
    }

    public interface OnFavRestaurantItemClickListener {
        void onFavRestaurantItemClick(FavRestaurant favRestaurant);
    }
}
