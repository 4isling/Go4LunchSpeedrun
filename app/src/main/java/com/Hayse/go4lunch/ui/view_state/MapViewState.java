package com.Hayse.go4lunch.ui.view_state;



import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import com.Hayse.go4lunch.domain.entites.map_api.nerbysearch.Location;
import com.Hayse.go4lunch.domain.entites.map_api.nerbysearch.Result;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MapViewState {

    private List<Result> restaurants;

    private List<Location> restaurantLocation = new ArrayList<>();
    private android.location.Location currentLocation;

 //TODO find the problem after the request we get result but marker setup don't work

    public MapViewState(
          @Nullable List<Result> restaurants
    ){
        if (restaurants != null){
            this.restaurants = restaurants;
            setRestaurantLocation();
        }
    }

    @NonNull
    public List<Result> getRestaurantMapResult(){
        return restaurants;
    }

    private void setRestaurantLocation(){
        for (Result restaurant: this.restaurants) {
            restaurantLocation.add(restaurant.getGeometry().getLocation());
        }
    }

    private void setCurrentLocation(android.location.Location userLocation){
        this.currentLocation = userLocation;
    }

    public android.location.Location getCurrentLocation(){
        return currentLocation;
    }

    public List<Location> getRestaurantsLocations() {
        return restaurantLocation;
    }

    @Override
    public boolean equals(Object o){
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        MapViewState that = (MapViewState) o;
        return restaurants.equals(that.restaurants);
    }

    @Override
    public int hashCode(){
        return Objects.hash(restaurants);
    }

    @NonNull
    @Override
    public String toString(){
        return "RestaurantMapViewState{" +
                "restaurants=" + restaurants+'}';
    }
}
