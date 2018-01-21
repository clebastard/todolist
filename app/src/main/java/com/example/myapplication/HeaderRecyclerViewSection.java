package com.example.myapplication;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.View;
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
    private Context context;

    public HeaderRecyclerViewSection(Context context, String title, List<String> list) {
        super(new SectionParameters.Builder(R.layout.item_layout)
                .headerResourceId(R.layout.header_layout)
                .build());

        this.context = context;
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
                Toast.makeText(view.getContext(), "Position " + ListFragment.sectionAdapter.getPositionInSection(iHolder.getAdapterPosition()), Toast.LENGTH_SHORT).show();
            }
        });

        iHolder.actionDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDeleteConfirmationDialog(context, ListFragment.sectionAdapter.getPositionInSection(iHolder.getAdapterPosition()));
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

    public void showDeleteConfirmationDialog(final Context context, final long idx) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(context.getString(R.string.askDelete));
        builder.setPositiveButton(R.string.deleteTitle, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Toast.makeText(context, context.getString(R.string.inDevelopment) + " position " + idx,
                        Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton(R.string.cancelTitle, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Cancel" button, so dismiss the dialog
                // and continue editing the list.
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
