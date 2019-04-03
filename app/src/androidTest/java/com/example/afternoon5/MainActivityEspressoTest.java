package com.example.afternoon5;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;


@RunWith(AndroidJUnit4.class)
public class MainActivityEspressoTest {
    @Rule
    public ActivityTestRule<MainActivity> activityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void testListVisible() {
        //Todo: nächste Zeile einkommentieren, wenn Liste mit ID node_list im xml- File vorhanden ist.
        /*onView(withId(R.id.node_list)).check(matches(isDisplayed()));*/
    }

    @Test
    public void testTitle() {
        onView(withText("afternoon5")).check(matches(isDisplayed()));
    }

    @Test
    public void testListContent()  throws Throwable {
        final String TITLE = "veryUniqueTestingStringOne";
        final String TEXT = "veryUniqueTestingStringTwo";
        // When.
        activityTestRule.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                activityTestRule.getActivity().addListElement(new note_obj(TITLE,TEXT));
                onView(withText(TITLE)).check(matches(isDisplayed()));
                onView(withText(TEXT)).check(matches(isDisplayed()));
            }
        });
    }



    }