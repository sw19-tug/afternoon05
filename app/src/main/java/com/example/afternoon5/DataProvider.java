package com.example.afternoon5;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import com.example.afternoon5.HelperClasses.Note;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

class DataProvider {
    private static final DataProvider ourInstance = new DataProvider();

    static DataProvider getInstance() {
        return ourInstance;
    }

    private ArrayList<Note>notes;



    private DataProvider() {

        notes = new ArrayList<>();


    }

    public void load(Context context)
    {
        SharedPreferences myprefs;
        myprefs = context.getSharedPreferences("com.example.afternoon5", context.MODE_PRIVATE);
        Gson gson = new Gson();
        String jsonText = myprefs.getString("Notes", null);
        ArrayList<Note> notes= (ArrayList<Note>) gson.fromJson(jsonText,
                new TypeToken<ArrayList<Note>>() {
                }.getType());
        if (notes != null)
        {
            this.notes = notes;
        }



    }

    public void save(Context context) {


        SharedPreferences myprefs;
        myprefs = context.getSharedPreferences("com.example.afternoon5", context.MODE_PRIVATE);
        SharedPreferences.Editor myeditor = myprefs.edit();
        Gson gson = new Gson();

        String json = gson.toJson(notes);
        myeditor.putString("Notes",json);


        myeditor.commit();

    }

    public ArrayList<Note> getNotes() {
        return notes;
    }

    public void setNotes(ArrayList<Note> notes) {
        this.notes = notes;
    }

    public void addNoteToNotes(Note note)
    {
        notes.add(note);
    }

    public ArrayList<String> getAllTags()
    {
        ArrayList<String> tags = new ArrayList<>();
        for (Note note : notes)
        {
            for(String s : note.getTags())
            {
                if(!tags.contains(s))
                {
                    tags.add(s);
                }
            }
        }
        return tags;
    }

    public void exportToExternalStorage(ArrayList<Note> notes)
    {
        File dir  = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/NoteExport");
        if (!dir.mkdirs()) {
            Log.e("DATA", "Directory not created");
        }
        ArrayList<File> files = new ArrayList<>();
        for(Note note : notes)
        {
            File file = noteToFile(note, dir);
            files.add(file);
        }
        File [] out = files.toArray(new File[0]);
        zipFilesToFolder(out, dir);

        files.forEach(x -> x.delete());


    }

    private void zipFilesToFolder(File [] files, File outZipPath) {
        try {
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd-HHmmss");
            FileOutputStream fos = new FileOutputStream(outZipPath.getAbsolutePath() + "/" +  sdf.format(cal.getTime())+".zip");
            ZipOutputStream zos = new ZipOutputStream(fos);
            
            for (int i = 0; i < files.length; i++) {

                byte[] buffer = new byte[1024];
                FileInputStream fis = new FileInputStream(files[i]);
                zos.putNextEntry(new ZipEntry(files[i].getName()));
                int length;
                while ((length = fis.read(buffer)) > 0) {
                    zos.write(buffer, 0, length);
                }
                zos.closeEntry();
                fis.close();
            }
            zos.close();
        } catch (IOException ioe) {
            Log.e("", ioe.getMessage());
            ioe.printStackTrace();
        }
    }
    private File noteToFile(Note note, File path)
    {
        File file = new File(path, Integer.toString(note.hashCode()));
        try {

        FileOutputStream stream = new FileOutputStream(file);
        try {
            Gson gson = new Gson();
            stream.write(gson.toJson(note).getBytes());
        } finally {
            stream.close();
        }}
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return file;

    }

    public void unzipFileAndSaveNotes(Uri uri, Context context)
    {
        InputStream is;
        ZipInputStream zis;
        try
        {

            is = context.getContentResolver().openInputStream(uri);
            zis = new ZipInputStream(new BufferedInputStream(is));
            ZipEntry ze;

            while((ze = zis.getNextEntry()) != null)
            {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int count;

                ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );

                while((count = zis.read(buffer)) != -1)
                {
                    baos.write(buffer, 0, count);
                    byte[] bytes = baos.toByteArray();
                    outputStream.write(bytes);
                    baos.reset();
                }
                String str =  new String(outputStream.toByteArray(), "UTF-8");
                Gson gson = new Gson();

                Note note = gson.fromJson(str,
                        new TypeToken<Note>() {
                        }.getType());
                notes.add(note);

                zis.closeEntry();
            }
            save(context);
            zis.close();
        }
        catch(IOException e)
        {
            e.printStackTrace();

        }
    }




}
