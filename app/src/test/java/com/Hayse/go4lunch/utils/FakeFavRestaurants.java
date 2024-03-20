package com.Hayse.go4lunch.utils;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.Hayse.go4lunch.domain.entites.FavRestaurant;
import com.Hayse.go4lunch.domain.entites.map_api.detail.Photo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FakeFavRestaurants {
    public static List<FavRestaurant> favRestaurants(){
        List<String> htmlAttributions1 = new ArrayList<>();
        htmlAttributions1.add("Attribution1");
        Photo photo1 = new Photo();
        photo1.setHeight(800L);
        photo1.setHtmlAttributions(htmlAttributions1);
        photo1.setPhotoReference("photoRef1");
        photo1.setWidth(600L);

        List<String> htmlAttributions2 = new ArrayList<>();
        htmlAttributions2.add("Attribution2");
        Photo photo2 = new Photo();
        photo2.setHeight(1024L);
        photo2.setHtmlAttributions(htmlAttributions2);
        photo2.setPhotoReference("photoRef2");
        photo2.setWidth(768L);

        List<String> htmlAttributions3 = new ArrayList<>();
        htmlAttributions3.add("Attribution3");
        Photo photo3 = new Photo();
        photo3.setHeight(1200L);
        photo3.setHtmlAttributions(htmlAttributions3);
        photo3.setPhotoReference("photoRef3");
        photo3.setWidth(800L);
        FavRestaurant restaurant1 = new FavRestaurant(
                "user1",
                "place1",
                "Restaurant1",
                photo1,
                4.5f,
                "123 Main St",
                "123-456-7890",
                "www.restaurant1.com"
        );
        FavRestaurant restaurant2 = new FavRestaurant(
                "user2",
                "place2",
                "Restaurant2",
                photo2,
                4.0f,
                "456 Elm St",
                null,
                "www.restaurant2.com"
        );

        FavRestaurant restaurant3 = new FavRestaurant(
                "user3",
                "place3",
                "Restaurant3",
                photo3,
                4.8f,
                "789 Oak St",
                "987-654-3210",
                null
        );
        return Arrays.asList(restaurant1,restaurant2,restaurant3);
    }

    public static LiveData<List<FavRestaurant>> favRestaurantLiveData(){
        return new MutableLiveData<>(favRestaurants());
    }

}
