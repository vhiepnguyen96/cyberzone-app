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
import android.widget.Toast;

import com.n8plus.vhiep.cyberzone.data.model.ProductType;
import com.n8plus.vhiep.cyberzone.R;
import com.n8plus.vhiep.cyberzone.ui.home.HomeActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PopularCategoryAdapter extends RecyclerView.Adapter<PopularCategoryAdapter.MyViewHolder> {

    private List<ProductType> producTypeList;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        public ImageView img_popularCategory;
        public TextView txt_popularCategory;
        private ItemClickListener itemClickListener;

        public MyViewHolder(View itemView) {
            super(itemView);
            img_popularCategory = (ImageView) itemView.findViewById(R.id.img_popularCategory);
            txt_popularCategory = (TextView) itemView.findViewById(R.id.txt_popularCategory);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        public void setItemClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }

        @Override
        public void onClick(View v) {
            itemClickListener.onClick(v, getAdapterPosition(), false);
        }

        @Override
        public boolean onLongClick(View v) {
            itemClickListener.onClick(v, getAdapterPosition(), true);
            return true;
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
        if (producTypeList != null) {
            holder.txt_popularCategory.setText(producTypeList.get(position).getName());
            if (producTypeList.get(position).getImageURL() != null){
                Picasso.get().load(producTypeList.get(position).getImageURL()).resize(64, 64).centerInside().into(holder.img_popularCategory);
            } else {
                Picasso.get().load(R.drawable.ic_image_gray_24dp).resize(64, 64).centerInside().into(holder.img_popularCategory);
            }
        }

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                if (isLongClick) {
                    Toast.makeText(view.getContext(), "Long Click", Toast.LENGTH_SHORT).show();
                } else {
                    if (view.getContext() instanceof HomeActivity) {
                        ((HomeActivity) view.getContext()).popularCategoryItemSelected(producTypeList.get(position).getId());
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return producTypeList.size();
    }

    public interface ItemClickListener {
        void onClick(View view, int position, boolean isLongClick);
    }
}
