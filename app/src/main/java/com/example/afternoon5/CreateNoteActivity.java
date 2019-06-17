package com.example.afternoon5;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.afternoon5.HelperClasses.Note;
import com.pes.androidmaterialcolorpickerdialog.ColorPicker;
import com.pes.androidmaterialcolorpickerdialog.ColorPickerCallback;


public class CreateNoteActivity extends AppCompatActivity {

    int selectedColor = Color.WHITE;

    private Activity thisActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);

        this.thisActivity = this;
        selectedColor = getResources().getColor(R.color.colorNoteBackground,getTheme());

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, DataProvider.getInstance().getAllTags());
        MultiAutoCompleteTextView textView = findViewById(R.id.tagsTextView);
        textView.setAdapter(adapter);
        textView.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());



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

        Switch onOffSwitch = (Switch) findViewById(R.id.geoTagSwitch);
        onOffSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
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
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Switch onOffSwitch = (Switch) findViewById(R.id.geoTagSwitch);
                    onOffSwitch.setChecked(false);
                }
            }
        }
    }

    public void createNote(View view) {

        String title = ((EditText) findViewById(R.id.editTitle)).getText().toString();
        String text = ((EditText) findViewById(R.id.editText)).getText().toString();
        String tagString = ((MultiAutoCompleteTextView) findViewById(R.id.tagsTextView)).getText().toString();


        if (title.isEmpty() || text.isEmpty()) {
            Toast toast = Toast.makeText(this, "Title or Text can't be empty", Toast.LENGTH_LONG);
            toast.show();
            return;
        }

        tagString = tagString.replaceAll("(\\s+[#]?)|(\\s*[,]\\s*)|#", ",");


        Switch onOffSwitch = (Switch) findViewById(R.id.geoTagSwitch);
        String location = "";
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            if (onOffSwitch.isChecked()) {
                LocationManager locationManager = (LocationManager) getSystemService(getApplicationContext().LOCATION_SERVICE);
                Location locationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                location = locationGPS.getLatitude() + ":" + locationGPS.getLongitude();
            }
        }

        String[] tags = tagString.matches(".*\\S+.*") ? tagString.split(",") : new String[0];


        Note createNode = new Note(title, text, tags, false, location);
        createNode.setColor(selectedColor);
        DataProvider.getInstance().addNoteToNotes(createNode);


        DataProvider.getInstance().save(this);

        CharSequence save = "Note saved";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(this, save, duration);
        toast.show();

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.create_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_change_color) {
            final ColorPicker cp = new ColorPicker(CreateNoteActivity.this,
                    Color.alpha(selectedColor),
                    Color.red(selectedColor),
                    Color.green(selectedColor),
                    Color.blue(selectedColor));
            cp.enableAutoClose();
            cp.setCallback(new ColorPickerCallback() {
                @Override
                public void onColorChosen(int color) {
                    selectedColor = cp.getColor();
                    ConstraintLayout NoteElement = (ConstraintLayout) findViewById(R.id.note_color);
                    NoteElement.setBackgroundTintList(ColorStateList.valueOf(cp.getColor()));
                }
            });
            cp.show();
        }


        return super.onOptionsItemSelected(item);
    }
}
