package com.example.myapplication.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Catherine on 2/4/2018.
 */

public class TaskDbHelper extends SQLiteOpenHelper {
    // Name of the database file
    private static final String DATABASE_NAME = "tasklist.db";

    // Database version. If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 1;

    private Context context;

    // Constructs a new instance
    public TaskDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context.getApplicationContext();
    }

    // This is called when the database is created for the first time.
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create a String that contains the SQL statement to create the task table
        String SQL_CREATE_TASK_TABLE =  "CREATE TABLE " + TaskContract.TaskEntry.TABLE_NAME + " ("
                + TaskContract.TaskEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + TaskContract.TaskEntry.COLUMN_TASK_NAME + " TEXT NOT NULL);";

        // Create a String that contains the SQL statement to create the task detail table
        String SQL_CREATE_TASKDETAIL_TABLE =  "CREATE TABLE " + TaskContract.TaskDetailEntry.TABLE_NAME + " ("
                + TaskContract.TaskDetailEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + TaskContract.TaskDetailEntry.COLUMN_TASKDETAIL_NAME + " TEXT NOT NULL, "
                + TaskContract.TaskDetailEntry.COLUMN_PARENT_ID + " INT, "
                + TaskContract.TaskDetailEntry.COLUMN_PRIORITY + " INT);";

        // Execute the SQL statement
        db.execSQL(SQL_CREATE_TASK_TABLE);
        db.execSQL(SQL_CREATE_TASKDETAIL_TABLE);

        insertTaskData(db);
        insertTaskDetailData(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TaskContract.TaskEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TaskContract.TaskDetailEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    private void insertTaskData(SQLiteDatabase db){
        if(db == null){
            return;
        }
        List<String> tasks = Arrays.asList((context.getResources().getStringArray(R.array.tasks)));
        //create a list of tasks
        List<ContentValues> list = new ArrayList<ContentValues>();

        ContentValues cv;

        for (int i = 0; i < context.getResources().getStringArray(R.array.tasks).length; i++)
        {
            cv = new ContentValues();
            cv.put(TaskContract.TaskEntry.COLUMN_TASK_NAME, tasks.get(i));
            list.add(cv);
        }

        //insert all tasks in one transaction
        try
        {
            db.beginTransaction();
            //go through the list and add one by one
            for(ContentValues c:list){
                db.insert(TaskContract.TaskEntry.TABLE_NAME, null, c);
            }
            db.setTransactionSuccessful();
        }
        catch (SQLException e) {
            //too bad :(
        }
        finally
        {
            db.endTransaction();
        }
    }

    private void insertTaskDetailData(SQLiteDatabase db){
        if(db == null){
            return;
        }
        //create a list of fake task detail
        List<ContentValues> list = new ArrayList<ContentValues>();

        ContentValues cv;

        for (int i = 0; i < context.getResources().getStringArray(R.array.tasks).length; i++)
        {
            int action = context.getResources().getIdentifier("actionTask" + (i+1), "array", context.getPackageName());
            for (String actionTask : context.getResources().getStringArray(action)) {
                cv = new ContentValues();
                cv.put(TaskContract.TaskDetailEntry.COLUMN_TASKDETAIL_NAME, actionTask);
                cv.put(TaskContract.TaskDetailEntry.COLUMN_PARENT_ID, i + 1);
                cv.put(TaskContract.TaskDetailEntry.COLUMN_PRIORITY, TaskContract.TaskDetailEntry.PRIORITY_NORMAL);
                list.add(cv);
            }
        }

        //insert all tasks in one transaction
        try
        {
            db.beginTransaction();
            //go through the list and add one by one
            for(ContentValues c:list){
                db.insert(TaskContract.TaskDetailEntry.TABLE_NAME, null, c);
            }
            db.setTransactionSuccessful();
        }
        catch (SQLException e) {
            //too bad :(
        }
        finally
        {
            db.endTransaction();
        }
    }
}
