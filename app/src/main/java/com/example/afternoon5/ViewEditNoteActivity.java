package com.example.afternoon5;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;


import com.example.afternoon5.HelperClasses.Note;

import java.util.ArrayList;

public class ViewEditNoteActivity extends AppCompatActivity {
    private static final String EXTRA_MESSAGE ="extra";
    private static Note objectToEdit;
    private int position;

/*    static void callIntentwithExtra(Context cx, Note object)
    {
        Intent intent = new Intent(cx, ViewEditNoteActivity.class);
        //intent.putExtra(EXTRA_MESSAGE, object);
        objectToEdit = object;
        cx.startActivity(intent);
    }*/

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

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, DataProvider.getInstance().getAllTags());

        tags.setAdapter(adapter);
        tags.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
        tags.setText(objectToEdit.getTagsAsString());

        Button btn = this.findViewById(R.id.button_safe);
        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                safeAndCallMainActivity();
            }
        });

        //Go Back on Android Logo Label Click
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

    }

    @Override
    public void onBackPressed() {
        safeAndCallMainActivity();
    }

    private void safeAndCallMainActivity()
    {
        final TextView editNote = (TextView) this.findViewById(R.id.editNote);
        final TextView Title = (TextView) this.findViewById(R.id.Title);
        final MultiAutoCompleteTextView tags = (MultiAutoCompleteTextView) this.findViewById(R.id.editTagsTextView);
        DataProvider.getInstance().getNotes().get(position).setText(editNote.getText().toString());
        DataProvider.getInstance().getNotes().get(position).setTitle(Title.getText().toString());
        String tagString = tags.getText().toString();
        tagString = tagString.replaceAll(" ", "");
        if(tagString.endsWith(","))
        {
            tagString = tagString.substring(0, tagString.length()-1);
        }
        String[] tagsArray =tagString.split(",");


        DataProvider.getInstance().getNotes().get(position).setTags(tagsArray);
        DataProvider.getInstance().save(this);

        Intent intent = new Intent(getBaseContext(), MainActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_delete) {

            DialogFragment deleteNoteDialogFragment = new DeleteNoteDialogFragment();
            deleteNoteDialogFragment.show(getSupportFragmentManager(), "DeleteNoteDialogFragment");

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    static public class DeleteNoteDialogFragment extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the Builder class for convenient dialog construction
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("Do you want to delete this Note?")
                    .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int id) {
                            // FIRE ZE MISSILES!
                            Dialog dialog  = (Dialog) dialogInterface;
                            Context context = dialog.getContext();
                            DataProvider.getInstance().getNotes().remove(objectToEdit);
                            DataProvider.getInstance().save(context);
                            Intent intent = new Intent(context, MainActivity.class);
                            startActivity(intent);

                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User cancelled the dialog
                        }
                    });
            // Create the AlertDialog object and return it
            return builder.create();
        }
    }

}

