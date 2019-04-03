package com.example.afternoon5;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.afternoon5.HelperClasses.Note;

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
        DataProvider.getInstance().getNotes().get(position).setText(editNote.getText().toString());
        DataProvider.getInstance().getNotes().get(position).setTitle(Title.getText().toString());
        DataProvider.getInstance().save(this);

        Intent intent = new Intent(getBaseContext(), MainActivity.class);
        startActivity(intent);
    }
}
