package com.example.afternoon5;

import android.support.test.espresso.Espresso;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.closeSoftKeyboard;
import static android.support.test.espresso.Espresso.onView;
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

    private final String SPINNER_SELECTABLE_SORT_BY_TITLE = "Sort by Title";
    private final String SPINNER_SELECTABLE_SORT_BY_CREATION_DATE = "Sort by Creation Date";
    private final String SPINNER_SELECTABLE_UNSORTED = "Unsorted";

    private void addTestNotes(String title, String text) {
        onView(withId(R.id.createNoteButton)).perform(click());
        onView(withId(R.id.editTitle)).perform(replaceText(title));
        onView(withId(R.id.editText)).perform(replaceText(text));
        closeSoftKeyboard();
        onView(withId(R.id.SaveNoteButton)).check(matches(isClickable())).perform(click());
    }

    private void setSpinnerToMode(String spinnermode) {
        onView(withId(R.id.spinner1)).perform(click());
        Espresso.onData(allOf(is(instanceOf(String.class)), is(spinnermode))).perform(click());
        onView(withId(R.id.spinner1)).check(matches(withSpinnerText(containsString(spinnermode))));
    }

    @Test
    public void testSpinnerVisible() throws InterruptedException {
        onView(withId(R.id.spinner1)).check(matches(isDisplayed()));
    }

    @Test
    public void allValuesSelectable() throws InterruptedException {
        onView(withId(R.id.spinner1)).perform(click());
        Espresso.onData(allOf(is(instanceOf(String.class)), is(SPINNER_SELECTABLE_SORT_BY_TITLE))).perform(click());
        onView(withId(R.id.spinner1)).check(matches(withSpinnerText(containsString(SPINNER_SELECTABLE_SORT_BY_TITLE))));

        onView(withId(R.id.spinner1)).perform(click());
        Espresso.onData(allOf(is(instanceOf(String.class)), is(SPINNER_SELECTABLE_SORT_BY_CREATION_DATE))).perform(click());
        onView(withId(R.id.spinner1)).check(matches(withSpinnerText(containsString(SPINNER_SELECTABLE_SORT_BY_CREATION_DATE))));

        onView(withId(R.id.spinner1)).perform(click());
        Espresso.onData(allOf(is(instanceOf(String.class)), is(SPINNER_SELECTABLE_UNSORTED))).perform(click());
        onView(withId(R.id.spinner1)).check(matches(withSpinnerText(containsString(SPINNER_SELECTABLE_UNSORTED))));
    }

    @Test
    public void testAlphabeticalSortingFunction() throws InterruptedException {
        //Todo: clear notes list before starting with test (not implemented yet and not in scope of this feature)
        addTestNotes("C" + TITLE, "C" + TEXT);
        addTestNotes("B" + TITLE, "B" + TEXT);
        addTestNotes("A" + TITLE, "A" + TEXT);

        setSpinnerToMode(SPINNER_SELECTABLE_SORT_BY_TITLE);

        //Check sorting order
        onView(withText("A" + TITLE)).perform(scrollTo()).check(isAbove(withText("B" + TITLE)));
        onView(withText("C" + TITLE)).perform(scrollTo()).check(isBelow(withText("B" + TITLE)));
        onView(withText("A" + TITLE)).perform(scrollTo()).check(isAbove(withText("C" + TITLE)));
    }

    @Test
    public void testCreationDatelSortingFunction() throws InterruptedException {
        //Todo: clear notes list before starting with test (not implemented yet and not in scope of this feature)
        setSpinnerToMode(SPINNER_SELECTABLE_SORT_BY_TITLE);

        addTestNotes("F" + TITLE, "F" + TEXT);
        addTestNotes("E" + TITLE, "E" + TEXT);
        addTestNotes("D" + TITLE, "D" + TEXT);

        setSpinnerToMode(SPINNER_SELECTABLE_SORT_BY_CREATION_DATE);

        //Check sorting order
        onView(withText("F" + TITLE)).perform(scrollTo()).check(isAbove(withText("E" + TITLE)));
        onView(withText("E" + TITLE)).perform(scrollTo()).check(isAbove(withText("D" + TITLE)));
        onView(withText("D" + TITLE)).perform(scrollTo()).check(isBelow(withText("F" + TITLE)));
    }

    @Test
    public void testAddNoteCreationDatedMode() {
        setSpinnerToMode(SPINNER_SELECTABLE_UNSORTED);

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
        setSpinnerToMode(SPINNER_SELECTABLE_SORT_BY_TITLE);

        addTestNotes("L" + TITLE, TEXT);
        addTestNotes("K" + TITLE, TEXT);
        addTestNotes("J" + TITLE, TEXT);

        //Check sorting order
        onView(withText("J" + TITLE)).perform(scrollTo()).check(isAbove(withText("K" + TITLE)));
        onView(withText("K" + TITLE)).perform(scrollTo()).check(isAbove(withText("L" + TITLE)));
        onView(withText("J" + TITLE)).perform(scrollTo()).check(isAbove(withText("L" + TITLE)));
    }
}