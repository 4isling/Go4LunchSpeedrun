package com.Hayse.go4lunch.utils;

import androidx.lifecycle.MutableLiveData;

import com.Hayse.go4lunch.domain.entites.map_api.detail.Result;
import com.Hayse.go4lunch.domain.entites.map_api.detail.*;

import java.util.ArrayList;
import java.util.Arrays;

public class DetailFakeResult {


    public static Result getFakeDetailResult(){
        Result result = new Result();
        result.setPlaceId("fakePlaceID");
        result.setName("Restaurant 1");
        result.setPhotos(new ArrayList<Photo>());
        result.setRating(3.5f);
        result.setWebsite("http://www.fakerestaurant.com");
        result.setFormattedAddress("123 Main St");
        result.setFormattedPhoneNumber("0600000000");
        result.setTypes(Arrays.asList("Type1", "type2"));
        return result;
    }

    public static MutableLiveData<Result> getFakeDetailResultLiveData(){
        MutableLiveData<Result> resultMutableLiveData = new MutableLiveData<>();
        Result result = new Result();
        result.setPlaceId("fakePlaceID");
        result.setName("Restaurant 1");
        result.setPhotos(new ArrayList<Photo>());
        result.setRating(3.5f);
        result.setWebsite("http://www.fakerestaurant.com");
        result.setFormattedAddress("123 Main St");
        result.setFormattedPhoneNumber("0600000000");
        result.setTypes(Arrays.asList("Type1", "type2"));
        resultMutableLiveData.postValue(result);
        return resultMutableLiveData;
    }


}
