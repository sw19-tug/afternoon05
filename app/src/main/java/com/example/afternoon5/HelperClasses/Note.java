package com.example.afternoon5.HelperClasses;

import android.view.View;
import android.widget.CheckBox;

import java.util.ArrayList;
import java.util.Arrays;

public class Note {
    private String title;
    private String text;
    private Boolean pinned;

    private ArrayList<String> tags;

    public Note(String title, String text) {
        this.title = title;
        this.text = text;
        this.tags = new ArrayList<>();
        this.pinned = false;
    }
    public Note(String title, String text, String[] tags) {
        this.title = title;
        this.text = text;
        this.tags = removeDuplicates(new ArrayList<>(Arrays.asList(tags)));
    }

    public static <T> ArrayList<T> removeDuplicates(ArrayList<T> list)
    {

        ArrayList<T> newList = new ArrayList<T>();
        for (T element : list) {

            if (!newList.contains(element)) {
                newList.add(element);
            }
        }

        return newList;

    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public ArrayList<String> getTags() {
        return tags;
    }

    public String getTagsAsString()
    {
        String tagsAsString = "";
        for (String s : tags)
        {
            tagsAsString = tagsAsString + s + ", ";
        }
        return tagsAsString;
    }




    public void setTags(String[] tags) {
        this.tags = removeDuplicates(new ArrayList<>(Arrays.asList(tags)));
    }

    public void onCheckboxClicked(View view)
    {
        boolean checked = ((CheckBox) view).isChecked();
        if (checked)
            this.pinned = true;
    }
}
