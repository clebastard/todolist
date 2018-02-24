package com.example.myapplication.data;

import android.content.ContentResolver;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Catherine on 2/4/2018.
 */

public class TaskContract {
    // Empty constructor.
    private TaskContract() {}

    //  Name for the entire content provider
    public static final String CONTENT_AUTHORITY = "com.example.myapplication";

    // Create the base of all URI's which apps will use to contact the content provider.
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    // Task table structure
    public static final class TaskEntry implements BaseColumns {
        // Table name
        public static final String TABLE_NAME = "task";

        // The content URI to access the task data in the provider
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, TABLE_NAME);

        // The MIME type of the {@link #CONTENT_URI} for a list of tasks
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + TABLE_NAME;

        // The MIME type of the {@link #CONTENT_URI} for a single task.
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + TABLE_NAME;

        // Table structure
        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_TASK_NAME = "description";
    }

    // Detail Task table structure
    public static final class TaskDetailEntry implements BaseColumns {
        // Table name
        public static final String TABLE_NAME = "taskDetail";

        // The content URI to access the task data in the provider
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, TABLE_NAME);

        // The MIME type of the {@link #CONTENT_URI} for a list of tasks
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + TABLE_NAME;

        // The MIME type of the {@link #CONTENT_URI} for a single task.
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + TABLE_NAME;

        // Table structure
        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_TASKDETAIL_NAME = "description";
        public static final String COLUMN_PARENT_ID = "parent";
        public static final String COLUMN_PRIORITY = "priority";

        /**
         * Possible values for the priority
         */
        public static final int PRIORITY_LOW = 0;
        public static final int PRIORITY_NORMAL = 1;
        public static final int PRIORITY_HIGH = 2;
    }

    // Detail Task view structure
    public static final class TaskDetailViewEntry implements BaseColumns {
        // View name
        public static final String VIEW_NAME = "taskDetailView";

        // The content URI to access the taskdetail view data in the provider
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, VIEW_NAME);

        // The MIME type of the {@link #CONTENT_URI} for a list of tasksdetail view
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + VIEW_NAME;

        // The MIME type of the {@link #CONTENT_URI} for a single taskdetail view.
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + VIEW_NAME;

        // Table structure
        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_TASKDETAIL_NAME = "description";
        public static final String COLUMN_PARENT_NAME = "parent";
        public static final String COLUMN_PRIORITY = "priority";
    }
}