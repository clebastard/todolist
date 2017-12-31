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
        Item item = new Item("Strawberry", "Fruits");
        itemList.add(item);

        item = new Item("Grapes", "Fruits");
        itemList.add(item);

        item = new Item("Tea", "Beverage");
        itemList.add(item);

        item = new Item("Coffee", "Beverage");
        itemList.add(item);

        item = new Item("Oreo", "Cookies");
        itemList.add(item);

        item = new Item("Chocolate Bear Paws", "Cookies");
        itemList.add(item);

        item = new Item("Bread", "Baking goods");
        itemList.add(item);

        item = new Item("Toothpaste", "Bathroom");
        itemList.add(item);

        mAdapter.notifyDataSetChanged();
    }
}
