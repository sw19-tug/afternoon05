package com.example.afternoon5;

import android.support.test.espresso.intent.Intents;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.afternoon5.HelperClasses.Note;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Date;

import static android.support.test.espresso.Espresso.closeSoftKeyboard;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isClickable;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;

@RunWith(AndroidJUnit4.class)
public class InsideNoteActivityEspressoTest {
    @Rule
    public IntentsTestRule<MainActivity> activityTestRule = new IntentsTestRule<>(MainActivity.class);


    public void createNewListElement(final String node_title, final String node_text, final boolean pinned) throws Throwable {

        activityTestRule.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                DataProvider.getInstance().addNoteToNotes(new Note(node_title, node_text, pinned, new Date()));
                activityTestRule.getActivity().refreshList();
            }
        });
    }

    @Test
    public void testNodeContentAndClickableOnMainActivity() throws Throwable {
        DataProvider.getInstance().setNotes(new ArrayList<>());

        final String node_title = "new_node_title_01";
        final String node_text = "new_node_text_01";
        final boolean pinned = false;

        createNewListElement(node_title, node_text, pinned);


        onView(withText(node_title)).check(matches(isDisplayed()));
        onView(withText(node_text)).check(matches(isDisplayed()));


        onView(withText(node_title)).perform(click());


        intended(hasComponent(ViewEditNoteActivity.class.getName()));
    }

    @Test
    public void testCheckContentAfterClickedNodeInMainActivity() throws Throwable {
        DataProvider.getInstance().setNotes(new ArrayList<>());
        final String node_title = "new_node_title_02";
        final String node_text = "new_node_text_02";
        final boolean pinned = false;


        createNewListElement(node_title, node_text, pinned);

        onView(withText(node_title)).perform(click());

        onView(withText(node_title)).check(matches(isDisplayed()));
        onView(withText(node_text)).check(matches(isDisplayed()));

        closeSoftKeyboard();

        onView(withId(R.id.button_safe)).check(matches(isDisplayed()));
        onView(withId(R.id.button_safe)).check(matches(isClickable()));
    }


    @Test
    public void testEditNodeTitleCheckActivityAfterBack() throws Throwable {
        DataProvider.getInstance().setNotes(new ArrayList<>());

        final String node_title = "new_node_title_03";
        final String node_text = "new_node_text_03";
        final boolean pinned = false;



        createNewListElement(node_title, node_text, pinned);

        onView(withText(node_title)).perform(click());



        final String node_title_new = "new_node_title_changed";


        onView(withId(R.id.Title)).perform(replaceText(node_title_new));


        pressBack();


        onView(withText(node_title_new)).check(matches(isDisplayed()));
    }

}