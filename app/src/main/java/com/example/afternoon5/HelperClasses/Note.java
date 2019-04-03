package com.example.afternoon5.HelperClasses;

import java.time.LocalDateTime;
import java.util.Date;

public class Note {
    private String title;
    private String text;
    private Date creation_date;

    public Note(String title, String text, Date created) {
        this.title = title;
        this.text = text;
        this.creation_date = created;
    }

    public Date getCreationDate() {
        return creation_date;
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
}
