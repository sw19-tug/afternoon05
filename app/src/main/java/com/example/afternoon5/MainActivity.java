package com.example.afternoon5;


import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Environment;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;


import android.support.v7.widget.Toolbar;

import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;

import com.example.afternoon5.HelperClasses.Note;
import com.example.afternoon5.HelperClasses.list_adapter;


import org.w3c.dom.Node;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import java.util.Collections;
import java.util.Comparator;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;


public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
    private list_adapter adapter;


    Menu menu;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        this.menu = menu;
        MenuItem checkable_menue = menu.findItem(R.id.checkable_sort);
        int spinner_value = getSortingPreference();
        Menu submenue = checkable_menue.getSubMenu();
        MenuItem sort_option = submenue.findItem(spinner_value);
        if (sort_option != null)
            sort_option.setChecked(true);

        MenuItem checkable_nightmode = menu.findItem(R.id.action_nightmode_toggle);
        checkable_nightmode.setChecked(getNightModefromPrefs());


        MenuItem searchItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(this);

        return true;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DataProvider.getInstance().load(this);

        final ListView list = (ListView) findViewById(R.id.node_list);

        adapter = new list_adapter(this, DataProvider.getInstance().getNotes());
        list.setAdapter(adapter);
        list.setItemsCanFocus(false);
        list.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           int pos, long id) {

                for (int i = 0; i < list.getCount(); i++) {

                    View view1 = list.getChildAt(i);
                    view1.findViewById(R.id.export_checkbox).setVisibility(View.VISIBLE);

                }
                menu.findItem(R.id.action_share).setVisible(true);
                menu.findItem(R.id.action_export).setVisible(true);
                menu.findItem(R.id.action_cancel).setVisible(true);
                return true;
            }
        });


        list.setOnItemClickListener((parent, view, position, id) -> {

            Intent intent = new Intent(getBaseContext(), ViewEditNoteActivity.class);
            intent.putExtra("position", position);
            startActivity(intent);
        });

        sortList(getSortingPreference());
        setNightmode(getNightModefromPrefs());



    }


    public void closeExport(MenuItem menuItem) {
        final ListView list = (ListView) findViewById(R.id.node_list);
        for (int i = 0; i < list.getCount(); i++) {
            View view1 = list.getChildAt(i);
            view1.findViewById(R.id.export_checkbox).setVisibility(View.GONE);
        }
        menu.findItem(R.id.action_export).setVisible(false);
        menu.findItem(R.id.action_cancel).setVisible(false);
        menu.findItem(R.id.action_share).setVisible(false);

    }


    public void shareSelectedNotes(MenuItem menuItem) {

        final ListView list = (ListView) findViewById(R.id.node_list);
        ArrayList<Note> listtoShare = new ArrayList<>();
        for (int i = 0; i < list.getCount(); i++) {
            View view1 = list.getChildAt(i);
            if (((CheckBox) view1.findViewById(R.id.export_checkbox)).isChecked())
                listtoShare.add(DataProvider.getInstance().getNotes().get(i));
        }


        StringBuilder share_text = new StringBuilder("");

        for (Note note : listtoShare) {
            share_text.append("Titel: ");
            share_text.append(note.getTitle());
            share_text.append("\n");
            share_text.append(note.getText());
            share_text.append("\n");
            share_text.append(note.getTagsAsStringHashes());
            share_text.append("\n\n");
        }
        if (share_text.length() >= 3)
            share_text.delete(share_text.length() - 3, share_text.length());

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, share_text.toString());
        sendIntent.setType("text/plain");
        startActivity(sendIntent);

    }

    public void exportSelectedNotes(MenuItem menuItem) {
        final ListView list = (ListView) findViewById(R.id.node_list);
        ArrayList<Note> listtoExport = new ArrayList<>();
        for (int i = 0; i < list.getCount(); i++) {
            View view1 = list.getChildAt(i);
            if (((CheckBox) view1.findViewById(R.id.export_checkbox)).isChecked())
                listtoExport.add(DataProvider.getInstance().getNotes().get(i));
        }
        if (isExternalStorageWritable()) {
            DataProvider.getInstance().exportToExternalStorage(listtoExport);
        }

        Toast toast = Toast.makeText(this, R.string.toast_export, Toast.LENGTH_LONG);
        toast.show();
        closeExport(menuItem);


    }

    public void importNote(MenuItem menuItem) {

        isExternalStorageWritable();
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);

        intent.setType("*/*");

        startActivityForResult(intent, 42);

    }

    public void nightmodeSelected(MenuItem menuItem) {
        boolean isChecked = !menuItem.isChecked();
        menuItem.setChecked(isChecked);

        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        prefs.edit().putBoolean("nightmode_enabled", isChecked).apply();
        setNightmode(isChecked);
    }

    private void setNightmode(final boolean nightmode_enabled) {

        final int nightmode = nightmode_enabled ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO;
        if (AppCompatDelegate.getDefaultNightMode() != nightmode) {
            AppCompatDelegate.setDefaultNightMode(nightmode);
            recreate();
        }
    }

    private boolean getNightModefromPrefs() {
        final SharedPreferences prefs;
        prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        return prefs.getBoolean("nightmode_enabled", false);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent resultData) {

        if (requestCode == 42 && resultCode == Activity.RESULT_OK) {

            Uri uri = null;
            if (resultData != null) {
                uri = resultData.getData();
                DataProvider.getInstance().unzipFileAndSaveNotes(uri, this);
            }
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id != R.id.sort_alphabetical && id != R.id.sort_creation_date && id != R.id.sort_pinned) {
            return super.onOptionsItemSelected(item);
        }

        sortList(item.getItemId());
        item.setChecked(true);
        DataProvider.getInstance().save(getBaseContext());

        SharedPreferences prefs;
        prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        final SharedPreferences.Editor prefEditor = prefs.edit();
        prefEditor.putInt("spinner value start", item.getItemId());
        prefEditor.apply();
        return true;
    }


    public void openCreateNote(View view) {
        Intent intent = new Intent(this, CreateNoteActivity.class);
        startActivity(intent);

    }


    public void onCheckboxClicked(View view)
    {
        final ListView list = (ListView) findViewById(R.id.node_list);
        Note newNote = new Note();

        for (int i = 0; i < list.getCount(); i++) {
            View view1 = list.getChildAt(i);
            if (((CheckBox) view1.findViewById(R.id.checkBox2)).isChecked())
            {
                int number = i;
                String numberAsString = Integer.toString(number);
                Log.d("Meine_ID", numberAsString);
                ArrayList <Note> old_notes = DataProvider.getInstance().getNotes();
                old_notes.get(i).setPinn(true);
            }
            else
            {
                ArrayList <Note> old_notes = DataProvider.getInstance().getNotes();
                old_notes.get(i).setPinn(false);
            }
        }
        DataProvider.getInstance().save(this);
        sortList(getSortingPreference());
    }



    public void refreshList() {
        final ListView list = findViewById(R.id.node_list);
        final list_adapter adapter = new list_adapter(this, DataProvider.getInstance().getNotes());
        list.setAdapter(adapter);
    }

    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {

            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {

                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                } else {
                    Log.i("MAIN", "NO PERM");

                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            3);

                }
            }
            return true;
        }
        return false;
    }

    private void sortList(int item_id) {

        Comparator<Note> m_list_gradation;
        switch (item_id) {
            case R.id.sort_creation_date:
                m_list_gradation = new Comparator<Note>() {
                    @Override
                    public int compare(Note o1, Note o2) {
                        return o1.getCreationDate().compareTo(o2.getCreationDate());
                    }
                };
                break;
            case R.id.sort_alphabetical:
                m_list_gradation = new Comparator<Note>() {
                    @Override
                    public int compare(Note o1, Note o2) {
                        return o1.getTitle().compareTo(o2.getTitle());
                    }
                };
                break;
            case R.id.sort_pinned:
                m_list_gradation = new Comparator<Note>() {
                    @Override
                    public int compare(Note o1, Note o2) {
                        if (o1.getPinn() && o2.getPinn())
                        {
                            return 0;
                        }
                        if (o1.getPinn())
                        {
                            return -1;
                        }
                        else
                        {
                          return 1;
                        }

                    }
                };
                break;
            default:
                return;
        }


        Collections.sort(DataProvider.getInstance().getNotes(), m_list_gradation);
        adapter.notifyDataSetChanged();
        refreshList();
    }

    @Override
    protected void onResume() {
        super.onResume();
        sortList(getSortingPreference());
    }

    private int getSortingPreference() {
        final SharedPreferences prefs;
        prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        int value = prefs.getInt("spinner value start", R.id.sort_pinned);
        return value;
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return true;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        adapter.filter(s);
        ((ListView) findViewById(R.id.node_list)).invalidateViews();
        return true;
    }
}
