package com.example.nicke.loginapplication.models;

/**
 * Created by Nicke on 7/26/2017.
 */

public class Notes {
    private int id;
    private String title;
    private String date;
    private String content;
    private String notes_user;

    public Notes(String title, String date, String content) {
        this.title = title;
        this.date = date;
        this.content = content;
    }

    public Notes() {

    }

    public Notes(int id, String title, String date, String content, String notes_user) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.content = content;
        this.notes_user = notes_user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getNotes_user() {
        return notes_user;
    }

    public void setNotes_user(String notes_user) {
        this.notes_user = notes_user;
    }
}
