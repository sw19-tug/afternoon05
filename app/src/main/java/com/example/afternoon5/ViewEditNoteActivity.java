package com.example.afternoon5;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
}
