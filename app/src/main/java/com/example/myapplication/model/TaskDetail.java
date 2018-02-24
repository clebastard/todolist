package com.example.myapplication.model;

/**
 * Created by Catherine on 2/10/2018.
 */

public class TaskDetail {
    private int id;
    private String description;
    private String parent;
    private int priority;

    // Empty constructor
    public TaskDetail(){}

    // Constructor for taskDetail table
    public TaskDetail(int id, String description, String parent, int priority){
        this.id = id;
        this.description = description;
        this.parent = parent;
        this.priority = priority;
    }

    // Get the id
    public long getId() { return id; }

    // Get the description
    public String getDescription() { return description; }

    // Get the parent
    public String getParent() { return parent; }

    // Get the priority
    public int getPriority() { return priority; }

}
