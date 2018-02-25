package com.example.myapplication;

import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.myapplication.data.TaskContract;
import com.example.myapplication.model.TaskDetail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;
import static com.example.myapplication.utils.Utility.setTintDrawable;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>,
        HeaderRecyclerViewSection.ListItemClickListener{
    public static SectionedRecyclerViewAdapter sectionAdapter;
    private RecyclerView sectionHeader;

    // Indicate this fragment has menu
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        final View layout = inflater.inflate(R.layout.fragment_list, container, false);

        getActivity().setTitle(R.string.List);
        sectionAdapter = new SectionedRecyclerViewAdapter();
        List<String> tasks = Arrays.asList((getResources().getStringArray(R.array.tasks)));
        sectionHeader = (RecyclerView) layout.findViewById(R.id.recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        sectionHeader.setLayoutManager(linearLayoutManager);
        sectionHeader.setHasFixedSize(true);

        // Kick off the loader
        resetLoader(0);

        return layout;
    }

    public void resetLoader(int status) {
        if (status == 0) getLoaderManager().initLoader(0, null, this);
        else getLoaderManager().restartLoader(0, null, this);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.main, menu);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        Boolean spinnerOn = sharedPreferences.getBoolean(getString(R.string.pref_show_dropdown_key), getResources().getBoolean(R.bool.pref_show_dropdown_default));

        MenuItem item = menu.findItem(R.id.spinner);

        final Spinner spinner = (Spinner) item.getActionView();
        item.setVisible(spinnerOn);

        MenuItem sync = menu.findItem(R.id.sync);
        MenuItem shareusers = menu.findItem(R.id.shareusers);
        setTintDrawable(sync.getIcon(), Color.WHITE);
        setTintDrawable(shareusers.getIcon(), Color.WHITE);

        if (spinnerOn) {
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                    R.array.spinner_list_item_array, R.layout.spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);

            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
                    int width = 0;
                    String text = adapterView.getItemAtPosition(pos).toString();
                    //if (text.length() > 60) width = 60;
                    ViewGroup.LayoutParams spinnerLayoutParams = spinner.getLayoutParams();
                    //spinnerLayoutParams.width -= 1;
                    spinnerLayoutParams.width = 80;
                    spinner.setLayoutParams(spinnerLayoutParams);
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                }
            });
        }

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String selection = null;
        String[] projection = {
                TaskContract.TaskDetailViewEntry._ID,
                TaskContract.TaskDetailViewEntry.COLUMN_TASKDETAIL_NAME,
                TaskContract.TaskDetailViewEntry.COLUMN_PARENT_NAME,
                TaskContract.TaskDetailViewEntry.COLUMN_PRIORITY
        };
        // this loader will execute the ContentProvider's query method on a background thread
        return new CursorLoader(getActivity(),
                TaskContract.TaskDetailViewEntry.CONTENT_URI,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor != null && cursor.getCount() > 0) {
        sectionAdapter = new SectionedRecyclerViewAdapter();

        List<TaskDetail> data = new ArrayList<TaskDetail>();
        //int i = 1;
        String group = null;

        // move cursor to first row
        if (cursor.moveToFirst()) {
            do {
                // Get data from Cursor
                int id = cursor.getInt(cursor.getColumnIndex(TaskContract.TaskDetailEntry._ID));
                String taskName = cursor.getString(cursor.getColumnIndex(TaskContract.TaskDetailEntry.COLUMN_TASKDETAIL_NAME));
                String parentTask = cursor.getString(cursor.getColumnIndex("parent"));
                int priority = cursor.getInt(cursor.getColumnIndex(TaskContract.TaskDetailEntry.COLUMN_PRIORITY));
                if (group == null) {
                    group = parentTask;
                    data.add(new TaskDetail(id, taskName, parentTask, priority));
                } else if (!group.equals(parentTask)) {
                    HeaderRecyclerViewSection newSection = new HeaderRecyclerViewSection(getContext(), group, data, this);
                    sectionAdapter.addSection(newSection);
                    group = parentTask;
                    data = new ArrayList<TaskDetail>();
                    data.add(new TaskDetail(id, taskName, parentTask, priority));
                } else {
                    data.add(new TaskDetail(id, taskName, parentTask, priority));
                }
                // move to next row
            } while (cursor.moveToNext());
        }
        HeaderRecyclerViewSection newSection = new HeaderRecyclerViewSection(getContext(), group, data, this);
        sectionAdapter.addSection(newSection);


        sectionHeader.setAdapter(sectionAdapter);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void onListItemClickListener(long id) {
        showDeleteConfirmationDialog(getContext(), id);
    }

    public void showDeleteConfirmationDialog(final Context context, final long idx) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(context.getString(R.string.askDelete));
        builder.setPositiveButton(R.string.deleteTitle, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Uri currentUri = ContentUris.withAppendedId(TaskContract.TaskDetailEntry.CONTENT_URI, idx);
                // Delete row
                int rowsDeleted = context.getContentResolver().delete(currentUri, null, null);

                // Show a toast message depending on whether or not the delete was successful.
                if (rowsDeleted == 0) {
                    Toast.makeText(context, context.getString(R.string.editor_delete_task_failed),
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, context.getString(R.string.editor_delete_task_successful),
                            Toast.LENGTH_SHORT).show();
                }

                // Kick off the loader
                resetLoader(1);
            }
        });
        builder.setNegativeButton(R.string.cancelTitle, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();
    }
}
