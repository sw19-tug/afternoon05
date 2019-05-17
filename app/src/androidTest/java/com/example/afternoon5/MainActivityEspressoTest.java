package com.example.afternoon5;


import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.is;


@RunWith(AndroidJUnit4.class)
public class MainActivityEspressoTest {
    @Rule
    public ActivityTestRule<MainActivity> activityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void testListVisible() {
        DataProvider.getInstance().setNotes(new ArrayList<>());
        final String TITLE = "onenode";
        final String TEXT = "node";
        NoteSortingEspressoTest.addTestNotes(TITLE, TEXT);
        onView(withId(R.id.node_list)).check(matches(isDisplayed()));
    }

    @Test
    public void testTitle() {
        onView(withText("NOTE")).check(matches(isDisplayed()));
    }

    @Test
    public void testListContent() throws Throwable {
        DataProvider.getInstance().setNotes(new ArrayList<>());
        final String TITLE = "testListContent";
        final String TEXT = "text134";
        NoteSortingEspressoTest.addTestNotes(TITLE, TEXT);

        onView(withText(TITLE)).check(matches(isDisplayed()));
        onView(withText(TEXT)).check(matches(isDisplayed()));

    }


}