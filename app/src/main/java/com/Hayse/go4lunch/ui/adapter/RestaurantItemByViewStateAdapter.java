package com.Hayse.go4lunch.ui.adapter;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.Hayse.go4lunch.MainApplication;
import com.Hayse.go4lunch.R;
import com.Hayse.go4lunch.ui.view_state.HomeViewState;
import com.bumptech.glide.Glide;

public class RestaurantItemByViewStateAdapter extends ListAdapter<HomeViewState, RestaurantItemByViewStateAdapter.ViewHolder> {

    private OnRestaurantItemClickListener onRestaurantItemClickListener;

    public RestaurantItemByViewStateAdapter(){
        super(new ListRestaurantItemCallback());
    }

    @NonNull
    @Override
    public RestaurantItemByViewStateAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RestaurantItemByViewStateAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_restaurant,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantItemByViewStateAdapter.ViewHolder holder, int position) {
        holder.bind(getItem(position));
        holder.itemView.setOnClickListener(v -> onRestaurantItemClickListener.onRestaurantItemClick(getItem(position)));
    }

    private static class ListRestaurantItemCallback extends DiffUtil.ItemCallback<HomeViewState>{
        @Override
        public boolean areItemsTheSame(@NonNull HomeViewState oldItem, @NonNull HomeViewState newItem) {
            return oldItem.getPlaceId().equals(newItem.getPlaceId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull HomeViewState oldItem, @NonNull HomeViewState newItem) {
            return oldItem.equals(newItem);
        }
    }
    public void setOnItemClickListener(RestaurantItemByViewStateAdapter.OnRestaurantItemClickListener onRestaurantItemClickListener) {
        this.onRestaurantItemClickListener = onRestaurantItemClickListener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView restaurantImage;
        private final TextView restaurantName;
        private final TextView restaurantAddress;
        private final TextView restaurantOpenHour;
        private final TextView restaurantFoodType;
        private final RatingBar restaurantRating;
        private final ImageView personIcon;
        private final TextView workmateNumber;
        private final TextView distance;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            restaurantImage = itemView.findViewById(R.id.restaurant_item_picture);
            restaurantName = itemView.findViewById(R.id.restaurant_item_name);
            restaurantAddress = itemView.findViewById(R.id.restaurant_item_address);
            restaurantOpenHour = itemView.findViewById(R.id.restaurant_item_time);
            restaurantFoodType = itemView.findViewById(R.id.restaurant_item_food);
            restaurantRating = itemView.findViewById(R.id.restaurant_item_rating);
            distance = itemView.findViewById(R.id.restaurant_item_distance);
            personIcon = itemView.findViewById(R.id.restaurant_item_person_icon);
            workmateNumber = itemView.findViewById(R.id.restaurant_item_number_coworker);
        }

        @SuppressLint("SetTextI18n")
        public void bind(HomeViewState restaurant) {
            if(restaurant != null){
                if(restaurant.getImageUri()!= null){
                    String photoRef = restaurant.getImageUri();
                    Glide.with(restaurantImage.getContext())
                            .load("https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference="+photoRef+"&key="+ MainApplication.getApplication().getApplicationContext().getResources().getString(R.string.MAPS_API_KEY))
                            .into(restaurantImage);
                }
                if(restaurant.getRestaurantName()!= null){
                    restaurantName.setText(restaurant.getRestaurantName());
                }else{
                    restaurantName.setText("");
                }
                if (restaurant.getAddress()!=null){
                    restaurantAddress.setText(restaurant.getAddress());
                }else{
                    restaurantAddress.setText("");
                }
                if (restaurant.getOpeningHours() != null){
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
                if (restaurant.getWorkmateNumber() > 0){
                    personIcon.setImageResource(R.drawable.baseline_person_24);
                    personIcon.setVisibility(View.VISIBLE);
                    workmateNumber.setText("("+restaurant.getWorkmateNumber()+")  ");
                }else{
                    personIcon.setVisibility(View.GONE);
                    workmateNumber.setText("");
                }
                distance.setText(restaurant.getDistance()+"m");
                restaurantFoodType.setText("");
                restaurantRating.setRating((float) (restaurant.getRating() * 3) / 5);
            }
        }
    }

    public interface OnRestaurantItemClickListener{
        void onRestaurantItemClick(HomeViewState result);
    }
}
