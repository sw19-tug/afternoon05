package com.example.afternoon5;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.afternoon5.HelperClasses.Note;
import com.pes.androidmaterialcolorpickerdialog.ColorPicker;
import com.pes.androidmaterialcolorpickerdialog.ColorPickerCallback;


public class CreateNoteActivity extends AppCompatActivity {

    int selectedColor = Color.WHITE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);

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

    }

    public void createNote(View view) {

        String title = ((EditText)findViewById(R.id.editTitle)).getText().toString();
        String text = ((EditText)findViewById(R.id.editText)).getText().toString();
        String tagString = ((MultiAutoCompleteTextView)findViewById(R.id.tagsTextView)).getText().toString();
        boolean pinned = false;

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
        Note createNode = new Note(title, text, tags, pinned);
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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_change_color){
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
                    ConstraintLayout NoteElement = (ConstraintLayout)findViewById(R.id.note_color);
                    NoteElement.setBackgroundTintList(ColorStateList.valueOf(cp.getColor()));
                }
            });
            cp.show();
        }


        return super.onOptionsItemSelected(item);
    }
}
