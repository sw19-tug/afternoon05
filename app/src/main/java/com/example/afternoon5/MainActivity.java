package com.example.afternoon5;


import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;



import android.support.v7.widget.Toolbar;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.afternoon5.HelperClasses.Note;
import com.example.afternoon5.HelperClasses.list_adapter;

import java.util.Collections;
import java.util.Comparator;


public class MainActivity extends AppCompatActivity {
    private list_adapter adapter;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_menu_main_activity, menu);
        MenuItem checkable_menue = menu.findItem(R.id.checkable_sort);
        int spinner_value = getSortingPreference();
        Menu submenue = checkable_menue.getSubMenu();
        MenuItem sort_option = submenue.findItem(spinner_value);

        sort_option.setChecked(true);

        return true;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar1);

        setSupportActionBar(myToolbar);

        DataProvider.getInstance().load(this);

        final ListView list = (ListView) findViewById(R.id.node_list);

        adapter = new list_adapter(this, DataProvider.getInstance().getNotes());
        list.setAdapter(adapter);
        list.setItemsCanFocus(false);
        list.setOnItemClickListener((parent, view, position, id) -> {
            //ViewEditNoteActivity.callIntentwithExtra(MainActivity.this, DataProvider.getInstance().getNotes().get(position));
            Log.i("MAIN", "FUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUU");
            Intent intent = new Intent(getBaseContext(), ViewEditNoteActivity.class);
            intent.putExtra("position" ,position);
            startActivity(intent);

        });

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
        prefEditor.apply();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id != R.id.sort_alphabetical && id != R.id.sort_creation_date) {
            return super.onOptionsItemSelected(item);
        }

        sortList(item.getItemId());
        item.setChecked(true);
        DataProvider.getInstance().save(getBaseContext());

        SharedPreferences prefs;
        prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        final SharedPreferences.Editor prefEditor = prefs.edit();
        prefEditor.putInt("spinner value start", item.getItemId());
        prefEditor.apply();
        return true;
    }

    private void sortList(int item_id) {
        Comparator<Note> m_list_gradation;
        switch (item_id) {
            case R.id.sort_creation_date:
                m_list_gradation = new Comparator<Note>() {
                    @Override
                    public int compare(Note o1, Note o2) {
                        return o1.getCreationDate().compareTo(o2.getCreationDate());
                    }
                };
                break;
            case R.id.sort_alphabetical:
                m_list_gradation = new Comparator<Note>() {
                    @Override
                    public int compare(Note o1, Note o2) {
                        return o1.getTitle().compareTo(o2.getTitle());
                    }
                };
                break;
            default:
                return;
        }


        Collections.sort(DataProvider.getInstance().getNotes(), m_list_gradation);
        adapter.notifyDataSetChanged();
    }




    public void openCreateNote(View view) {
        Intent intent = new Intent(this, CreateNoteActivity.class);
        startActivity(intent);
    }


    public void refreshList() {
    final ListView list = findViewById(R.id.node_list);
    final list_adapter adapter = new list_adapter(this,DataProvider.getInstance().getNotes());
    list.setAdapter(adapter);
    }
    @Override
    protected void onResume() {
        super.onResume();
        sortList(getSortingPreference());
    }

    private int getSortingPreference() {
        final SharedPreferences prefs;
        prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        int value = prefs.getInt("spinner value start", R.id.sort_alphabetical);
        return value;
    }
}
