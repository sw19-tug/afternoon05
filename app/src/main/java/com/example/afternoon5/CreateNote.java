package com.example.afternoon5;

import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.afternoon5.HelperClasses.Note;
import com.google.gson.Gson;

import java.util.ArrayList;


public class CreateNote extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);
    }

    public void createNote(View view) {


        String title = ((EditText)findViewById(R.id.editTitle)).getText().toString();
        String text = ((EditText)findViewById(R.id.editText)).getText().toString();

        DataProvider.getInstance().addNoteToNotes(new Note(title, text));
        DataProvider.getInstance().save(this);

        CharSequence save = "Note saved";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(this, save, duration);
        toast.show();

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

    }
}
