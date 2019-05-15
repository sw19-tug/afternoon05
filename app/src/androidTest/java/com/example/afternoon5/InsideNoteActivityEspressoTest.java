package com.example.afternoon5;

import android.support.test.espresso.intent.Intents;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.afternoon5.HelperClasses.Note;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Date;

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


    public void createNewListElement(final String node_title, final String node_text) throws Throwable
    {
        //add new List element, set title and text of node of the new node
        activityTestRule.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //activityTestRule.getActivity().addListElement(new Note(node_title, node_text));

                DataProvider.getInstance().addNoteToNotes(new Note(node_title, node_text, new Date()));
                activityTestRule.getActivity().refreshList();
            }
        });
    }

    @Test
    public void testNodeContentAndClickableOnMainActivity() throws Throwable {

        final String node_title = "new_node_title_01";
        final String node_text = "new_node_text_01";

        createNewListElement(node_title, node_text);

        //Check if displayed on MainActivity
        onView(withText(node_title)).check(matches(isDisplayed()));
        onView(withText(node_text)).check(matches(isDisplayed()));

        //Check if Node Clickable
        onView(withText(node_title)).perform(click());

        //Check if Activity Changed after Click on Node Title
        intended(hasComponent(ViewEditNoteActivity.class.getName()));
    }

    @Test
    public void testCheckContentAfterClickedNodeInMainActivity() throws Throwable {
        final String node_title = "new_node_title_02";
        final String node_text = "new_node_text_02";

        //Activity: MainActivity

        createNewListElement(node_title, node_text);

        //Click on the new Node to get into the InsideNodeView Activity, where you are able to see and edit the node
        onView(withText(node_title)).perform(click());

        //Activity: InsideNodeActivity

        //Check in new opened Activity, if the title and test is the same as in the Activity before
        onView(withText(node_title)).check(matches(isDisplayed()));
        onView(withText(node_text)).check(matches(isDisplayed()));

        //Check if Safe Button showing and clickable
        onView(withId(R.id.button_safe)).check(matches(isDisplayed()));
        onView(withId(R.id.button_safe)).check(matches(isClickable()));
    }


    @Test
    public void testEditNodeTitleCheckActivityAfterBack() throws Throwable {

        final String node_title = "new_node_title_03";
        final String node_text = "new_node_text_03";

        //Activity: MainActivity

        createNewListElement(node_title, node_text);

        //Click on the new Node to get into the InsideNodeView Activity, where you are able to see and edit the node
        onView(withText(node_title)).perform(click());

        //Activity: InsideNode

        final String node_title_new = "new_node_title_changed";

        //Change Title of Node
        onView(withId(R.id.Title)).perform(replaceText(node_title_new));

        //Go Back to Activity Main
        pressBack();

        //Check if new Title is displayed on Activity Main
        onView(withText(node_title_new)).check(matches(isDisplayed()));
    }

}