package com.n8plus.vhiep.cyberzone.ui.product.viewholder;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ViewHolder.ParentViewHolder;
import com.n8plus.vhiep.cyberzone.R;

public class FilterParentViewHolder extends ParentViewHolder {
    public TextView mFilterTitleTextView;
    public ImageButton mParentDropDownArrow;

    public FilterParentViewHolder(View itemView) {
        super(itemView);
        mFilterTitleTextView = (TextView) itemView.findViewById(R.id.txt_header_title);
        mParentDropDownArrow = (ImageButton) itemView.findViewById(R.id.ibn_header_more);
    }
}
