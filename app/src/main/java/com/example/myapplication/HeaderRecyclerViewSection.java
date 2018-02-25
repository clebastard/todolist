package com.example.myapplication;

import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.myapplication.data.TaskContract;
import com.example.myapplication.model.TaskDetail;

import java.util.List;

import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters;
import io.github.luizgrp.sectionedrecyclerviewadapter.StatelessSection;

/**
 * Created by Catherine on 1/9/2018.
 */

public class HeaderRecyclerViewSection extends StatelessSection {
    private String title;
    private List<TaskDetail> list;
    private Context context;
    final private ListItemClickListener mOnClickListener;

    public HeaderRecyclerViewSection(Context context, String title, List<TaskDetail> list, ListItemClickListener mOnClickListener) {
        super(new SectionParameters.Builder(R.layout.item_layout)
                .headerResourceId(R.layout.header_layout)
                .build());

        this.context = context;
        this.title = title;
        this.list = list;
        this.mOnClickListener = mOnClickListener;
    }

    /*
     * Custom Click Listener for the custom dialog
     */
    public interface ListItemClickListener{
        void onListItemClickListener(long id);
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
        final String name = list.get(position).getDescription();
        final long id = list.get(position).getId();

        iHolder.itemContent.setText(name);

        iHolder.rootView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ListActivity.class);
                Uri currentUri = ContentUris.withAppendedId(TaskContract.TaskDetailEntry.CONTENT_URI, id);

                // Set the URI on the data field of the intent
                intent.setData(currentUri);
                context.startActivity(intent);
                //Toast.makeText(view.getContext(), "Position " + ListFragment.sectionAdapter.getPositionInSection(iHolder.getAdapterPosition()), Toast.LENGTH_SHORT).show();
            }
        });

        iHolder.actionDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnClickListener.onListItemClickListener(id);
                //showDeleteConfirmationDialog(context, id);
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
