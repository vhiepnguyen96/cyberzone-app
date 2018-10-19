package com.n8plus.vhiep.cyberzone.ui.product.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bignerdranch.expandablerecyclerview.Adapter.ExpandableRecyclerAdapter;
import com.bignerdranch.expandablerecyclerview.Model.ParentObject;
import com.n8plus.vhiep.cyberzone.data.model.Filter;
import com.n8plus.vhiep.cyberzone.data.model.FilterChild;
import com.n8plus.vhiep.cyberzone.R;
import com.n8plus.vhiep.cyberzone.ui.product.viewholder.FilterChildViewHolder;
import com.n8plus.vhiep.cyberzone.ui.product.viewholder.FilterParentViewHolder;

import java.util.ArrayList;
import java.util.List;

public class FilterExpandableAdapter extends ExpandableRecyclerAdapter<FilterParentViewHolder, FilterChildViewHolder> {

    LayoutInflater mInflater;
    private ArrayList<ParentObject> parentObjects;

    public FilterExpandableAdapter(Context context, ArrayList<ParentObject> parentItemList) {
        super(context, parentItemList);
        parentObjects = parentItemList;
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
        filterParentViewHolder.mFilterTitleTextView.setText(filter.getFilterName());
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindChildViewHolder(FilterChildViewHolder filterChildViewHolder, int i, Object o) {
        final FilterChild filterChild = (FilterChild) o;
        filterChildViewHolder.txt_item.setText(filterChild.getName());
        if (filterChild.isState()) {
            filterChildViewHolder.txt_item.setBackgroundResource(R.drawable.filter_selected);
        } else {
            filterChildViewHolder.txt_item.setBackgroundResource(R.drawable.filter_unselected);
        }

        filterChildViewHolder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                if (isLongClick) {
                    Toast.makeText(view.getContext(), "Long Click", Toast.LENGTH_SHORT).show();
                } else {
                    filterChild.setState(!filterChild.isState());
                    notifyDataSetChanged();
                }
            }
        });
    }

    public ArrayList<Object> getFilterChildChoose() {
        ArrayList<Object> arrayList = new ArrayList<>();
        for (int i = 0; i < parentObjects.size(); i++) {
            for (int j = 0; j < parentObjects.get(i).getChildObjectList().size(); j++) {
                FilterChild filterChild = (FilterChild) parentObjects.get(i).getChildObjectList().get(j);
                if (filterChild.isState()) {
                    arrayList.add(parentObjects.get(i).getChildObjectList().get(j));
                }
            }
        }
        return arrayList;
    }

    public interface ItemClickListener {
        void onClick(View view, int position, boolean isLongClick);
    }
}
