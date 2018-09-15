package com.n8plus.vhiep.cyberzone.ui.product.viewholder;

import android.view.View;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ViewHolder.ChildViewHolder;
import com.n8plus.vhiep.cyberzone.R;

public class FilterChildViewHolder extends ChildViewHolder {
    public TextView txt_item;

    public FilterChildViewHolder(View itemView) {
        super(itemView);

        txt_item = (TextView) itemView.findViewById(R.id.txt_item);
    }

}
