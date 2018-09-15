package com.n8plus.vhiep.cyberzone.ui.home.adapter;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.n8plus.vhiep.cyberzone.data.model.ProductType;
import com.n8plus.vhiep.cyberzone.R;

import java.util.List;

public class PopularCategoryAdapter extends RecyclerView.Adapter<PopularCategoryAdapter.MyViewHolder> {

    private List<ProductType> producTypeList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView img_popularCategory;
        public TextView txt_popularCategory;
        public LinearLayout layout_popular_category;

        public MyViewHolder(View itemView) {
            super(itemView);
            img_popularCategory = (ImageView) itemView.findViewById(R.id.img_popularCategory);
            txt_popularCategory = (TextView) itemView.findViewById(R.id.txt_popularCategory);
            layout_popular_category = (LinearLayout) itemView.findViewById(R.id.layout_popular_category);
        }
    }

    public PopularCategoryAdapter(List<ProductType> producTypeList) {
        this.producTypeList = producTypeList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_popular_category, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ProductType productType = producTypeList.get(position);
        holder.layout_popular_category.setBackgroundColor(position%2 ==1 ? Color.parseColor("#ECECEC") : Color.parseColor("#ffffff"));
        holder.txt_popularCategory.setText(productType.getName());
        holder.img_popularCategory.setImageResource(productType.getIcon());
    }

    @Override
    public int getItemCount() {
        return producTypeList.size();
    }


}
