package com.n8plus.vhiep.cyberzone.ui.product.viewholder;

import android.view.View;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ViewHolder.ChildViewHolder;
import com.n8plus.vhiep.cyberzone.R;
import com.n8plus.vhiep.cyberzone.ui.product.adapter.FilterExpandableAdapter;
import com.n8plus.vhiep.cyberzone.ui.product.adapter.RecyclerProductAdapter;

public class FilterChildViewHolder extends ChildViewHolder implements View.OnClickListener, View.OnLongClickListener{
    public TextView txt_item;
    private FilterExpandableAdapter.ItemClickListener itemClickListener;

    public FilterChildViewHolder(View itemView) {
        super(itemView);
        txt_item = (TextView) itemView.findViewById(R.id.txt_item);
        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);
    }

    public void setItemClickListener(FilterExpandableAdapter.ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v, getAdapterPosition(), false);
    }

    @Override
    public boolean onLongClick(View v) {
        itemClickListener.onClick(v, getAdapterPosition(), true);
        return true;
    }

}


