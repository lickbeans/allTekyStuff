package com.example.aaroncampbell.elevennote;

import java.util.Date;

/**
 * Created by aaroncampbell on 10/18/16.
 */

public class Note implements Comparable<Note>{
    private String title;
    private String text;
    private Date date;

    public Note(String title, String text, Date date) {
        this.title = title;
        this.text = text;
        this.date = date;
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }


    @Override
    public int compareTo(Note another) {
        return another.getDate().compareTo(getDate());
    }
}
