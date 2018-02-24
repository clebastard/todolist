package com.example.myapplication.model;

/**
 * Created by Catherine on 2/19/2018.
 */

public class Task {
    private int id;
    private String description;

    // Empty constructor
    public Task(){}

    // Constructor for taskDetail table
    public Task(int id, String description){
        this.id = id;
        this.description = description;
    }

    // Get the id
    public long getId() { return id; }

    // Get the description
    public String getDescription() { return description; }
}
