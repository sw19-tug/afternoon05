package com.example.afternoon5;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.afternoon5.HelperClasses.Note;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Array;
import java.util.ArrayList;

class DataProvider {
    private static final DataProvider ourInstance = new DataProvider();

    static DataProvider getInstance() {
        return ourInstance;
    }

    private ArrayList<Note>notes;
    private ArrayList<String> allTags;


    private DataProvider() {

        notes = new ArrayList<>();
        allTags = new ArrayList<>();

    }

    public void load(Context context)
    {
        SharedPreferences myprefs;
        myprefs = context.getSharedPreferences("com.example.afternoon5", context.MODE_PRIVATE);
        Gson gson = new Gson();
        String jsonText = myprefs.getString("Notes", null);
        ArrayList<Note> notes= (ArrayList<Note>) gson.fromJson(jsonText,
                new TypeToken<ArrayList<Note>>() {
                }.getType());
        if (notes != null)
        {
            this.notes = notes;
        }
        String jsonTags = myprefs.getString("Tags", null);
        ArrayList<String> tags= (ArrayList<String>) gson.fromJson(jsonTags,
                new TypeToken<ArrayList<String>>() {
                }.getType());
        if (tags != null)
        {
            this.allTags = tags;
        }



    }

    public void save(Context context) {


        SharedPreferences myprefs;
        myprefs = context.getSharedPreferences("com.example.afternoon5", context.MODE_PRIVATE);
        SharedPreferences.Editor myeditor = myprefs.edit();
        Gson gson = new Gson();

        String json = gson.toJson(notes);
        String jsonTags = gson.toJson(allTags);

        myeditor.putString("Notes",json);
        myeditor.putString("Tags", jsonTags);
        myeditor.commit();

    }

    public ArrayList<Note> getNotes() {
        return notes;
    }

    public void setNotes(ArrayList<Note> notes) {
        this.notes = notes;
    }

    public void addNoteToNotes(Note note)
    {
        notes.add(note);
    }

    public ArrayList<String> getAllTags() {
        return allTags;
    }

    public void setAllTags(ArrayList<String> allTags) {
        this.allTags = allTags;
    }
}
