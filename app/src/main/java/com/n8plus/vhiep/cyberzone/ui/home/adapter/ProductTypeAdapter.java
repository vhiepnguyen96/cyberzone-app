package com.n8plus.vhiep.cyberzone.ui.home.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.n8plus.vhiep.cyberzone.data.model.ProductType;
import com.n8plus.vhiep.cyberzone.R;

import java.util.List;

public class ProductTypeAdapter extends BaseAdapter {
    private Context context;
    private int resource;
    private List<ProductType> arrProductType;

    public ProductTypeAdapter(Context context, int resource, List<ProductType> arrProductType) {
        this.context = context;
        this.resource = resource;
        this.arrProductType = arrProductType;
    }

    public class ViewHolder {
        TextView txt_productType;
    }

    @Override
    public int getCount() {
        return arrProductType.size();
    }

    @Override
    public Object getItem(int position) {
        return arrProductType.get(position);
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
            viewHolder.txt_productType = (TextView) convertView.findViewById(R.id.txt_productType);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        ProductType productType = arrProductType.get(position);
        viewHolder.txt_productType.setText(productType.getName());

        return convertView;
    }
}

