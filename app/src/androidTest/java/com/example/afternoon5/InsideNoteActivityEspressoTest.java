package com.example.afternoon5;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.EditText;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isClickable;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;


@RunWith(AndroidJUnit4.class)
public class InsideNoteActivityEspressoTest {
    @Rule
    public ActivityTestRule<MainActivity> activityTestRule = new ActivityTestRule<>(MainActivity.class);


    public void createNewListElement(final String node_title, final String node_text) throws Throwable
    {
        //add new List element, set title and text of node of the new node
        activityTestRule.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                activityTestRule.getActivity().addListElement(new note_obj(node_title, node_text));
            }
        });
    }

    @Test
    public void testNodeContentAndClickableOnMainActivity() throws Throwable {

        final String node_title = "new_node_title";
        final String node_text = "new_node_text";

        createNewListElement(node_title, node_text);

        //Check if displayed on MainActivity
        onView(withText(node_title)).check(matches(isDisplayed()));
        onView(withText(node_text)).check(matches(isDisplayed()));


        //Check if Node Clickable
        onView(withText(node_title)).check(matches(isClickable()));
    }

    @Test
    public void testCheckContentAfterClickedNodeInMainActivity() throws Throwable {
        final String node_title = "new_node_title";
        final String node_text = "new_node_text";

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

        final String node_title = "new_node_title";
        final String node_text = "new_node_text";

        //Activity: MainActivity

        createNewListElement(node_title, node_text);

        //Click on the new Node to get into the InsideNodeView Activity, where you are able to see and edit the node
        onView(withText(node_title)).perform(click());

        //Activity: InsideNode

        final String node_title_new = "new_node_title_changed";

        //Change Title of Node
        onView(withId(R.id.Title)).perform(typeText(node_title_new));

        //Go Back to Activity Main
        pressBack();

        //Check if new Title is displayed on Activity Main
        onView(withText(node_title_new)).check(matches(isDisplayed()));
    }

}