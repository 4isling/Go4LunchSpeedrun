package com.Hayse.go4lunch.utils;

import com.Hayse.go4lunch.domain.entites.Workmate;

import java.util.ArrayList;
import java.util.List;

public class FakeWorkmates {

    public static List<Workmate> workmates() {
        List<Workmate> workmates = new ArrayList<>();

        Workmate workmate1 = new Workmate();
        workmate1.setId("1");
        workmate1.setAvatarUrl("https://example.com/avatar1");
        workmate1.setName("John Doe");
        workmate1.setEmail("john.doe@example.com");
        workmate1.setPlaceId("place1");
        workmate1.setRestaurantName("Restaurant 1");
        workmate1.setRestaurantAddress("123 Main St");
        workmate1.setRestaurantTypeOfFood("Italian");
        workmates.add(workmate1);

        Workmate workmate2 = new Workmate();
        workmate2.setId("2");
        workmate2.setAvatarUrl("https://example.com/avatar2");
        workmate2.setName("Jane Doe");
        workmate2.setEmail("jane.doe@example.com");
        workmate2.setPlaceId("place2");
        workmate2.setRestaurantName("Restaurant 2");
        workmate2.setRestaurantAddress("456 Elm St");
        workmate2.setRestaurantTypeOfFood("Chinese");
        workmates.add(workmate2);

        Workmate workmate3 = new Workmate();
        workmate3.setId("3");
        workmate3.setAvatarUrl("https://example.com/avatar3");
        workmate3.setName("Bob Smith");
        workmate3.setEmail("bob.smith@example.com");
        workmate3.setPlaceId("place3");
        workmate3.setRestaurantName("Restaurant 3");
        workmate3.setRestaurantAddress("789 Pine St");
        workmate3.setRestaurantTypeOfFood("Japanese");
        workmates.add(workmate3);

        Workmate workmate4 = new Workmate();
        workmate4.setId("4");
        workmate4.setAvatarUrl("https://example.com/avatar4");
        workmate4.setName("Alice Johnson");
        workmate4.setEmail("alice.johnson@example.com");
        workmate4.setPlaceId("place4");
        workmate4.setRestaurantName("Restaurant 4");
        workmate4.setRestaurantAddress("321 Oak St");
        workmate4.setRestaurantTypeOfFood("Indian");
        workmates.add(workmate4);

        Workmate workmate5 = new Workmate();
        workmate5.setId("5");
        workmate5.setAvatarUrl("https://example.com/avatar5");
        workmate5.setName("Charlie Brown");
        workmate5.setEmail("charlie.brown@example.com");
        workmate5.setPlaceId("place5");
        workmate5.setRestaurantName("Restaurant 5");
        workmate5.setRestaurantAddress("654 Maple Ave");
        workmate5.setRestaurantTypeOfFood("Thai");
        workmates.add(workmate5);

        Workmate workmate6 = new Workmate();
        workmate6.setId("6");
        workmate6.setAvatarUrl("https://example.com/avatar6");
        workmate6.setName("Emma White");
        workmate6.setEmail("emma.white@example.com");
        workmate6.setPlaceId("place6");
        workmate6.setRestaurantName("Restaurant 6");
        workmate6.setRestaurantAddress("987 Birch Lane");
        workmate6.setRestaurantTypeOfFood("Greek");
        workmates.add(workmate6);

        Workmate workmate7 = new Workmate();
        workmate7.setId("7");
        workmate7.setAvatarUrl("https://example.com/avatar7");
        workmate7.setName("Oliver Black");
        workmate7.setEmail("oliver.black@example.com");
        workmate7.setPlaceId("place7");
        workmate7.setRestaurantName("Restaurant 7");
        workmate7.setRestaurantAddress("246 Cedar Rd");
        workmate7.setRestaurantTypeOfFood("French");
        workmates.add(workmate7);

        Workmate workmate8 = new Workmate();
        workmate8.setId("8");
        workmate8.setAvatarUrl("https://example.com/avatar8");
        workmate8.setName("Sophia Green");
        workmate8.setEmail("sophia.green@example.com");
        workmate8.setPlaceId("place8");
        workmate8.setRestaurantName("Restaurant 8");
        workmate8.setRestaurantAddress("852 Willow Dr");
        workmate8.setRestaurantTypeOfFood("Korean");
        workmates.add(workmate8);

        Workmate workmate9 = new Workmate();
        workmate9.setId("9");
        workmate9.setAvatarUrl("https://example.com/avatar9");
        workmate9.setName("Jack Blue");
        workmate9.setEmail("jack.blue@example.com");
        workmate9.setPlaceId("place9");
        workmate9.setRestaurantName("Restaurant 9");
        workmate9.setRestaurantAddress("369 Elmwood Ave");
        workmate9.setRestaurantTypeOfFood("Mexican");
        workmates.add(workmate9);

        Workmate workmate10 = new Workmate();
        workmate10.setId("10");
        workmate10.setAvatarUrl("https://example.com/avatar10");
        workmate10.setName("Mia Red");
        workmate10.setEmail("mia.red@example.com");
        workmate10.setPlaceId("place10");
        workmate10.setRestaurantName("Restaurant 10");
        workmate10.setRestaurantAddress("711 Hickory St");
        workmate10.setRestaurantTypeOfFood("American");
        workmates.add(workmate10);

        return workmates;
    }
}
