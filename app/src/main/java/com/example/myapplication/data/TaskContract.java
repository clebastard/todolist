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

    // Task table structure
    public static final class TaskEntry implements BaseColumns {
        // Table name
        public static final String TABLE_NAME = "task";

        // Table structure
        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_TASK_NAME = "description";
    }

    // Detail Task table structure
    public static final class TaskDetailEntry implements BaseColumns {
        // Table name
        public static final String TABLE_NAME = "taskDetail";

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
}