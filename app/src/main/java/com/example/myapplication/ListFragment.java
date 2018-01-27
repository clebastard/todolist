package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListFragment extends Fragment {
    public static SectionedRecyclerViewAdapter sectionAdapter;
    private RecyclerView sectionHeader;

    public ListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        final View layout = inflater.inflate(R.layout.fragment_list, container, false);

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

    private List<String> getDataTasks(int i){
        List<String> data = new ArrayList<>();
        int action = getResources().getIdentifier("actionTask" + i, "array", getActivity().getPackageName());
        for (String actionTask : getResources().getStringArray(action)) {
            data.add(actionTask);
        }
        return data;
    }
}
