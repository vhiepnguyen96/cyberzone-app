package com.n8plus.vhiep.cyberzone.ui.product.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bignerdranch.expandablerecyclerview.Adapter.ExpandableRecyclerAdapter;
import com.bignerdranch.expandablerecyclerview.Model.ParentObject;
import com.n8plus.vhiep.cyberzone.data.model.Filter;
import com.n8plus.vhiep.cyberzone.data.model.FilterChild;
import com.n8plus.vhiep.cyberzone.R;
import com.n8plus.vhiep.cyberzone.ui.product.viewholder.FilterChildViewHolder;
import com.n8plus.vhiep.cyberzone.ui.product.viewholder.FilterParentViewHolder;

import java.util.List;

public class FilterExpandableAdapter extends ExpandableRecyclerAdapter<FilterParentViewHolder, FilterChildViewHolder>{

    LayoutInflater mInflater;

    public FilterExpandableAdapter(Context context, List<ParentObject> parentItemList) {
        super(context, parentItemList);
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public FilterParentViewHolder onCreateParentViewHolder(ViewGroup viewGroup) {
        View view = mInflater.inflate(R.layout.expandable_header_custom, viewGroup, false);
        return new FilterParentViewHolder(view);
    }

    @Override
    public FilterChildViewHolder onCreateChildViewHolder(ViewGroup viewGroup) {
        View view = mInflater.inflate(R.layout.expandable_item_custom, viewGroup, false);
        return new FilterChildViewHolder(view);
    }

    @Override
    public void onBindParentViewHolder(FilterParentViewHolder filterParentViewHolder, int i, Object o) {
        Filter filter = (Filter) o;
        filterParentViewHolder.mFilterTitleTextView.setText(filter.getTitle());
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindChildViewHolder(FilterChildViewHolder filterChildViewHolder, int i, Object o) {
        FilterChild filterChild = (FilterChild) o;
        filterChildViewHolder.txt_item.setText(filterChild.getName());
        if (filterChild.isState()){
            filterChildViewHolder.txt_item.setBackgroundResource(R.drawable.filter_selected);
        } else {
            filterChildViewHolder.txt_item.setBackgroundResource(R.drawable.filter_unselected);
        }
    }
}
