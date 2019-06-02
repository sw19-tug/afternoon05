package com.example.afternoon5;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.app.AppCompatDelegate;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Locale;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class NightModeTest {
    @Rule
    public ActivityTestRule<MainActivity> activityTestRule = new ActivityTestRule<>(MainActivity.class);

    private void toggleNightMode() {
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        Context cx = InstrumentationRegistry.getTargetContext();
        String menue_entry = cx.getResources().getString(R.string.night_mode);
        onView(withText(menue_entry)).check(matches(isDisplayed())).perform(click());
    }

    @Test
    public void turnOnNightMode() {
        while (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES)
            toggleNightMode();

        toggleNightMode();
        assertEquals(AppCompatDelegate.getDefaultNightMode(), AppCompatDelegate.MODE_NIGHT_YES);
    }

    @Test
    public void turnOffNightMode() {
        while (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_NO)
            toggleNightMode();

        toggleNightMode();
        assertEquals(AppCompatDelegate.getDefaultNightMode(), AppCompatDelegate.MODE_NIGHT_NO);
    }
}
