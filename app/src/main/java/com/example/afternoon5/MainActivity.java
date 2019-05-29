package com.example.afternoon5;


import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;


import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;

import com.example.afternoon5.HelperClasses.Note;
import com.example.afternoon5.HelperClasses.list_adapter;

import org.w3c.dom.Node;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {



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
        list.setOnItemClickListener((parent, view, position, id) -> {
            //ViewEditNoteActivity.callIntentwithExtra(MainActivity.this, DataProvider.getInstance().getNotes().get(position));
            Log.i("MAIN", "FUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUU");
            Intent intent = new Intent(getBaseContext(), ViewEditNoteActivity.class);
            intent.putExtra("position" ,position);
            startActivity(intent);

        });

    }

    public void openCreateNote(View view) {
        Intent intent = new Intent(this, CreateNoteActivity.class);
        startActivity(intent);

    }


    public void onCheckboxClicked(View view)
    {
        final ListView list = (ListView) findViewById(R.id.node_list);
        Note newNote = new Note();

        for (int i = 0; i < list.getCount(); i++) {
            View view1 = list.getChildAt(i);
            if (((CheckBox) view1.findViewById(R.id.checkBox2)).isChecked())

            {
                int number = i;
                String numberAsString = Integer.toString(number);
                Log.d("Meine_ID", numberAsString);
                ArrayList <Note> old_notes = DataProvider.getInstance().getNotes();
                old_notes.get(i).setPinn(true);


            }

            else
            {
                ArrayList <Note> old_notes = DataProvider.getInstance().getNotes();
                old_notes.get(i).setPinn(false);
            }
        }
        DataProvider.getInstance().save(this);
    }



    public void refreshList() {
    final ListView list = findViewById(R.id.node_list);
    final list_adapter adapter = new list_adapter(this,DataProvider.getInstance().getNotes());
    list.setAdapter(adapter);
    }
}

