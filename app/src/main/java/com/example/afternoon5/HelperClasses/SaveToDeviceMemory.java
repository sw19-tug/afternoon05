package com.example.afternoon5.HelperClasses;

import android.content.SharedPreferences;

import com.google.gson.Gson;

public class SaveToDeviceMemory  implements DataInterface{

    public SaveToDeviceMemory()
    {

    }



    @Override
    public void SaveNote(Note note) {
        SharedPreferences myprefs;
        myprefs = DataProvider.getInstance().getContext().getSharedPreferences("com.example.afternoon5", DataProvider.getInstance().getContext().MODE_PRIVATE);
        SharedPreferences.Editor myeditor = myprefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(note);
        myeditor.putString(note.getTitle(),json);

    }
}

