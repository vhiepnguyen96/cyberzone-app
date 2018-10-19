package com.n8plus.vhiep.cyberzone.ui.manage.mydeliveryaddress.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.n8plus.vhiep.cyberzone.R;
import com.n8plus.vhiep.cyberzone.data.model.District;
import com.n8plus.vhiep.cyberzone.data.model.Province;

import java.util.List;

public class SpinnerDisrictAdapter extends BaseAdapter{

    private Context context;
    private int resource;
    private List<District> districtList;

    public SpinnerDisrictAdapter(Context context, int resource, List<District> districtList) {
        this.context = context;
        this.resource = resource;
        this.districtList = districtList;
    }

    public class ViewHolder {
        TextView tv_name_spinner;
        ImageView img_check_spinner;
    }

    @Override
    public int getCount() {
        return districtList.size();
    }

    @Override
    public Object getItem(int position) {
        return districtList.get(position);
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
            viewHolder.tv_name_spinner = (TextView) convertView.findViewById(R.id.tv_name_spinner);
            viewHolder.img_check_spinner = (ImageView) convertView.findViewById(R.id.img_check_spinner);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tv_name_spinner.setText(districtList.get(position).getName());

        return convertView;
    }
}
