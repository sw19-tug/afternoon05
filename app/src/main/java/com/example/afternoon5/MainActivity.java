package com.example.afternoon5;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.afternoon5.HelperClasses.Note;
import com.example.afternoon5.HelperClasses.list_adapter;

import java.util.Collections;
import java.util.Comparator;


public class MainActivity extends AppCompatActivity {
    private Spinner spinner1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DataProvider.getInstance();
        DataProvider.getInstance().load(this);

        ListView list = (ListView) findViewById(R.id.node_list);
        final list_adapter adapter = new list_adapter(this, DataProvider.getInstance().getNotes());
        list.setAdapter(adapter);

        final boolean check_for_created = true;

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,int position, long id) {

                //capital letters will be sorted before small letters
                Collections.sort(DataProvider.getInstance().getNotes(), new Comparator<Note>() {
                    @Override
                    public int compare(Note o1, Note o2) {
                        if(check_for_created){
                            return o1.getCreationDate().compareTo(o2.getCreationDate());
                        }else {
                            return o1.getTitle().compareTo(o2.getTitle());
                        }
                    }
                });


                adapter.notifyDataSetChanged();
                DataProvider.getInstance().save(getBaseContext());
            }
        });
        spinner1 = findViewById(R.id.spinner1);
        final SharedPreferences prefs;
        prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        final SharedPreferences.Editor prefEditor = prefs.edit();
        int spinner_value = prefs.getInt("spinner value start",2);

        spinner1.setSelection(spinner_value);
        //AdapterView.OnItemSelectedListener(this);
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(MainActivity.this, "position= "+position, Toast.LENGTH_LONG).show();
                if(position == 1){ //sort by Createtion Date
                    Collections.sort(DataProvider.getInstance().getNotes(), new Comparator<Note>() {
                        @Override
                        public int compare(Note o1, Note o2) {
                            return o1.getCreationDate().compareTo(o2.getCreationDate());
                        }
                    });
                }else if(position == 0){ //sort by Title
                    Collections.sort(DataProvider.getInstance().getNotes(), new Comparator<Note>() {
                        @Override
                        public int compare(Note o1, Note o2) {
                            return o1.getTitle().compareTo(o2.getTitle());
                        }
                    });
                }
                adapter.notifyDataSetChanged();
                DataProvider.getInstance().save(getBaseContext());


                prefEditor.putInt("spinner value start", position);
                prefEditor.commit();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });
       // addListenerOnSpinnerItemSelection();

/*
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,int position, long id) {

                Note note = (Note)parent.getItemAtPosition(position);

                //note.getText();
                DataProvider.getInstance().getNotes().remove(note);
                adapter.notifyDataSetChanged();
                DataProvider.getInstance().save(getBaseContext());
            }
        });
*/

    }

    /*public void addListenerOnSpinnerItemSelection() {
        spinner1 = (Spinner) findViewById(R.id.spinner1);
        spinner1.setOnItemSelectedListener(new CustomOnItemSelectedListener());
    }*/

    public void openCreateNote(View view) {
        Intent intent = new Intent(this, CreateNoteActivity.class);
        startActivity(intent);

    }



}
