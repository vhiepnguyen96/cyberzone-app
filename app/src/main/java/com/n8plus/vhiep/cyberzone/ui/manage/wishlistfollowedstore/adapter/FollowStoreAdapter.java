package com.n8plus.vhiep.cyberzone.ui.manage.wishlistfollowedstore.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.n8plus.vhiep.cyberzone.R;
import com.n8plus.vhiep.cyberzone.data.model.Store;

import java.util.List;

public class FollowStoreAdapter extends BaseAdapter{
    private Context context;
    private int resource;
    private List<Store> storeList;

    public FollowStoreAdapter(Context context, int resource, List<Store> storeList) {
        this.context = context;
        this.resource = resource;
        this.storeList = storeList;
    }

    public class ViewHolder {
        public ImageView img_follow_store_logo;
        public TextView tv_follow_store_name, tv_follow_store_location, tv_follow_store_createdate, tv_follow_store_categories;
    }

    @Override
    public int getCount() {
        return storeList.size();
    }

    @Override
    public Object getItem(int position) {
        return storeList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(resource, null);
            holder = new ViewHolder();
            holder.img_follow_store_logo = (ImageView) convertView.findViewById(R.id.img_follow_store_logo);
            holder.tv_follow_store_name = (TextView) convertView.findViewById(R.id.tv_follow_store_name);
            holder.tv_follow_store_location = (TextView) convertView.findViewById(R.id.tv_follow_store_location);
            holder.tv_follow_store_createdate = (TextView) convertView.findViewById(R.id.tv_follow_store_createdate);
            holder.tv_follow_store_categories = (TextView) convertView.findViewById(R.id.tv_follow_store_categories);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Store store = storeList.get(position);
        holder.tv_follow_store_name.setText(store.getStoreName());
        holder.tv_follow_store_location.setText(store.getLocation());
        holder.tv_follow_store_createdate.setText("9");

        // Get category
        StringBuilder builder = new StringBuilder();
        for (int i=0; i<store.getCategories().size(); i++){
            builder.append(store.getCategories().get(i).getName());
            if (i != store.getCategories().size()){
                builder.append(", ");
            }
        }
        holder.tv_follow_store_categories.setText(builder.toString());

        convertView.findViewById(R.id.lnr_un_follow_store).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "Un follow", Toast.LENGTH_SHORT).show();
            }
        });

        convertView.findViewById(R.id.lnr_go_to_store).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "Go to store", Toast.LENGTH_SHORT).show();
            }
        });

        return convertView;
    }
}
