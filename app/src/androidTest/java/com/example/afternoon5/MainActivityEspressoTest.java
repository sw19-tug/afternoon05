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

import static android.support.test.espresso.Espresso.onData;
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
        onView(withId(R.id.node_list)).check(matches(isDisplayed()));
    }

    @Test
    public void testTitle() {
        onView(withText("afternoon5")).check(matches(isDisplayed()));
    }

    @Test
    public void testListContent() throws Throwable {
        final String TITLE = "veryUniqueTestingStringOne";
        final String TEXT = "veryUniqueTestingStringTwo";
        // When.
        activityTestRule.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //activityTestRule.getActivity().addListElement(new note_obj(TITLE, TEXT));
                DataProvider.getInstance().addNoteToNotes(new Note(TITLE, TEXT));

            }
        });
        

        Thread.sleep(10000);
        onView(withText(TITLE)).check(matches(isDisplayed()));
        onView(withText(TEXT)).check(matches(isDisplayed()));

    }


    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }

    @Test
    public void testNoteRemoved() throws Throwable {

        int noteSize = DataProvider.getInstance().getNotes().size();

        ViewInteraction floatingActionButton = onView(
                allOf(withId(R.id.createNoteButton),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                1),
                        isDisplayed()));
        floatingActionButton.perform(click());

        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.editTitle),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                0),
                        isDisplayed()));
        appCompatEditText.perform(click());

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.editTitle),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                0),
                        isDisplayed()));
        appCompatEditText2.perform(replaceText("test"), closeSoftKeyboard());

        ViewInteraction appCompatEditText3 = onView(
                allOf(withId(R.id.editText),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                1),
                        isDisplayed()));
        appCompatEditText3.perform(replaceText("test"), closeSoftKeyboard());

        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.SaveNoteButton), withText("Create Note"),
                        isDisplayed()));
        appCompatButton.perform(click());





        DataInteraction linearLayoutCompat = onData(anything())
                .inAdapterView(allOf(withId(R.id.node_list),
                        childAtPosition(
                                withClassName(is("android.support.constraint.ConstraintLayout")),
                                0)))
                .atPosition(0);
        linearLayoutCompat.perform(click());

        ViewInteraction appCompatList = onView(
                allOf(withId(R.id.node_list),
                        isDisplayed()));




        appCompatList.check(ViewAssertions.matches(Matchers.withListSize(noteSize)));

        //Thread.sleep(10000);


    }



}