package com.example.afternoon5;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;


import android.widget.ListView;

import com.example.afternoon5.HelperClasses.list_adapter;


public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DataProvider.getInstance();
        DataProvider.getInstance().load(this);

        ListView list = (ListView) findViewById(R.id.node_list);

        list_adapter adapter = new list_adapter(this, DataProvider.getInstance().getNotes());
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent,
                                    View view, int position, long id) {
                //ViewEditNoteActivity.callIntentwithExtra(MainActivity.this, DataProvider.getInstance().getNotes().get(position));
                Intent intent = new Intent(getBaseContext(), ViewEditNoteActivity.class);
                intent.putExtra("position" ,position);
                startActivity(intent);

            }
        });
    }




    public void openCreateNote(View view) {
        Intent intent = new Intent(this, CreateNoteActivity.class);
        startActivity(intent);

    }
}
