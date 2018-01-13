package com.example.myapplication;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import java.util.List;

import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters;
import io.github.luizgrp.sectionedrecyclerviewadapter.StatelessSection;

/**
 * Created by Catherine on 1/9/2018.
 */

public class HeaderRecyclerViewSection extends StatelessSection {
    private static final String TAG = HeaderRecyclerViewSection.class.getSimpleName();
    private String title;
    private List<String> list;
    public HeaderRecyclerViewSection(String title, List<String> list) {
        super(new SectionParameters.Builder(R.layout.header_layout)
                .headerResourceId(R.layout.item_layout)
                .build());
        this.title = title;
        this.list = list;
    }

    /*
    * Custom Click Listener for the custom dialog
    */
    public interface ListItemClickListener{
        void onListItemClickListener(int clickedItemIndex, long id, ImageView btn);
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
        ItemViewHolder iHolder = (ItemViewHolder)holder;
        iHolder.itemContent.setText(list.get(position));
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
