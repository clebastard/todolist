package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;

import static com.example.myapplication.utils.Utility.setTintDrawable;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListFragment extends Fragment {
    public static SectionedRecyclerViewAdapter sectionAdapter;
    private RecyclerView sectionHeader;

    /*
    * Indicate this fragment has menu
    */
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
        sectionAdapter = new SectionedRecyclerViewAdapter();
        for (int i = 0; i < getResources().getStringArray(R.array.tasks).length; i++)
        {
            HeaderRecyclerViewSection newSection = new HeaderRecyclerViewSection(getContext(), tasks.get(i), getDataTasks(i+1));
            sectionAdapter.addSection(newSection);
        }
        sectionHeader.setAdapter(sectionAdapter);

        return layout;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.main, menu);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        Boolean spinnerOn = sharedPreferences.getBoolean(getString(R.string.pref_show_dropdown_key), getResources().getBoolean(R.bool.pref_show_dropdown_default));
        //Toast.makeText(getContext(), "Spiner is " + spinnerOn, Toast.LENGTH_SHORT).show();

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

    private List<String> getDataTasks(int i){

        List<String> data = new ArrayList<>();
        int action = getResources().getIdentifier("actionTask" + i, "array", getActivity().getPackageName());
        for (String actionTask : getResources().getStringArray(action)) {
            data.add(actionTask);
        }
        return data;
    }
}
