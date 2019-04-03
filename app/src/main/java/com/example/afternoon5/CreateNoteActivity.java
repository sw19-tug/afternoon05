package com.example.afternoon5;

import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Toast;

import com.example.afternoon5.HelperClasses.Note;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;


public class CreateNoteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);



        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, DataProvider.getInstance().getAllTags());
        MultiAutoCompleteTextView textView = findViewById(R.id.tagsTextView);
        textView.setAdapter(adapter);
        textView.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());

    }

    public void createNote(View view) {



        String title = ((EditText)findViewById(R.id.editTitle)).getText().toString();
        String text = ((EditText)findViewById(R.id.editText)).getText().toString();
        String tagString = ((MultiAutoCompleteTextView)findViewById(R.id.tagsTextView)).getText().toString();

        if(title.isEmpty() || text.isEmpty())
        {
            Toast toast = Toast.makeText(this, "Title or Text can't be empty", Toast.LENGTH_LONG);
            toast.show();
            return;
        }
        tagString = tagString.replaceAll(" ", "");
        if(tagString.endsWith(","))
        {
            tagString = tagString.substring(0, tagString.length()-1);
        }
        String[] tags =tagString.split(",");
        DataProvider.getInstance().addNoteToNotes(new Note(title, text, tags));

        DataProvider.getInstance().save(this);

        CharSequence save = "Note saved";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(this, save, duration);
        toast.show();

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

    }
}
