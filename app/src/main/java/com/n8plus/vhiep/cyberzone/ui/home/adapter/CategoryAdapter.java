package com.n8plus.vhiep.cyberzone.ui.home.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.n8plus.vhiep.cyberzone.data.model.Category;
import com.n8plus.vhiep.cyberzone.R;

import java.util.List;

public class CategoryAdapter extends BaseAdapter {
    private Context context;
    private int resource;
    private List<Category> arrCategory;

    public CategoryAdapter(Context context, int resource, List<Category> arrCategory) {
        this.context = context;
        this.resource = resource;
        this.arrCategory = arrCategory;
    }

    public class ViewHolder {
        TextView txt_category;
    }

    @Override
    public int getCount() {
        return arrCategory.size();
    }

    @Override
    public Object getItem(int position) {
        return arrCategory.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(resource, null);
            viewHolder = new ViewHolder();
            viewHolder.txt_category = (TextView) convertView.findViewById(R.id.txt_category);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Category category = arrCategory.get(position);
        viewHolder.txt_category.setText(category.getName());

        return convertView;
    }
}
