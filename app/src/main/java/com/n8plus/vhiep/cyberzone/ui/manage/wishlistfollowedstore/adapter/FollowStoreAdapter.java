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
import com.n8plus.vhiep.cyberzone.data.model.Category;
import com.n8plus.vhiep.cyberzone.data.model.FollowStore;
import com.n8plus.vhiep.cyberzone.data.model.Store;
import com.n8plus.vhiep.cyberzone.ui.manage.wishlistfollowedstore.followedstore.FollowedStoreFragment;

import java.util.List;

public class FollowStoreAdapter extends BaseAdapter {
    private FollowedStoreFragment context;
    private int resource;
    private List<FollowStore> followStores;

    public FollowStoreAdapter(FollowedStoreFragment context, int resource, List<FollowStore> followStores) {
        this.context = context;
        this.resource = resource;
        this.followStores = followStores;
    }

    public class ViewHolder {
        public ImageView img_follow_store_logo;
        public TextView tv_follow_store_name, tv_follow_store_location, tv_follow_store_createdate, tv_follow_store_categories;
    }

    @Override
    public int getCount() {
        return followStores.size();
    }

    @Override
    public Object getItem(int position) {
        return followStores.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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

        Store store = followStores.get(position).getStore();
        holder.tv_follow_store_name.setText(store.getStoreName());
        holder.tv_follow_store_location.setText(store.getLocation());
        holder.tv_follow_store_createdate.setText(store.getTimeInSystem());
        if (store.getCategories() != null && store.getCategories().size() > 0) {
            holder.tv_follow_store_categories.setText(getPopularCategory(store.getCategories()));
        }

        convertView.findViewById(R.id.lnr_un_follow_store).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (context instanceof FollowedStoreFragment) {
                    context.actionUnfollowStore(position);
                }
            }
        });

        convertView.findViewById(R.id.lnr_go_to_store).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (context instanceof FollowedStoreFragment) {
                    context.actionGoToStore(position);
                }
            }
        });

        return convertView;
    }

    public String getPopularCategory(List<Category> categories) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < categories.size(); i++) {
            if (i < categories.size() - 1) {
                builder.append(categories.get(i).getName() + ", ");
            } else {
                builder.append(categories.get(i).getName());
            }
        }
        return builder.toString();
    }
}
