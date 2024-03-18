package com.Hayse.go4lunch.datasource.firebase;

import com.Hayse.go4lunch.domain.entites.Workmate;
import com.Hayse.go4lunch.services.firebase.FirebaseHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.net.Uri;

public class FirebaseHelperTest {

    private FirebaseHelper firebaseHelper;

    @Mock
    private FirebaseAuth firebaseAuth;

    @Mock
    private FirebaseUser firebaseUser;

    @Mock
    private FirebaseFirestore firebaseFirestore;

    @Mock
    private CollectionReference workmateRef;

    @Mock
    private CollectionReference favRestaurantRef;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        firebaseHelper = new FirebaseHelper();

        // Mock FirebaseAuth and FirebaseUser instances
        when(FirebaseAuth.getInstance()).thenReturn(firebaseAuth);
        when(firebaseAuth.getCurrentUser()).thenReturn(firebaseUser);

        // Mock FirebaseFirestore and CollectionReference instances
        when(FirebaseFirestore.getInstance()).thenReturn(firebaseFirestore);
        when(firebaseFirestore.collection(anyString())).thenReturn(workmateRef);
        when(firebaseFirestore.collection(anyString())).thenReturn(favRestaurantRef);
    }

    @Test
    public void testGetUserDataOAuth() {
        // Given
        String avatarUrl = "https://example.com/avatar.jpg";
        String name = "John Doe";
        String email = "johndoe@example.com";
        String uid = "1234567890";

        when(firebaseUser.getPhotoUrl()).thenReturn(Uri.parse(avatarUrl));
        when(firebaseUser.getDisplayName()).thenReturn(name);
        when(firebaseUser.getEmail()).thenReturn(email);
        when(firebaseUser.getUid()).thenReturn(uid);

        // When
        Workmate workmate = firebaseHelper.getFirestoreUserDataRT().getValue();

        // Then
        assertEquals(avatarUrl, workmate.getAvatarUrl());
        assertEquals(name, workmate.getName());
        assertEquals(email, workmate.getEmail());
        assertEquals(uid, workmate.getId());
    }

    @Test
    public void testSetNewWorkmate() {
        // Given
        String avatarUrl = "https://example.com/avatar.jpg";
        String name = "John Doe";
        String email = "johndoe@example.com";
        String uid = "1234567890";

        Workmate workmate = new Workmate(avatarUrl, name, email, uid);

        when(firebaseUser.getPhotoUrl()).thenReturn(Uri.parse(avatarUrl));
        when(firebaseUser.getDisplayName()).thenReturn(name);
        when(firebaseUser.getEmail()).thenReturn(email);
        when(firebaseUser.getUid()).thenReturn(uid);

        // When
        firebaseHelper.setNewWorkmate();

        // Then
        Map<String, Object> expectedData = new HashMap<>();
        expectedData.put("id", uid);
        expectedData.put("avatarUrl", avatarUrl);
        expectedData.put("name", name);
        expectedData.put("email", email);
        expectedData.put("placeId", "");
        expectedData.put("restaurantAddress", "");
        expectedData.put("restaurantName", "");
        expectedData.put("restaurantTypeOfFood", "");

        verify(workmateRef).document(uid).set(expectedData);
    }

    // Add more test methods as needed
}