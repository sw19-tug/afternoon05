package com.example.afternoon5;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private final ArrayList<note_obj> pairs = new ArrayList<note_obj>();
    private Context cx;
    private static note_obj objectToAdd;

    static void callIntentwithObjectToPushIntoList(Context cx, note_obj object) {
        objectToAdd = object;
        Intent intent = new Intent(cx, MainActivity.class);
        cx.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Todo: Serialize pairs list from file
        if (objectToAdd != null) {
            pairs.add(objectToAdd);
            objectToAdd = null;
            //Todo: Serialize pairs list into file
        } else {
            pairs.add(new note_obj("Title0", "Text0"));
        }


        ListView list = (ListView) findViewById(R.id.node_list);

        list_adapter adapter = new list_adapter(this, pairs);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent,
                                    View view, int position, long id) {
                ViewEditNote.callIntentwithExtra(MainActivity.this, pairs.remove(position));
                refreshList();
            }
        });
    }

    public void addListElement(note_obj object) {
        pairs.add(object);
        refreshList();
    }

    private void refreshList() {
        ListView list = (ListView) findViewById(R.id.node_list);
        list_adapter adapter = new list_adapter(this, pairs);
        list.setAdapter(adapter);
    }

}
