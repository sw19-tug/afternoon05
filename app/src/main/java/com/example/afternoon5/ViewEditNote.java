package com.example.afternoon5;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ViewEditNote extends AppCompatActivity {
    private static final String EXTRA_MESSAGE ="extra";
    private static note_obj objectToEdit;

    static void callIntentwithExtra(Context cx, note_obj object)
    {
        Intent intent = new Intent(cx, ViewEditNote.class);
        //intent.putExtra(EXTRA_MESSAGE, object);
        objectToEdit = object;
        cx.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_edit_note);

        final TextView editNote = (TextView) this.findViewById(R.id.editNote);
        editNote.setText(objectToEdit.text);

        final TextView Title = (TextView) this.findViewById(R.id.Title);
        Title.setText(objectToEdit.title);

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
        objectToEdit.text = editNote.getText().toString();
        objectToEdit.title = Title.getText().toString();
        MainActivity.callIntentwithObjectToPushIntoList(ViewEditNote.this,objectToEdit);
    }
}
