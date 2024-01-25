package com.Hayse.go4lunch.services.google_map.google_api;

import com.Hayse.go4lunch.domain.entites.map_api.detail.DetailResponse;
import com.Hayse.go4lunch.domain.entites.map_api.nerbysearch.RestaurantResult;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GMapsApi {
    @GET("nearbysearch/json" )
    Call<RestaurantResult> getListOfRestaurants(@Query("location") String location,
                                                @Query("radius") int radius,
                                                @Query("type") String type,
                                                @Query("key") String key
    );

    @GET("nearbysearch/json")
    Call<RestaurantResult> getNearBySearchResult(@Query("location")String location,
                                                 @Query("radius")String radius,
                                                 @Query("type") String type,
                                                 @Query("key") String key);
    @GET("details/json" )
    Call<DetailResponse> getPlaceDetails(@Query("place_id")String placeId,
                                         @Query("key") String key);


    @GET("nearbysearch/json")
    Observable<RestaurantResult> getRestaurantResult(@Query("location") String location,
                                                     @Query("radius") int radius,
                                                     @Query("type") String type,
                                                     @Query("key") String key
    );
}
