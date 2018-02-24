package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.myapplication.data.TaskContract;
import com.example.myapplication.model.Task;

import java.util.ArrayList;
import java.util.List;

import static com.example.myapplication.utils.Utility.setTintDrawable;

public class ListActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {

    // Content URI for the existing task (null if it's a new task)
    private Uri mCurrentUri;

    // EditTest fields
    private EditText mDescription;
    private Spinner mTaskSpinner;
    private Spinner mPrioritySpinner;

    // Priority default value
    private int mPriority = TaskContract.TaskDetailEntry.PRIORITY_NORMAL;

    // Flag that keeps track pf whether the taskdetail has been edited or not
    private boolean mTaskDetailHasChanged = false;

    // ArrayList of Task data
    private static List<Task> data = new ArrayList<>();

    // Listen for any user touches on a View
    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mTaskDetailHasChanged = true;
            return false;
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        // Determine if we're creating a new taskdetail or editing an existing one.
        Intent intent = getIntent();
        mCurrentUri = intent.getData();

        if (mCurrentUri == null) {
            // This is a new task, so change the app bar to say "Add a Task"
            setTitle(getString(R.string.editor_activity_title_new_taskdetail));

            invalidateOptionsMenu();
        } else {
            // Otherwise this is an existing task, so change app bar to say "Edit Task"
            setTitle(getString(R.string.editor_activity_title_edit_taskdetail));

            // Initialize a loader to read the task data from the database
            getLoaderManager().initLoader(0, null, this);
        }

        // Find all relevant views that we will need to read user input from
        mDescription = (EditText) findViewById(R.id.edit_taskdetail_description);
        mTaskSpinner = (Spinner) findViewById(R.id.spinner_task);
        mPrioritySpinner = (Spinner) findViewById(R.id.spinner_priority);

        mDescription.setOnTouchListener(mTouchListener);
        mTaskSpinner.setOnTouchListener(mTouchListener);
        mPrioritySpinner.setOnTouchListener(mTouchListener);

        SimpleCursorAdapter mAdapter;
        Cursor c = cursorSpinner(this, null);
        mAdapter = itemSpinner(this, c);
        mTaskSpinner.setAdapter(mAdapter);
        setupSpinner();

        if (c.moveToFirst()){
            do {
                // Get data from Cursor
                int id = c.getInt(c.getColumnIndex(TaskContract.TaskEntry._ID));
                String taskName = c.getString(c.getColumnIndex(TaskContract.TaskEntry.COLUMN_TASK_NAME));
                data.add(new Task(id, taskName));
            } while (c.moveToNext());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_editor, menu);

        // Change the default icon color to white
        MenuItem item = menu.findItem(R.id.action_save);
        setTintDrawable(item.getIcon(), Color.WHITE);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to a click on the "Save" menu option
            case R.id.action_save:
                saveTask();
                //Toast.makeText(getApplicationContext(), "Test" + mPrioritySpinner.getSelectedItemId() , Toast.LENGTH_SHORT).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Get user input from editor and save task into database.
    private void saveTask() {
        // Get the description
        String nameString = mDescription.getText().toString().trim();
        // Get the task
        int parentId = (int) data.get(mTaskSpinner.getSelectedItemPosition()).getId();

        // Check if this record is a new task
        if (mCurrentUri == null && mPriority == TaskContract.TaskDetailEntry.PRIORITY_NORMAL) {

            return;
        }

        ContentValues values = new ContentValues();
        values.put(TaskContract.TaskDetailEntry.COLUMN_TASKDETAIL_NAME, nameString);
        values.put(TaskContract.TaskDetailEntry.COLUMN_PARENT_ID, parentId);
        values.put(TaskContract.TaskDetailEntry.COLUMN_PRIORITY, mPriority);


        // Determine if this is a new or existing task by checking if mCurrentUri is null or not
        if (mCurrentUri == null) {
            // Insert a new row
            Uri newUri = getContentResolver().insert(TaskContract.TaskDetailEntry.CONTENT_URI, values);

            // Show a toast message depending on whether or not the insertion was successful.
            if (newUri == null) {
                Toast.makeText(this, getString(R.string.editor_insert_task_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.editor_insert_task_successful),
                        Toast.LENGTH_SHORT).show();
            }
        } else {
            // Update a new row
            int rowsAffected = getContentResolver().update(mCurrentUri, values, null, null);

            // Show a toast message depending on whether or not the update was successful.
            if (rowsAffected == 0) {
                Toast.makeText(this, getString(R.string.editor_update_task_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.editor_update_task_successful),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Setup the dropdown spinner that allows the user to select the priority.
    private void setupSpinner() {
        // Create adapter for spinner
        ArrayAdapter prioritySpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_priority_options, android.R.layout.simple_spinner_item);

        // Specify dropdown layout style - simple list view with 1 item per line
        prioritySpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        // Apply the adapter to the spinner
        mPrioritySpinner.setAdapter(prioritySpinnerAdapter);

        // Set the integer mSelected to the constant values
        mPrioritySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.priority_low))) {
                        mPriority = TaskContract.TaskDetailEntry.PRIORITY_LOW;
                    } else if (selection.equals(getString(R.string.priority_normal))) {
                        mPriority = TaskContract.TaskDetailEntry.PRIORITY_NORMAL;
                    } else {
                        mPriority = TaskContract.TaskDetailEntry.PRIORITY_HIGH;
                    }
                }
            }

            // Because AdapterView is an abstract class, onNothingSelected must be defined
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mPriority = TaskContract.TaskDetailEntry.PRIORITY_LOW;
            }
        });
    }

    // Set the cursor to load data for the spinner
    public static SimpleCursorAdapter itemSpinner(Context context, Cursor cursor) {
        SimpleCursorAdapter adapter;
        String[] fromColumns = {
                TaskContract.TaskEntry.COLUMN_TASK_NAME
        };
        // View IDs to map the columns (fetched above) into
        int[] toViews = {
                android.R.id.text1
        };

        adapter = new SimpleCursorAdapter(
                context, // context
                android.R.layout.simple_spinner_item, // layout file
                cursor, // DB cursor
                fromColumns, // data to bind to the UI
                toViews, // views that'll represent the data from `fromColumns`
                0
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return adapter;
    }

    // Generates the cursor to load data for the spinner
    public static Cursor cursorSpinner(Context context, String where) {
        // Columns from DB to map into the view file
        int pos = 0;
        String[] projection = {
                TaskContract.TaskEntry._ID,
                TaskContract.TaskEntry.COLUMN_TASK_NAME
        };
        String order = TaskContract.TaskEntry.COLUMN_TASK_NAME;

        Cursor cursor = context.getContentResolver().query(
                TaskContract.TaskEntry.CONTENT_URI,
                projection,
                where,
                null,
                order
        );
        return cursor;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        // Since the editor shows all task attributes
        String[] projection = {
                TaskContract.TaskDetailEntry._ID,
                TaskContract.TaskDetailEntry.COLUMN_TASKDETAIL_NAME,
                TaskContract.TaskDetailEntry.COLUMN_PARENT_ID,
                TaskContract.TaskDetailEntry.COLUMN_PRIORITY };

        // This loader will execute the ContentProvider's query method on a background thread
        return new CursorLoader(this,
                mCurrentUri,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor == null || cursor.getCount() < 1) {
            return;
        }

        if (cursor.moveToFirst()) {
            // Find the columns of task detail attributes
            int descriptionColumnIndex = cursor.getColumnIndex(TaskContract.TaskDetailEntry.COLUMN_TASKDETAIL_NAME);
            int parentColumnIndex = cursor.getColumnIndex(TaskContract.TaskDetailEntry.COLUMN_PARENT_ID);
            int priorityColumnIndex = cursor.getColumnIndex(TaskContract.TaskDetailEntry.COLUMN_PRIORITY);

            // Extract out the value from the Cursor for the given column index
            String description = cursor.getString(descriptionColumnIndex);
            int parent = cursor.getInt(parentColumnIndex);
            int priority = cursor.getInt(priorityColumnIndex);

            // Update the views on the screen with the values from the database
            mDescription.setText(description);
            // Find the position for the task Id
            int pos = 0;
            for (int i = 0; i < data.size(); i++) {
                if (data.get(i).getId() == parent) {
                    pos = i;
                    break;
                }
            }
            mTaskSpinner.setSelection(pos);

            // Priority is a dropdown spinner, so map the constant value from the database
            switch (priority) {
                case TaskContract.TaskDetailEntry.PRIORITY_NORMAL:
                    mPrioritySpinner.setSelection(1);
                    break;
                case TaskContract.TaskDetailEntry.PRIORITY_HIGH:
                    mPrioritySpinner.setSelection(2);
                    break;
                default:
                    mPrioritySpinner.setSelection(0);
                    break;
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // If the loader is invalidated, clear out all the data from the input fields.
        mDescription.setText("");
        mTaskSpinner.setSelection(0);
        mPrioritySpinner.setSelection(0);
    }
}
