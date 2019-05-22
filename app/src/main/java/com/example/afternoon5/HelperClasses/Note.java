package com.example.afternoon5.HelperClasses;

import java.util.ArrayList;
import java.util.Arrays;

import java.time.LocalDateTime;
import java.util.Date;

public class Note {
    private String title;
    private String text;
    private Date creation_date;
    private ArrayList<String> tags;

    public Note(String title, String text, Date created) {
        this.title = title;
        this.text = text;
        this.creation_date = created;
        this.tags = new ArrayList<>();
    }

    public Date getCreationDate() {
        return creation_date;
    }

    public Note(String title, String text, String[] tags) {
        this.title = title;
        this.text = text;
        this.tags = removeDuplicates(new ArrayList<>(Arrays.asList(tags)));
        this.creation_date = new Date();
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

    public String getTagsAsStringHashes()
    {
        String tagsAsString = "";
        for (String s : tags)
        {
            if (!s.isEmpty() && !s.contentEquals(" "))
            {
                tagsAsString = tagsAsString + "#" + s + " ";
            }
        }
        return tagsAsString;
    }






    public void setTags(String[] tags) {
        this.tags = removeDuplicates(new ArrayList<>(Arrays.asList(tags)));
    }


}
