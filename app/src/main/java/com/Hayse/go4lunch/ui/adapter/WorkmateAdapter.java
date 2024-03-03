package com.Hayse.go4lunch.ui.adapter;

import android.annotation.SuppressLint;
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
import com.Hayse.go4lunch.domain.entites.Workmate;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class WorkmateAdapter extends ListAdapter<Workmate, WorkmateAdapter.ViewHolder> {

    List<Workmate> workmatesList;
    public WorkmateAdapter(){
        super(new ListWorkmateItemCallback());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_workmate,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setList(List<Workmate> workmatesList){
        this.workmatesList = workmatesList;
    }

    private static class ListWorkmateItemCallback extends DiffUtil.ItemCallback<Workmate> {
        @Override
        public boolean areItemsTheSame(@NonNull Workmate oldItem, @NonNull Workmate newItem) {
            return oldItem.getId().equals(newItem.getId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull Workmate oldItem, @NonNull Workmate newItem) {
            return oldItem.equals(newItem);
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView avatarImageView;
        private TextView nameTextView;
        private TextView typeOfFoodTextView;
        private TextView restaurantNameTextView;

        public ViewHolder(@NonNull View itemView){
            super(itemView);
            avatarImageView = itemView.findViewById(R.id.workmate_item_avatar);
            nameTextView = itemView.findViewById(R.id.workmate_item_name);
            typeOfFoodTextView = itemView.findViewById(R.id.workmate_item_type_food);
            restaurantNameTextView = itemView.findViewById(R.id.workmate_item_restaurent);
        }

        public void bind(Workmate item){
            nameTextView.setText(item.getName());

            if(item.getRestaurantTypeOfFood() != null){
                typeOfFoodTextView.setText(item.getRestaurantTypeOfFood());
            }
            if(item.getRestaurantName()!= null){
                restaurantNameTextView.setText(item.getRestaurantName());
            }
            if (item.getAvatarUrl() != null){
                Glide.with(avatarImageView)
                        .load(item.getAvatarUrl())
                        .apply(RequestOptions.circleCropTransform())
                        .into(avatarImageView);
            }
        }
    }
}
