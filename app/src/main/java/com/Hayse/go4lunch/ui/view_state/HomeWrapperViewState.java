package com.Hayse.go4lunch.ui.view_state;

import android.location.Location;

import java.util.List;
import java.util.Objects;
public class HomeWrapperViewState {
    private final List<HomeViewState> listRestaurants;

    private final Location location;

    @Override
    public String toString() {
        return "HomeWrapperViewState{" +
                "listRestaurants=" + listRestaurants +
                ", location=" + location +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HomeWrapperViewState that = (HomeWrapperViewState) o;
        return Objects.equals(listRestaurants, that.listRestaurants) && Objects.equals(location, that.location);
    }

    @Override
    public int hashCode() {
        return Objects.hash(listRestaurants, location);
    }

    public HomeWrapperViewState(List<HomeViewState> listRestaurants, Location location){
        this.listRestaurants = listRestaurants;
        this.location = location;
    }

    public List<HomeViewState> getHomeViewState(){
        return listRestaurants;
    }

    public Location getLocation() {
        return location;
    }

}
