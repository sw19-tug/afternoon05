package com.example.afternoon5;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DataProvider.getInstance();
        DataProvider.getInstance().load(this);


    }

    public void openCreateNote(View view) {
        Intent intent = new Intent(this, CreateNote.class);
        startActivity(intent);


    }
}
