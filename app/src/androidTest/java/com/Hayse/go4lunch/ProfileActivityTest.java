package com.Hayse.go4lunch;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.app.Activity;

import androidx.lifecycle.ViewModelProvider;
import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;



import com.Hayse.go4lunch.services.firebase.FavRepository;
import com.Hayse.go4lunch.services.firebase.WorkmateRepository;
import com.Hayse.go4lunch.ui.activitys.ProfileActivity;
import com.Hayse.go4lunch.ui.viewmodel.ProfileViewModel;
import com.Hayse.go4lunch.ui.viewmodel.ViewModelFactory;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;


/*
@RunWith(AndroidJUnit4.class)
public class ProfileActivityTest {

    @Rule
    public final ActivityScenarioRule<ProfileActivity> rule = new ActivityScenarioRule<>(ProfileActivity.class);

    private ProfileViewModel viewModel;

    private ActivityScenario<ProfileActivity> scenario = rule.getScenario();


    @Before
    public void setup() {
        FakeWorkmateDataSource fakeWorkmateDataSource = new FakeWorkmateDataSource();
        FakeFavDataSource fakeFavDataSource = new FakeFavDataSource();
        WorkmateRepository workmateRepository = new WorkmateRepository(fakeWorkmateDataSource);
        FavRepository favRepository = new FavRepository(fakeFavDataSource);
        ViewModelFactory viewModelFactory = new ViewModelFactory(workmateRepository, favRepository);
        viewModel = new ViewModelProvider(activityTestRule.getActivity(), viewModelFactory).get(ProfileViewModel.class);
    }


    public void tearDown() throws Exception {
        if (scenario != null) {
            scenario.close();
        }
        scenario = null;
    }

    public Activity getActivity() {
        if (scenario == null) {
            scenario = ActivityScenario.launch(ProfileActivity.class);
        }
        return tryAcquireScenarioActivity(scenario);
    }

    protected static Activity tryAcquireScenarioActivity(ActivityScenario activityScenario) {
        Semaphore activityResource = new Semaphore(0);
        Activity[] scenarioActivity = new Activity[1];
        activityScenario.onActivity(activity -> {
            scenarioActivity[0] = activity;
            activityResource.release();
        });
        try {
            activityResource.tryAcquire(15000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            Assert.fail("Failed to acquire activity scenario semaphore");
        }
        Assert.assertNotNull("Scenario Activity should be non-null", scenarioActivity[0]);
        return scenarioActivity[0];
    }

    @Test
    public void testSetupTextFields() {
        // Set the user's name and email in the ViewModel
        viewModel.setName("John Doe");
        viewModel.setEmail("johndoe@example.com");

        // Check that the name and email fields are displayed correctly
        onView(withId(R.id.nameField)).check(matches(withText("John Doe")));
        onView(withId(R.id.emailField)).check(matches(withText("johndoe@example.com")));

        // Change the name and email fields and check that the ViewModel is updated correctly
        onView(withId(R.id.nameField)).perform(replaceText("Jane Doe"));
        onView(withId(R.id.emailField)).perform(replaceText("janedoe@example.com"));
        assertThat(viewModel.getUser().getValue().getName(), equalTo("Jane Doe"));
        assertThat(viewModel.getUser().getValue().getEmail(), equalTo("janedoe@example.com"));
    }

    @Test
    public void testSaveButton() {
        // Set the user's name and email in the ViewModel
        viewModel.setName("John Doe");
        viewModel.setEmail("johndoe@example.com");

        // Check that the save button is enabled
        onView(withId(R.id.save_button)).check(matches(isEnabled()));

        // Clear the name and email fields and check that the save button is disabled
        onView(withId(R.id.name_field)).perform(replaceText(""));
        onView(withId(R.id.email_field)).perform(replaceText(""));
        onView(withId(R.id.save_button)).check(matches(not(isEnabled())));

        // Enter a valid name and email and check that the save button is enabled
        onView(withId(R.id.nameField)).perform(replaceText("John Doe"));
        onView(withId(R.id.emailField)).perform(replaceText("johndoe@example.com"));
        onView(withId(R.id.saveButton)).check(matches(isEnabled()));

        // Click the save button and check that the user's name and email are updated in the ViewModel
        onView(withId(R.id.saveButton)).perform(click());
        assertThat(viewModel.getUser().getValue().getName(), equalTo("John Doe"));
        assertThat(viewModel.getUser().getValue().getEmail(), equalTo("johndoe@example.com"));
    }

    // Add more test methods as needed
}*/