package com.Hayse.go4lunch;

import android.content.Intent;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import com.Hayse.go4lunch.ui.activitys.MainActivity;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityScenarioRule<MainActivity> activityScenarioRule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Before
    public void setUp() {
        // Set up any necessary data or mocks before each test
    }

    @Test
    public void testLaunchActivity() {
        // Check that the activity is launched successfully
        onView(withId(R.id.activity_main_frame_layout)).check(matches(isDisplayed()));
    }

    @Test
    public void testBottomNavigation() {
        // Check that the bottom navigation view is displayed
        onView(withId(R.id.bottom_nav_view)).check(matches(isDisplayed()));

        // Click on the map item
        onView(withId(R.id.navigation_map)).perform(click());

        // Check that the map fragment is displayed
        onView(withId(R.id.map_view)).check(matches(isDisplayed()));

        // Click on the restaurant item
        onView(withId(R.id.navigation_restaurant)).perform(click());

        // Check that the restaurant list fragment is displayed
        onView(withId(R.id.restaurant_list)).check(matches(isDisplayed()));

        // Click on the workmate item
        onView(withId(R.id.navigation_workmate)).perform(click());

        // Check that the workmate fragment is displayed
        onView(withId(R.id.workmate_list)).check(matches(isDisplayed()));
    }
}