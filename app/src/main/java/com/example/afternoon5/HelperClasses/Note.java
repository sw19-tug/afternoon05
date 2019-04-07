package com.example.afternoon5.HelperClasses;

import java.util.ArrayList;
import java.util.Arrays;

public class Note {
    private String title;
    private String text;

    private ArrayList<String> tags;

    public Note(String title, String text) {
        this.title = title;
        this.text = text;
        this.tags = new ArrayList<>();
    }
    public Note(String title, String text, String[] tags) {
        this.title = title;
        this.text = text;
        this.tags = new ArrayList<>(Arrays.asList(tags));
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



    public void setTags(ArrayList<String> tags) {
        this.tags = tags;
    }


}
