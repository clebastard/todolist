package com.example.myapplication;

/**
 * Created by Catherine on 12/30/2017.
 */

public class Item {
    private String title, genre;

    public Item() {
    }

    public Item(String title, String genre) {
        this.title = title;
        this.genre = genre;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String name) {
        this.title = name;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }
}

