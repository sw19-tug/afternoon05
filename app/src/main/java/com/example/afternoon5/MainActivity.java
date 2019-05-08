package com.example.afternoon5;


import android.content.Intent;
import android.graphics.Color;
import android.os.Environment;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;



import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;

import com.example.afternoon5.HelperClasses.Note;
import com.example.afternoon5.HelperClasses.list_adapter;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    Menu menu;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        this.menu = menu;
        return true;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DataProvider.getInstance();
        DataProvider.getInstance().load(this);

        final ListView list = (ListView) findViewById(R.id.node_list);

        final list_adapter adapter = new list_adapter(this, DataProvider.getInstance().getNotes());
        list.setAdapter(adapter);
        list.setItemsCanFocus(false);
        list.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           int pos, long id) {
                // TODO Auto-generated method stub
                Log.i("MAIN", "TEST");

                for (int i = 0; i < list.getCount(); i++)
                {
                    // Log.i("MAIN", "DEBUG: " + list.getAdapter().get);
                    // list.getAdapter().getView(i, parent, null).findViewById(R.id.export_checkbox).setVisibility(View.VISIBLE);

                    View view1  = list.getChildAt(i);
                    view1.findViewById(R.id.export_checkbox).setVisibility(View.VISIBLE);
                    //findViewById(R.id.action_export).setVisibility(View.VISIBLE);


                }
                menu.findItem(R.id.action_export).setVisible(true);
                menu.findItem(R.id.action_cancel).setVisible(true);
                return true;
            }
        });



        list.setOnItemClickListener((parent, view, position, id) -> {
            //ViewEditNoteActivity.callIntentwithExtra(MainActivity.this, DataProvider.getInstance().getNotes().get(position));

            Intent intent = new Intent(getBaseContext(), ViewEditNoteActivity.class);
            intent.putExtra("position" ,position);
            startActivity(intent);

        });


    }


    public void closeExport(MenuItem menuItem) {
        final ListView list = (ListView) findViewById(R.id.node_list);
        for (int i = 0; i < list.getCount(); i++)
        {
            // Log.i("MAIN", "DEBUG: " + list.getAdapter().get);
            // list.getAdapter().getView(i, parent, null).findViewById(R.id.export_checkbox).setVisibility(View.VISIBLE);

            View view1  = list.getChildAt(i);
            view1.findViewById(R.id.export_checkbox).setVisibility(View.GONE);
            //findViewById(R.id.action_export).setVisibility(View.VISIBLE);


        }
        menu.findItem(R.id.action_export).setVisible(false);
        menu.findItem(R.id.action_cancel).setVisible(false);

    }
    public void exportSelectedNotes(MenuItem menuItem) {
        final ListView list = (ListView) findViewById(R.id.node_list);
        ArrayList<Note> listtoExport = new ArrayList<>();
        for (int i = 0; i < list.getCount(); i++)
        {
            // Log.i("MAIN", "DEBUG: " + list.getAdapter().get);
            // list.getAdapter().getView(i, parent, null).findViewById(R.id.export_checkbox).setVisibility(View.VISIBLE);

            View view1  = list.getChildAt(i);
            if (((CheckBox)view1.findViewById(R.id.export_checkbox)).isChecked())
                listtoExport.add(DataProvider.getInstance().getNotes().get(i));
        }
        if(isExternalStorageWritable())
        {

        }


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

    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

}
