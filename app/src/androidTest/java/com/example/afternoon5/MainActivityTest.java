package com.example.afternoon5;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.example.afternoon5.HelperClasses.Note;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {
    @Test
    public void createNoteTest() {

        Context appContext = InstrumentationRegistry.getTargetContext();

        SharedPreferences settings = appContext.getSharedPreferences("com.example.afternoon5", appContext.MODE_PRIVATE);
        Gson gson = new Gson();
        TypeToken<ArrayList<Note>> token = new TypeToken<ArrayList<Note>>() {
        };
        ArrayList<Note> notes = gson.fromJson(settings.getString("Notes", ""), token.getType());
        assertNotNull(notes);
        assertTrue(notes.size() >= 1);
    }
}
