package com.example.afternoon5;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.closeSoftKeyboard;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.PositionAssertions.isAbove;
import static android.support.test.espresso.assertion.PositionAssertions.isBelow;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isClickable;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.allOf;
import static org.junit.matchers.JUnitMatchers.containsString;


@RunWith(AndroidJUnit4.class)
public class NoteSortingEspressoTest {
    @Rule
    public ActivityTestRule<MainActivity> activityTestRule = new ActivityTestRule<>(MainActivity.class);

    private final String TITLE = "veryUniqueTestingStringOne";
    private final String TEXT = "veryUniqueTestingStringTwo";

    private final int SPINNER_SELECTABLE_SORT_BY_TITLE = R.string.alphabetical;
    private final int SPINNER_SELECTABLE_SORT_BY_CREATION_DATE = R.string.creation_date;

    public static void addTestNotes(String title, String text) {
        try {
            onView(withText(title)).check(matches(isDisplayed()));
            onView(withText(title)).check(matches(isDisplayed()));
        } catch (NoMatchingViewException e) {
            onView(withId(R.id.createNoteButton)).perform(click());
            onView(withId(R.id.editTitle)).perform(replaceText(title));
            onView(withId(R.id.editText)).perform(replaceText(text));
            closeSoftKeyboard();
            onView(withId(R.id.SaveNoteButton)).check(matches(isClickable())).perform(click());
        }
    }

    private void setSortMode(int SelectableId) {
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        //onView(withId(R.id.checkable_sort)).perform(click());
        Context cx = InstrumentationRegistry.getTargetContext();
        String menue_entry = cx.getResources().getString(R.string.storting_menue);
        onView(withText(menue_entry)).check(matches(isDisplayed())).perform(click());

        String menue_entry2 = cx.getResources().getString(SelectableId);
        onView(withText(menue_entry2)).check(matches(isDisplayed())).perform(click());
    }

    private void addTestNodes() {
        addTestNotes("C" + TITLE, "C" + TEXT);
        addTestNotes("B" + TITLE, "B" + TEXT);
        addTestNotes("A" + TITLE, "A" + TEXT);
        addTestNotes("E" + TITLE, "E" + TEXT);
        addTestNotes("D" + TITLE, "D" + TEXT);
    }

    @Test
    public void testMenuIsDisplayed() throws InterruptedException {
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        Context cx = InstrumentationRegistry.getTargetContext();
        String menu_entry = cx.getResources().getString(R.string.storting_menue);
        onView(withText(menu_entry)).check(matches(isDisplayed())).perform(click());
        String TitleSortByCreationDate = cx.getResources().getString(SPINNER_SELECTABLE_SORT_BY_CREATION_DATE);
        String TitleSortByTitle = cx.getResources().getString(SPINNER_SELECTABLE_SORT_BY_TITLE);


        onView(withText(TitleSortByCreationDate)).check(matches(isDisplayed()));
        onView(withText(TitleSortByTitle)).check(matches(isDisplayed()));
    }

    @Test
    public void testAlphabeticalSortingFunction() throws InterruptedException {
        setSortMode(SPINNER_SELECTABLE_SORT_BY_CREATION_DATE);
        addTestNodes();
        setSortMode(SPINNER_SELECTABLE_SORT_BY_TITLE);

        //Check sorting order
        onView(withText("A" + TITLE)).perform(scrollTo()).check(isAbove(withText("B" + TITLE)));
        onView(withText("C" + TITLE)).perform(scrollTo()).check(isBelow(withText("B" + TITLE)));
        onView(withText("A" + TITLE)).perform(scrollTo()).check(isAbove(withText("C" + TITLE)));
    }

    @Test
    public void testCreationDateSortingFunction() throws InterruptedException {
        setSortMode(SPINNER_SELECTABLE_SORT_BY_TITLE);
        addTestNodes();
        setSortMode(SPINNER_SELECTABLE_SORT_BY_CREATION_DATE);

        //Check sorting order
        onView(withText("C" + TITLE)).perform(scrollTo()).check(isAbove(withText("B" + TITLE)));
        onView(withText("B" + TITLE)).perform(scrollTo()).check(isAbove(withText("A" + TITLE)));
        onView(withText("A" + TITLE)).perform(scrollTo()).check(isAbove(withText("E" + TITLE)));
        onView(withText("E" + TITLE)).perform(scrollTo()).check(isAbove(withText("D" + TITLE)));
    }

    @Test
    public void testAddNoteCreationDatedMode() {
        setSortMode(SPINNER_SELECTABLE_SORT_BY_CREATION_DATE);

        addTestNotes("I" + TITLE, "I" + TEXT);
        addTestNotes("H" + TITLE, "H" + TEXT);
        addTestNotes("G" + TITLE, "G" + TEXT);

        //Check sorting order
        onView(withText("I" + TITLE)).perform(scrollTo()).check(isAbove(withText("H" + TITLE)));
        onView(withText("G" + TITLE)).perform(scrollTo()).check(isBelow(withText("I" + TITLE)));
        onView(withText("H" + TITLE)).perform(scrollTo()).check(isAbove(withText("G" + TITLE)));
    }

    @Test
    public void testAddNoteSortByTitleMode() {
        setSortMode(SPINNER_SELECTABLE_SORT_BY_TITLE);

        addTestNotes("L" + TITLE, TEXT);
        addTestNotes("K" + TITLE, TEXT);
        addTestNotes("J" + TITLE, TEXT);

        //Check sorting order
        onView(withText("J" + TITLE)).perform(scrollTo()).check(isAbove(withText("K" + TITLE)));
        onView(withText("K" + TITLE)).perform(scrollTo()).check(isAbove(withText("L" + TITLE)));
        onView(withText("J" + TITLE)).perform(scrollTo()).check(isAbove(withText("L" + TITLE)));
    }
}