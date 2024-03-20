package com.Hayse.go4lunch.utils;

import com.Hayse.go4lunch.domain.entites.map_api.nerbysearch.Geometry;
import com.Hayse.go4lunch.domain.entites.map_api.nerbysearch.Location;
import com.Hayse.go4lunch.domain.entites.map_api.nerbysearch.OpeningHours;
import com.Hayse.go4lunch.domain.entites.map_api.nerbysearch.Photo;
import com.Hayse.go4lunch.domain.entites.map_api.nerbysearch.PlusCode;
import com.Hayse.go4lunch.domain.entites.map_api.nerbysearch.RestaurantResult;
import com.Hayse.go4lunch.domain.entites.map_api.nerbysearch.Result;

import java.util.ArrayList;
import java.util.List;

public class FakeNearbySearchResult {
    public static void main(String[] args) {
        List<Object> htmlAttributions = new ArrayList<>();
        List<Result> results = new ArrayList<>();

        // Fake Result object
        Geometry geometry = new Geometry(new Location(1.234567F, 2.345678F), null);
        OpeningHours openingHours = new OpeningHours(true);
        List<Photo> photos = new ArrayList<>();
        PlusCode plusCode = new PlusCode("fakeCompoundCode", "fakeGlobalCode");

        Result result = new Result(
                "OPERATIONAL",
                geometry,
                "fakeIcon",
                "fakeIconBackgroundColor",
                "fakeIconMaskBaseUri",
                "Fake Restaurant",
                openingHours,
                photos,
                "fakePlaceId",
                plusCode,
                2,
                4.5f,
                "fakeReference",
                "GOOGLE",
                new ArrayList<String>() {{ add("restaurant"); }},
                1234,
                "Fake Address"
        );

        results.add(result);

        RestaurantResult restaurantResult = new RestaurantResult(htmlAttributions, results, "OK");

        System.out.println(restaurantResult);
    }
}
