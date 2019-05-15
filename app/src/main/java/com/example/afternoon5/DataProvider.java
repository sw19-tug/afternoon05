package com.example.afternoon5;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.afternoon5.HelperClasses.Note;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

class DataProvider {
    private static final DataProvider ourInstance = new DataProvider();

    static DataProvider getInstance() {
        return ourInstance;
    }

    private ArrayList<Note>notes;



    private DataProvider() {

        notes = new ArrayList<>();


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



    }

    public void save(Context context) {


        SharedPreferences myprefs;
        myprefs = context.getSharedPreferences("com.example.afternoon5", context.MODE_PRIVATE);
        SharedPreferences.Editor myeditor = myprefs.edit();
        Gson gson = new Gson();

        String json = gson.toJson(notes);
        myeditor.putString("Notes",json);


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

    public ArrayList<String> getAllTags()
    {
        ArrayList<String> tags = new ArrayList<>();
        for (Note note : notes)
        {
            for(String s : note.getTags())
            {
                if(!tags.contains(s))
                {
                    tags.add(s);
                }
            }
        }
        return tags;
    }



}
