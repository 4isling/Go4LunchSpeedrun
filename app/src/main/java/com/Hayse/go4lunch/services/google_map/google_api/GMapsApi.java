package com.Hayse.go4lunch.services.google_map.google_api;

import com.Hayse.go4lunch.domain.entites.map_api.nerbysearch.RestaurantResult;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GMapsApi {
/*
    https://maps.googleapis.com/maps/api/place/nearbysearch/json?
    location=-33.8670522%2C151.1957362&
    radius=1500&
    type=restaurant&
    keyword=cruise&
    key=AIzaSyB4CUJAVDf1K4E-HHBInbgMKfz7JDh377w
 */   @GET("nearbysearch/json" )
    Call<RestaurantResult> getListOfRestaurants(@Query("location") String location,
                                                @Query("radius") int radius,
                                                @Query("type") String type,
                                                @Query("key") String key
    );
/*
    @GET("details/json?key=" + MAPS_API_KEY)
    Call<DetailResponse> getPlaceDetails(@Query("place_id")String placeId);
*/

}
