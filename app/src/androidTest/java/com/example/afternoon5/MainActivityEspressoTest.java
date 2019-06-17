package com.example.afternoon5;


import android.support.test.espresso.DataInteraction;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.assertion.ViewAssertions;

import android.support.test.espresso.ViewInteraction;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.example.afternoon5.HelperClasses.Note;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
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
        final String TITLE = "veryUniqueTestingStringOne";
        final String TEXT = "veryUniqueTestingStringTwo";
        final boolean PIN = false;
        String[] tags = {};
        // When.
        activityTestRule.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //activityTestRule.getActivity().addListElement(new note_obj(TITLE, TEXT));
                DataProvider.getInstance().addNoteToNotes(new Note(TITLE, TEXT, tags, PIN));
                activityTestRule.getActivity().refreshList();

            }
        });
        

        //Thread.sleep(10000);
        onView(withText(TITLE)).check(matches(isDisplayed()));
        onView(withText(TEXT)).check(matches(isDisplayed()));

    }





}