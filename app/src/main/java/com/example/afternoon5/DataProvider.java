package com.example.afternoon5;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.util.Log;

import com.example.afternoon5.HelperClasses.Note;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileOutputStream;
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

    public void exportToExternalStorage(ArrayList<Note> notes)
    {
        File dir  = new File(Environment.getExternalStorageDirectory(), "NoteExport");
        if (!dir.mkdirs()) {
            Log.e("DATA", "Directory not created");
        }
        for(Note note : notes)
        {
            File file = noteToFile(note, dir);

        }


    }
    private File noteToFile(Note note, File path)
    {
        File file = new File(path, Integer.toString(note.hashCode()));
        try {

        FileOutputStream stream = new FileOutputStream(file);
        try {
            Gson gson = new Gson();
            stream.write(gson.toJson(note).getBytes());
        } finally {
            stream.close();
        }}
        catch (Exception e)
        {

        }
        return file;

    }



}
