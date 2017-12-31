package com.example.myapplication;

/**
 * Created by Catherine on 12/30/2017.
 */

public class Item {
    private String task, activity;

    public Item() {
    }

    public Item(String task, String activity) {
        this.task = task;
        this.activity = activity;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String name) {
        this.task = name;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }
}

