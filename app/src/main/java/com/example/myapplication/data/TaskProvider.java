package com.example.myapplication.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.myapplication.R;

/**
 * Created by Catherine on 2/15/2018.
 */

public class TaskProvider extends ContentProvider {
    /** Database helper that will provide us access to the database */
    private TaskDbHelper mDbHelper;

    /** Tag for the log messages */
    public static final String LOG_TAG = TaskProvider.class.getSimpleName();

    /** URI matcher code for the content URI for the tasks table */
    private static final int TASKS = 100;

    /** URI matcher code for the content URI for a single task in the tasks table */
    private static final int TASK_ID = 101;

    /** URI matcher code for the content URI for the tasksDetails table */
    private static final int TASKDETAILS = 200;

    /** URI matcher code for the content URI for a single taskDetail in the taskDetails table */
    private static final int TASKDETAIL_ID = 201;

    /** URI matcher code for the content URI for the tasksDetails view */
    private static final int TASKDETAILVIEW = 300;

    /** URI matcher code for the content URI for a single taskDetail in the taskDetails view */
    private static final int TASKDETAILVIEW_ID = 301;
    /**
     * UriMatcher object to match a content URI to a corresponding code.
     * The input passed into the constructor represents the code to return for the root URI.
     * It's common to use NO_MATCH as the input for this case.
     */
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static
    {
        sUriMatcher.addURI(TaskContract.CONTENT_AUTHORITY, TaskContract.TaskEntry.TABLE_NAME,TASKS);
        sUriMatcher.addURI(TaskContract.CONTENT_AUTHORITY, TaskContract.TaskEntry.TABLE_NAME + "/#",TASK_ID);
        sUriMatcher.addURI(TaskContract.CONTENT_AUTHORITY, TaskContract.TaskDetailEntry.TABLE_NAME,TASKDETAILS);
        sUriMatcher.addURI(TaskContract.CONTENT_AUTHORITY, TaskContract.TaskDetailEntry.TABLE_NAME + "/#",TASKDETAIL_ID);
        sUriMatcher.addURI(TaskContract.CONTENT_AUTHORITY, TaskContract.TaskDetailViewEntry.VIEW_NAME,TASKDETAILVIEW);
        sUriMatcher.addURI(TaskContract.CONTENT_AUTHORITY, TaskContract.TaskDetailViewEntry.VIEW_NAME + "/#",TASKDETAILVIEW_ID);

    }

    @Override
    public boolean onCreate() {
        // To access our database, we instantiate our subclass of SQLiteOpenHelper
        // and pass the context, which is the current activity.
        mDbHelper = new TaskDbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        // Get readable database
        SQLiteDatabase database = mDbHelper.getReadableDatabase();

        // This cursor will hold the result of the query
        Cursor cursor;
        // Figure out if the URI matcher can match the URI to a specific code
        int match = sUriMatcher.match(uri);
        switch (match) {
            case TASKS:
                cursor = database.query(TaskContract.TaskEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case TASK_ID:
                selection = TaskContract.TaskEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };

                // This will perform a query on the task table where the _id equals 3 to return a
                // Cursor containing that row of the table.
                cursor = database.query(TaskContract.TaskEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case TASKDETAILS:
                cursor = database.query(TaskContract.TaskDetailEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case TASKDETAIL_ID:
                selection = TaskContract.TaskDetailEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };

                // This will perform a query on the taskDetail table where the _id equals 3 to return a
                // Cursor containing that row of the table.
                cursor = database.query(TaskContract.TaskDetailEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case TASKDETAILVIEW:
                cursor = database.query(TaskContract.TaskDetailViewEntry.VIEW_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case TASKDETAILVIEW_ID:
                selection = TaskContract.TaskDetailViewEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };

                // This will perform a query on the taskDetail view
                cursor = database.query(TaskContract.TaskDetailViewEntry.VIEW_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }

        // Set notification URI on the Cursor,
        // so we know what content URI the Cursor was created for.
        // If the data at this URI changes, then we know we need to update the Cursor.
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case TASKS:
                return TaskContract.TaskEntry.CONTENT_LIST_TYPE;
            case TASK_ID:
                return TaskContract.TaskEntry.CONTENT_ITEM_TYPE;
            case TASKDETAILS:
                return TaskContract.TaskDetailEntry.CONTENT_LIST_TYPE;
            case TASKDETAIL_ID:
                return TaskContract.TaskDetailEntry.CONTENT_ITEM_TYPE;
            case TASKDETAILVIEW:
                return TaskContract.TaskDetailViewEntry.CONTENT_LIST_TYPE;
            case TASKDETAILVIEW_ID:
                return TaskContract.TaskDetailViewEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case TASKS:
                return insertTable(uri, contentValues, TaskContract.TaskEntry.COLUMN_TASK_NAME, TaskContract.TaskEntry.TABLE_NAME);
            case TASKDETAILS:
                return insertTable(uri, contentValues, TaskContract.TaskDetailEntry.COLUMN_TASKDETAIL_NAME, TaskContract.TaskDetailEntry.TABLE_NAME);
            default:
                throw new IllegalArgumentException(getContext().getString(R.string.insert_error) + " " + uri);
        }
    }

    // Insert a task into the database with the given content values.
    private Uri insertTable(Uri uri, ContentValues values, String column, String table) {
        // Check that the name is not null
        String name = values.getAsString(column);
        if (name == null) {
            throw new IllegalArgumentException(getContext().getString(R.string.miss_insert_table));
        }

        // Get writeable database
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        // Insert the new shopping list with the given values
        long id = database.insert(table, null, values);
        // If the ID is -1, then the insertion failed. Log an error and return null.
        if (id == -1) {
            Log.e(LOG_TAG, getContext().getString(R.string.insert_row_error) + " " + uri);
            return null;
        }

        // Notify all listeners that the data has changed
        getContext().getContentResolver().notifyChange(uri, null);

        // Return the new URI with the ID (of the newly inserted row) appended at the end
        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Get writeable database
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        // Track the number of rows that were deleted
        int rowsDeleted;

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case TASKS:
                // Delete all rows that match the selection and selection args
                rowsDeleted = database.delete(TaskContract.TaskEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case TASK_ID:
                // Delete a single row given by the ID in the URI
                selection = TaskContract.TaskEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                rowsDeleted = database.delete(TaskContract.TaskEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case TASKDETAILS:
                // Delete all rows that match the selection and selection args
                rowsDeleted = database.delete(TaskContract.TaskDetailEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case TASKDETAIL_ID:
                // Delete a single row given by the ID in the URI
                selection = TaskContract.TaskEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                rowsDeleted = database.delete(TaskContract.TaskDetailEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException(getContext().getString(R.string.miss_delete_table) + uri);
        }
        // If 1 or more rows were deleted, then notify all listeners that the data at the
        // given URI has changed
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        // Return the number of rows deleted
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case TASKS:
                return updateTable(uri, contentValues, selection, selectionArgs);
            case TASK_ID:
                selection = TaskContract.TaskEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                return updateTable(uri, contentValues, selection, selectionArgs);
            case TASKDETAILS:
                return updateTable(uri, contentValues, selection, selectionArgs);
            case TASKDETAIL_ID:
                selection = TaskContract.TaskDetailEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                return updateTable(uri, contentValues, selection, selectionArgs);
            default:
                throw new IllegalArgumentException(getContext().getString(R.string.miss_update_table) + uri);
        }
    }

    /*
     * Update in the database with the given content values.
     */
    private int updateTable(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match){
            case TASK_ID:
                if (values.containsKey(TaskContract.TaskEntry.COLUMN_TASK_NAME)) {
                    String name = values.getAsString(TaskContract.TaskEntry.COLUMN_TASK_NAME);
                    if (name == null) {
                        throw new IllegalArgumentException(getContext().getString(R.string.miss_insert_table));
                    }
                }
                break;
            case TASKDETAIL_ID:
                if (values.containsKey(TaskContract.TaskDetailEntry.COLUMN_TASKDETAIL_NAME)) {
                    String name = values.getAsString(TaskContract.TaskDetailEntry.COLUMN_TASKDETAIL_NAME);
                    if (name == null) {
                        throw new IllegalArgumentException(getContext().getString(R.string.miss_insert_table));
                    }
                }
                break;
        }

        // If there are no values to update, then don't try to update the database
        if (values.size() == 0) {
            return 0;
        }

        // Otherwise, get writeable database to update the data
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        // Returns the number of database rows affected by the update statement

        // Perform the update on the database and get the number of rows affected
        int rowsUpdated = 0;
        switch (match) {
            case TASK_ID:
                rowsUpdated = database.update(TaskContract.TaskEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            case TASKDETAIL_ID:
                rowsUpdated = database.update(TaskContract.TaskDetailEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
        }

        // If 1 or more rows were updated, then notify all listeners that the data at the
        // given URI has changed
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        // Return the number of rows updated
        return rowsUpdated;
    }
}
