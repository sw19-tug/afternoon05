package com.example.afternoon5;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.example.afternoon5.HelperClasses.Note;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class LanguageTest {
    @Test
    public void checkDisplayedLanguage() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();
        String stringToTest = appContext.getString(R.string.night_mode);
        String testString = "";


        Locale local = Locale.getDefault();
        String language = local.getDisplayLanguage();
        if (language.equals(local.getDisplayLanguage(Locale.GERMAN)) ||
                language.equals(local.getDisplayLanguage(Locale.GERMANY))) {
            testString = "darkmode";
        } else if (language.equals(local.getDisplayLanguage(Locale.ENGLISH))) {
            testString = "darkmode";
        } else {
            assertTrue("device language not known", true);
        }
        assertEquals(stringToTest, testString);
    }
}
