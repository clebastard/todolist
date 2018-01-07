package com.example.myapplication;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListFragment extends Fragment {
    private java.util.List itemList = new ArrayList<>();
    private RecyclerView recyclerView;
    private ItemsAdapter mAdapter;

    public ListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        final View layout = inflater.inflate(R.layout.fragment_list, container, false);
        recyclerView = (RecyclerView) layout.findViewById(R.id.recycler_view);

        mAdapter = new ItemsAdapter(itemList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(mAdapter);

        prepareData();
        return layout;
    }

    private void prepareData() {

        List<String> myTasks = Arrays.asList((getResources().getStringArray(R.array.tasks)));
        List<String> ActionTasks1 = Arrays.asList((getResources().getStringArray(R.array.actionTask1)));
        List<String> ActionTasks2 = Arrays.asList((getResources().getStringArray(R.array.actionTask2)));
        List<String> ActionTasks3 = Arrays.asList((getResources().getStringArray(R.array.actionTask3)));

        Item item = new Item(ActionTasks1.get(ActionTasks1.indexOf(getString(R.string.actionTask11))), myTasks.get(myTasks.indexOf(getString(R.string.task1))));
        itemList.add(item);

        item = new Item(ActionTasks2.get(ActionTasks2.indexOf(getString(R.string.actionTask21))), myTasks.get(myTasks.indexOf(getString(R.string.task2))));
        itemList.add(item);

        item = new Item(ActionTasks1.get(ActionTasks1.indexOf(getString(R.string.actionTask12))), myTasks.get(myTasks.indexOf(getString(R.string.task1))));
        itemList.add(item);

        item = new Item(ActionTasks2.get(ActionTasks2.indexOf(getString(R.string.actionTask21))), myTasks.get(myTasks.indexOf(getString(R.string.task2))));
        itemList.add(item);

        item = new Item(ActionTasks3.get(ActionTasks3.indexOf(getString(R.string.actionTask31))), myTasks.get(myTasks.indexOf(getString(R.string.task3))));
        itemList.add(item);

        item = new Item(ActionTasks3.get(ActionTasks3.indexOf(getString(R.string.actionTask32))), myTasks.get(myTasks.indexOf(getString(R.string.task3))));
        itemList.add(item);

        item = new Item(ActionTasks1.get(ActionTasks1.indexOf(getString(R.string.actionTask13))), myTasks.get(myTasks.indexOf(getString(R.string.task1))));
        itemList.add(item);

        item = new Item(ActionTasks2.get(ActionTasks2.indexOf(getString(R.string.actionTask23))), myTasks.get(myTasks.indexOf(getString(R.string.task2))));
        itemList.add(item);

        mAdapter.notifyDataSetChanged();
    }
}
