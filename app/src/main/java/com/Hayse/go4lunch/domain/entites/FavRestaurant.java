package com.Hayse.go4lunch.domain.entites;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Nullable;

import com.Hayse.go4lunch.domain.entites.map_api.detail.PhotosItem;

import java.util.Objects;

public class FavRestaurant {
    private String id;
    private String user_id;
    private String place_id;
    private String restaurant_name;

    private String restaurant_pic;
    private int restaurant_rating;
    private String restaurant_address;
    private String restaurant_phone;
    private String restaurant_website;

    public FavRestaurant(){
    }

    public FavRestaurant(String user_id,
                         String place_id,
                         String restaurant_name,
                         PhotosItem restaurant_pic,
                         int restaurant_rating,
                         String restaurant_address,
                         @Nullable String restaurant_phone,
                         @Nullable String restaurant_website){
        this.id = user_id+place_id;
        this.user_id = user_id;
        this.place_id = place_id;
        this.restaurant_name= restaurant_name;
        this.restaurant_address = restaurant_address;
        this.restaurant_rating = restaurant_rating;
        this.restaurant_phone = restaurant_phone;
        this.restaurant_website = restaurant_website;
    }

    protected FavRestaurant(Parcel parcel){
        user_id = parcel.readString();
        place_id = parcel.readString();
        restaurant_name = parcel.readString();
        restaurant_address = parcel.readString();
        restaurant_rating = parcel.readInt();
        restaurant_phone = parcel.readString();
        restaurant_website = parcel.readString();
        id= user_id+place_id;
    }

    public static final Parcelable.Creator<FavRestaurant> CREATOR = new Parcelable.Creator<FavRestaurant>() {
        @Override
        public FavRestaurant createFromParcel(Parcel source) {
            return new FavRestaurant(source);
        }

        @Override
        public FavRestaurant[] newArray(int size) {
            return new FavRestaurant[size];
        }
    };

    //Getter

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getPlace_id() {
        return place_id;
    }

    public void setPlace_id(String place_id) {
        this.place_id = place_id;
    }

    public String getRestaurant_name() {
        return restaurant_name;
    }

    public void setRestaurant_name(String restaurant_name) {
        this.restaurant_name = restaurant_name;
    }

    public int getRestaurant_rating() {
        return restaurant_rating;
    }
    //Setter
    public void setRestaurant_rating(int restaurant_rating) {
        this.restaurant_rating = restaurant_rating;
    }

    public String getRestaurant_address() {
        return restaurant_address;
    }

    public void setRestaurant_address(String restaurant_address) {
        this.restaurant_address = restaurant_address;
    }

    public String getRestaurant_phone() {
        return restaurant_phone;
    }

    public void setRestaurant_phone(String restaurant_phone) {
        this.restaurant_phone = restaurant_phone;
    }

    public String getRestaurant_website() {
        return restaurant_website;
    }

    public void setRestaurant_website(String restaurant_website) {
        this.restaurant_website = restaurant_website;
    }

    @Override
    public int hashCode(){
        return Objects.hash(user_id,
                place_id,
                restaurant_name,
                restaurant_rating,
                restaurant_address,
                restaurant_phone,
                restaurant_website);
    }

    public String getRestaurant_pic() {
        return restaurant_pic;
    }

    public void setRestaurant_pic(String restaurant_pic) {
        this.restaurant_pic = restaurant_pic;
    }
}
