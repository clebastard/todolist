package com.example.myapplication;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.List;

import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters;
import io.github.luizgrp.sectionedrecyclerviewadapter.StatelessSection;

/**
 * Created by Catherine on 1/9/2018.
 */

public class HeaderRecyclerViewSection extends StatelessSection {
    private String title;
    private List<String> list;

    public HeaderRecyclerViewSection(String title, List<String> list) {
        super(new SectionParameters.Builder(R.layout.item_layout)
                .headerResourceId(R.layout.header_layout)
                .build());

        this.title = title;
        this.list = list;
    }

    @Override
    public int getContentItemsTotal() {
        return list.size();
    }

    @Override
    public RecyclerView.ViewHolder getItemViewHolder(View view) {
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
        final ItemViewHolder iHolder = (ItemViewHolder)holder;
        final String name = list.get(position);
        iHolder.itemContent.setText(name);

        iHolder.rootView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                ListFragment.showDeleteConfirmationDialog(ListFragment.sectionAdapter.getPositionInSection(iHolder.getAdapterPosition()));
                //Toast.makeText(view.getContext(), "Position " + ListFragment.sectionAdapter.getPositionInSection(iHolder.getAdapterPosition()), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public RecyclerView.ViewHolder getHeaderViewHolder(View view) {
        return new HeaderViewHolder(view);
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder) {
        HeaderViewHolder hHolder = (HeaderViewHolder)holder;
        hHolder.headerTitle.setText(title);
    }
}
