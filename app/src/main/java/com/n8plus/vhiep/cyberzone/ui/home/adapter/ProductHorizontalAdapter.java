package com.n8plus.vhiep.cyberzone.ui.home.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.n8plus.vhiep.cyberzone.data.model.Product;
import com.n8plus.vhiep.cyberzone.R;

import java.util.List;

public class ProductHorizontalAdapter extends RecyclerView.Adapter<ProductHorizontalAdapter.MyViewHolder> {
private List<Product> productList;

public class MyViewHolder extends RecyclerView.ViewHolder {
    public ImageView img_hor_product;
    public TextView txt_hor_productName, txt_hor_productPrice, txt_hor_productBasicPrice, txt_hor_discount;

    public MyViewHolder(View itemView) {
        super(itemView);
        img_hor_product = (ImageView) itemView.findViewById(R.id.img_hor_product);
        txt_hor_productName = (TextView) itemView.findViewById(R.id.txt_hor_productName);
        txt_hor_productPrice = (TextView) itemView.findViewById(R.id.txt_hor_productPrice);
        txt_hor_productBasicPrice = (TextView) itemView.findViewById(R.id.txt_hor_productBasicPrice);
        txt_hor_discount = (TextView) itemView.findViewById(R.id.txt_hor_discount);
    }
}

    public ProductHorizontalAdapter(List<Product> productList) {
        this.productList = productList;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_product_item_horizontal, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.img_hor_product.setImageResource(product.getImage());
        holder.txt_hor_productName.setText(product.getName());
        if (product.getDiscount() != 0){
            holder.txt_hor_productBasicPrice.setText(product.getPrice().toString());
            float basicPrice = Float.valueOf(product.getPrice());
            int discount = product.getDiscount();
            float salePrice = basicPrice - (basicPrice * discount / 100);
            holder.txt_hor_productPrice.setText(String.format("%.3f", salePrice));
            holder.txt_hor_discount.setText(String.valueOf(product.getDiscount()));
        } else {
            holder.txt_hor_productPrice.setText(product.getPrice().toString());
            holder.itemView.findViewById(R.id.layout_hor_discount).setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

}