package com.example.afternoon5;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import android.support.v7.widget.Toolbar;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Spinner;

import com.example.afternoon5.HelperClasses.Note;
import com.example.afternoon5.HelperClasses.list_adapter;

import java.util.Collections;
import java.util.Comparator;


public class MainActivity extends AppCompatActivity {
    private Spinner spinner1;
    private list_adapter adapter;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_menu_main_activity, menu);
        MenuItem checkable_menue = menu.findItem(R.id.checkable_sort);
        final SharedPreferences prefs;
        prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        final SharedPreferences.Editor prefEditor = prefs.edit();
        int spinner_value = prefs.getInt("spinner value start", R.id.sort_alphabetical);
        MenuItem sort_option;
        Menu submenue = checkable_menue.getSubMenu();
        sort_option = submenue.findItem(spinner_value);

        sort_option.setChecked(true);

        return true;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar1);

        setSupportActionBar(myToolbar);

        DataProvider.getInstance();
        DataProvider.getInstance().load(this);

        ListView list = (ListView) findViewById(R.id.node_list);
        adapter = new list_adapter(this, DataProvider.getInstance().getNotes());
        list.setAdapter(adapter);

        final boolean check_for_created = true;

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //capital letters will be sorted before small letters
                Collections.sort(DataProvider.getInstance().getNotes(), new Comparator<Note>() {
                    @Override
                    public int compare(Note o1, Note o2) {
                        if (check_for_created) {
                            return o1.getCreationDate().compareTo(o2.getCreationDate());
                        } else {
                            return o1.getTitle().compareTo(o2.getTitle());
                        }
                    }
                });
                adapter.notifyDataSetChanged();
                DataProvider.getInstance().save(getBaseContext());
            }
        });

        final SharedPreferences prefs;
        prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        final SharedPreferences.Editor prefEditor = prefs.edit();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){
            case R.id.sort_creation_date:

                Collections.sort(DataProvider.getInstance().getNotes(), new Comparator<Note>() {
                    @Override
                    public int compare(Note o1, Note o2) {
                        return o1.getCreationDate().compareTo(o2.getCreationDate());
                    }
                });
                break;
            case R.id.sort_alphabetical:

                Collections.sort(DataProvider.getInstance().getNotes(), new Comparator<Note>() {
                    @Override
                    public int compare(Note o1, Note o2) {
                        return o1.getTitle().compareTo(o2.getTitle());
                    }
                });
                break;
            default: return super.onOptionsItemSelected(item);

        }
        item.setChecked(true);
        adapter.notifyDataSetChanged();
        DataProvider.getInstance().save(getBaseContext());

        SharedPreferences prefs;
        prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        final SharedPreferences.Editor prefEditor = prefs.edit();
        prefEditor.putInt("spinner value start", item.getItemId());
        prefEditor.apply();
        return true;
    }
    public void openCreateNote(View view) {
        Intent intent = new Intent(this, CreateNoteActivity.class);
        startActivity(intent);

    }
}
