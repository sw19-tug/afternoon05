package com.example.afternoon5;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;


import com.example.afternoon5.HelperClasses.Note;
import com.pes.androidmaterialcolorpickerdialog.ColorPicker;
import com.pes.androidmaterialcolorpickerdialog.ColorPickerCallback;

import java.util.ArrayList;

public class ViewEditNoteActivity extends AppCompatActivity {
    private static final String EXTRA_MESSAGE = "extra";
    private static Note objectToEdit;
    private int position;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.edit_menu, menu);
        return true;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_edit_note);

        position = getIntent().getIntExtra("position", 0);
        objectToEdit = DataProvider.getInstance().getNotes().get(position);


        final TextView editNote = (TextView) this.findViewById(R.id.editNote);
        editNote.setText(objectToEdit.getText());

        final TextView Title = (TextView) this.findViewById(R.id.Title);
        Title.setText(objectToEdit.getTitle());

        final MultiAutoCompleteTextView tags = (MultiAutoCompleteTextView) this.findViewById(R.id.editTagsTextView);

        final TextView location = (TextView) this.findViewById(R.id.locationTextView);

        if(objectToEdit.getLocation() != null)
        {
            if (!objectToEdit.getLocation().isEmpty()) {
                location.setVisibility(View.VISIBLE);
                String[] latlong = objectToEdit.getLocation().split(":");
                location.setText("Latitute: " + latlong[0] + "\nLongitude: " + latlong[1]);
            } else {
                location.setVisibility(View.GONE);
            }
        }
        else
        {
            location.setVisibility(View.GONE);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, DataProvider.getInstance().getAllTags());

        tags.setAdapter(adapter);
        tags.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());


        String tagsAsString = objectToEdit.getTagsAsString();

        if(tagsAsString != null && !tagsAsString.isEmpty())
        {
            tagsAsString = tagsAsString.replaceAll("#", " ");

            if (tagsAsString.substring(0, 1).equals(" "))
            {
                tagsAsString = tagsAsString.substring(1);
            }
        }

        tags.setText(tagsAsString);

        Button btn = this.findViewById(R.id.button_safe);
        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                safeAndCallMainActivity();
            }
        });


        ActionBar act_bar = getSupportActionBar();
        if (act_bar != null) {
            act_bar.setDisplayShowTitleEnabled(false);
            act_bar.setDisplayShowCustomEnabled(true);

            View toolbarView = getLayoutInflater().inflate(R.layout.toolbar_custom, null);
            TextView title_bar = toolbarView.findViewById(R.id.toolbar_title);

            Intent mainIntent = new Intent(ViewEditNoteActivity.this, MainActivity.class);
            mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            title_bar.setOnClickListener(v -> this.startActivity(mainIntent));
            act_bar.setCustomView(toolbarView);
        }
        ConstraintLayout NoteElement = (ConstraintLayout)findViewById(R.id.note_color);
        NoteElement.setBackgroundTintList(ColorStateList.valueOf(objectToEdit.getColor()));

    }

    @Override
    public void onBackPressed() {
        safeAndCallMainActivity();
    }

    private void safeAndCallMainActivity() {
        final TextView editNote = (TextView) this.findViewById(R.id.editNote);
        final TextView Title = (TextView) this.findViewById(R.id.Title);
        final MultiAutoCompleteTextView tags = (MultiAutoCompleteTextView) this.findViewById(R.id.editTagsTextView);
        DataProvider.getInstance().getNotes().get(position).setText(editNote.getText().toString());
        DataProvider.getInstance().getNotes().get(position).setTitle(Title.getText().toString());
        String tagString = tags.getText().toString();
        tagString = tagString.replaceAll("#", " ");
        String[] tagsArray = tagString.split(" ");


        DataProvider.getInstance().getNotes().get(position).setTags(tagsArray);
        DataProvider.getInstance().save(this);

        Intent intent = new Intent(getBaseContext(), MainActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();


        if (id == R.id.action_delete) {

            DialogFragment deleteNoteDialogFragment = new DeleteNoteDialogFragment();
            deleteNoteDialogFragment.show(getSupportFragmentManager(), "DeleteNoteDialogFragment");

            return true;
        } else if (id == R.id.action_change_color){
            int current_color = objectToEdit.getColor();
            final ColorPicker cp = new ColorPicker(ViewEditNoteActivity.this,
                    Color.alpha(current_color),
                    Color.red(current_color),
                    Color.green(current_color),
                    Color.blue(current_color));

            cp.enableAutoClose();
            cp.setCallback(new ColorPickerCallback() {
                @Override
                public void onColorChosen(int color) {
                    int selected_color = cp.getColor();
                    objectToEdit.setColor(cp.getColor());
                    ConstraintLayout NoteElement = (ConstraintLayout)findViewById(R.id.note_color);
                    NoteElement.setBackgroundTintList(ColorStateList.valueOf(cp.getColor()));
                }
            });
            cp.show();
        }


        return super.onOptionsItemSelected(item);
    }

    static public class DeleteNoteDialogFragment extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("Do you want to delete this Note?")
                    .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int id) {

                            Dialog dialog = (Dialog) dialogInterface;
                            Context context = dialog.getContext();
                            DataProvider.getInstance().getNotes().remove(objectToEdit);
                            DataProvider.getInstance().save(context);
                            Intent intent = new Intent(context, MainActivity.class);
                            startActivity(intent);

                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                        }
                    });

            return builder.create();
        }
    }

}

