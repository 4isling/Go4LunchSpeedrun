package com.Hayse.go4lunch.ui.adapter;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.Hayse.go4lunch.R;
import com.Hayse.go4lunch.domain.entites.map_api.nerbysearch.Result;
import com.bumptech.glide.Glide;

import java.util.List;

public class RestaurantAdapter extends ListAdapter<Result, RestaurantAdapter.ViewHolder> {
    private List<Result> restaurantList;
    private OnRestaurantItemClickListener onRestaurantItemClickListener;
    public RestaurantAdapter(){
        super(new ListRestaurantItemCallback());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.restaurant_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(getItem(position));
        holder.itemView.setOnClickListener(v -> onRestaurantItemClickListener.onRestaurantItemClick(getItem(position)));
    }
    @SuppressLint("NotifyDataSetChanged")
    public void setList(List<Result> restaurantList){
        this.restaurantList = restaurantList;
    }

    public void setOnItemClickListener(OnRestaurantItemClickListener onRestaurantItemClickListener) {
        this.onRestaurantItemClickListener = onRestaurantItemClickListener;
    }

    private static class ListRestaurantItemCallback extends DiffUtil.ItemCallback<Result>{

        @Override
        public boolean areItemsTheSame(@NonNull Result oldItem, @NonNull Result newItem) {
            return oldItem.getPlaceId().equals(newItem.getPlaceId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull Result oldItem, @NonNull Result newItem) {
            return oldItem.equals(newItem);
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView restaurantImage;
        private final TextView restaurantName;
        private final TextView restaurantAddress;
        private final TextView restaurantOpenHour;
        private final TextView restaurantFoodType;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            restaurantImage = itemView.findViewById(R.id.restaurant_item_picture);
            restaurantName = itemView.findViewById(R.id.restaurant_item_name);
            restaurantAddress = itemView.findViewById(R.id.restaurant_item_address);
            restaurantOpenHour = itemView.findViewById(R.id.restaurant_item_time);
            restaurantFoodType = itemView.findViewById(R.id.restaurant_item_food);
        }

//@todo find why null pointer exception
        public void bind(Result restaurant) {
            if(restaurant != null){
                if(restaurant.getPhotos().get(0)!= null){
                    Glide.with(restaurantImage.getContext())
                            .load(restaurant.getPhotos().get(0))
                            .into(restaurantImage);
                }
                if(restaurant.getName()!= null){
                    restaurantName.setText(restaurant.getName());
                }else{
                    restaurantName.setText("");
                }
                if (restaurant.getVicinity()!=null){
                    restaurantAddress.setText(restaurant.getVicinity());
                }else{
                    restaurantAddress.setText("");
                }
                if (restaurant.getOpeningHours() !=null){
                    if(restaurant.getOpeningHours().isOpenNow()){
                        restaurantOpenHour.setText(R.string.is_open);
                        restaurantOpenHour.setTextColor(Color.GREEN);
                    }else{
                        restaurantOpenHour.setText(R.string.is_close);
                        restaurantOpenHour.setTextColor(Color.RED);
                    }
                }else {
                    restaurantOpenHour.setText("");
                }

                if (restaurant.getTypes() != null){
                    restaurantFoodType.setText(restaurant.getTypes().toString());
                }else {
                    restaurantFoodType.setText("");
                }
            }
       }
    }

    public interface OnRestaurantItemClickListener{
        void onRestaurantItemClick(Result result);
    }
}
