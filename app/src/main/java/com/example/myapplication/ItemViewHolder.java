package com.example.myapplication;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Catherine on 1/9/2018.
 */

public class ItemViewHolder extends RecyclerView.ViewHolder{
    public TextView itemContent;
    public ImageView actionDelete;
    public View rootView;

    public ItemViewHolder(View itemView) {
        super(itemView);

        rootView = itemView;
        itemContent = (TextView)itemView.findViewById(R.id.item_content);
        actionDelete = (ImageView) itemView.findViewById(R.id.action_delete);
    }
}
