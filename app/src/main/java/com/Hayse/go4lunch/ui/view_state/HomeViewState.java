package com.Hayse.go4lunch.ui.view_state;

import android.graphics.Path;
import android.media.Rating;

import androidx.annotation.Nullable;

import com.Hayse.go4lunch.domain.entites.map_api.nerbysearch.Geometry;
import com.Hayse.go4lunch.domain.entites.map_api.nerbysearch.OpeningHours;

import java.util.Objects;

public class HomeViewState {
    private String placeId;
    private String imageUri;
    private String restaurantName;
    private String address;
    private Geometry geometry;
    private OpeningHours openingHours;
    private float rating;
    private int workmateNumber;
    private int distance;

    public HomeViewState(
            @Nullable String placeId,
            @Nullable String imageUri,
            @Nullable String restaurantName,
            @Nullable String address,
            Geometry geometry,
            OpeningHours openingHours,
            float rating,
            int workmateNumber,
            int distance
    ) {
        this.placeId = placeId;
        this.imageUri= imageUri;
        this.restaurantName = restaurantName;
        this.address = address;
        this.geometry = geometry;
        this.openingHours = openingHours;
        this.rating = rating;
        this.workmateNumber = workmateNumber;
        this.distance = distance;
    }

    public String getPlaceId() {
        return placeId;
    }

    public String getImageUri() {
        return imageUri;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public String getAddress() {
        return address;
    }

    public Geometry getGeometry() {
        return geometry;
    }

    public OpeningHours getOpeningHours() {
        return openingHours;
    }

    public float getRating() {
        return rating;
    }

    public int getWorkmateNumber() {
        return workmateNumber;
    }

    public int getDistance() {
        return distance;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }

    public void setOpen(OpeningHours openingHours) {
        this.openingHours = openingHours;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public void setWorkmateNumber(int workmateNumber) {
        this.workmateNumber = workmateNumber;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HomeViewState that = (HomeViewState) o;
        return openingHours == that.openingHours && rating == that.rating && workmateNumber == that.workmateNumber && distance == that.distance && Objects.equals(placeId, that.placeId) && Objects.equals(imageUri, that.imageUri) && Objects.equals(restaurantName, that.restaurantName) && Objects.equals(address, that.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(placeId, imageUri, restaurantName, address, openingHours, rating, workmateNumber, distance);
    }

    @Override
    public String toString() {
        return "HomeViewState{" +
                "placeId='" + placeId + '\'' +
                ", imageUri='" + imageUri + '\'' +
                ", restaurantName='" + restaurantName + '\'' +
                ", address='" + address + '\'' +
                ", isOpen=" + openingHours +
                ", rating=" + rating +
                ", workmateNumber=" + workmateNumber +
                ", distance=" + distance +
                '}';
    }
}