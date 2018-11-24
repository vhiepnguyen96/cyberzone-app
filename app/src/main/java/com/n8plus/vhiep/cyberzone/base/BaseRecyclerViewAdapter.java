package com.n8plus.vhiep.cyberzone.base;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseRecyclerViewAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    protected LayoutInflater mInflater;
    protected List<T> mDataList;
    protected List<T> mDataListCopy = new ArrayList<>();
    protected ItemClickListener mItemClickListener;

    protected BaseRecyclerViewAdapter(@NonNull Context context,
                                      ItemClickListener itemClickListener) {
        mInflater = LayoutInflater.from(context);
        mItemClickListener = itemClickListener;
        mDataList = new ArrayList<>();
    }

    public void add(List<T> itemList) {
        mDataList.addAll(itemList);
        notifyDataSetChanged();
        mDataListCopy.clear();
        mDataListCopy.addAll(mDataList);
    }

    public void set(List<T> dataList) {
        List<T> clone = new ArrayList<>(dataList);
        mDataList.clear();
        mDataListCopy.clear();
        mDataList.addAll(clone);
        mDataListCopy.addAll(clone);
        notifyDataSetChanged();
    }

    public List<T> getDataList() {
        return mDataList;
    }

    public List<T> getDataListCopy() {
        return mDataListCopy;
    }

    public void clear() {
        mDataList.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
