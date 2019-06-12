package com.example.afternoon5;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.afternoon5.HelperClasses.Note;



public class CreateNoteActivity extends AppCompatActivity {

    private Activity thisActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);
        this.thisActivity = this;


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, DataProvider.getInstance().getAllTags());
        MultiAutoCompleteTextView textView = findViewById(R.id.tagsTextView);
        textView.setAdapter(adapter);
        textView.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());


        //Go Back on Android Logo Label Click
        ActionBar act_bar = getSupportActionBar();
        if (act_bar != null) {
            act_bar.setDisplayShowTitleEnabled(false);
            act_bar.setDisplayShowCustomEnabled(true);

            View toolbarView = getLayoutInflater().inflate(R.layout.toolbar_custom, null);
            TextView title_bar = toolbarView.findViewById(R.id.toolbar_title);


            Intent mainIntent = new Intent(CreateNoteActivity.this, MainActivity.class);
            mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            title_bar.setOnClickListener(v -> this.startActivity(mainIntent));
            act_bar.setCustomView(toolbarView);
        }

        Switch onOffSwitch = (Switch)  findViewById(R.id.geoTagSwitch);
        onOffSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

               if(isChecked)
               {
                   if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                           == PackageManager.PERMISSION_GRANTED) {

                   } else {

                       ActivityCompat.requestPermissions(thisActivity,
                               new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                               200);
                   }
               }

            }

        });

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 200: {
                if(grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Switch onOffSwitch = (Switch)  findViewById(R.id.geoTagSwitch);
                    onOffSwitch.setChecked(false);
                }
            }
        }
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


        Switch onOffSwitch = (Switch)  findViewById(R.id.geoTagSwitch);
        String location ="";
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            if(onOffSwitch.isChecked()) {
                LocationManager locationManager = (LocationManager) getSystemService(getApplicationContext().LOCATION_SERVICE);
                Location locationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                location = locationGPS.getLatitude() + ":" + locationGPS.getLongitude();
            }
        }


        DataProvider.getInstance().addNoteToNotes(new Note(title, text, tags, false, location));

        DataProvider.getInstance().save(this);

        CharSequence save = "Note saved";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(this, save, duration);
        toast.show();

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

    }
}
