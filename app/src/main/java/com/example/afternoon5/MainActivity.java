package com.example.afternoon5;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;


import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private final ArrayList<note_obj> pairs = new ArrayList<note_obj>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        DataProvider.getInstance();
        DataProvider.getInstance().load(this);
        pairs.add(new note_obj("Title0", "Text0"));
        pairs.add(new note_obj("Title1", "Text1"));
        pairs.add(new note_obj("Title2", "Text2"));



        ListView list = (ListView) findViewById(R.id.node_list);

        list_adapter adapter = new list_adapter(this, pairs);
        list.setAdapter(adapter);

    }

    public void openCreateNote(View view) {
        Intent intent = new Intent(this, CreateNote.class);
        startActivity(intent);

    }
    public void addListElement(note_obj object)
    {
        pairs.add(object);
        ListView list = (ListView) findViewById(R.id.node_list);
        list_adapter adapter = new list_adapter(this, pairs);
        list.setAdapter(adapter);

    }

}
