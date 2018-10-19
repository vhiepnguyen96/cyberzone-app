package com.n8plus.vhiep.cyberzone.ui.home.adapter;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.n8plus.vhiep.cyberzone.data.model.Product;
import com.n8plus.vhiep.cyberzone.R;
import com.n8plus.vhiep.cyberzone.ui.productdetails.ProductDetailActivity;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.List;

public class ProductHorizontalAdapter extends RecyclerView.Adapter<ProductHorizontalAdapter.MyViewHolder> {
    private List<Product> productList;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        public ImageView img_hor_product;
        public TextView txt_hor_productName, txt_hor_productPrice, txt_hor_productBasicPrice, txt_hor_discount;
        private ItemClickListener itemClickListener;

        public MyViewHolder(View itemView) {
            super(itemView);
            img_hor_product = (ImageView) itemView.findViewById(R.id.img_hor_product);
            txt_hor_productName = (TextView) itemView.findViewById(R.id.txt_hor_productName);
            txt_hor_productPrice = (TextView) itemView.findViewById(R.id.txt_hor_productPrice);
            txt_hor_productBasicPrice = (TextView) itemView.findViewById(R.id.txt_hor_productBasicPrice);
            txt_hor_discount = (TextView) itemView.findViewById(R.id.txt_hor_discount);

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
        final Product product = productList.get(position);
        DecimalFormat df = new DecimalFormat("#.000");
        if (product.getImageList() != null && product.getImageList().size() > 0){
            Picasso.get().load(product.getImageList().get(0).getImageURL()).into(holder.img_hor_product);
        }
//        holder.img_hor_product.setImageResource(product.getImageList().get(0).getImageId());
        holder.txt_hor_productName.setText(product.getProductName());
        if (product.getSaleOff().getDiscount() != 0) {
            int basicPrice = Integer.valueOf(product.getPrice());
            int discount = product.getSaleOff().getDiscount();
            int salePrice = basicPrice - (basicPrice * discount / 100);

            holder.txt_hor_productBasicPrice.setText(df.format(Product.convertToPrice(String.valueOf(basicPrice))));
            holder.txt_hor_productPrice.setText(df.format(Product.convertToPrice(String.valueOf(salePrice))));
            holder.txt_hor_discount.setText(String.valueOf(product.getSaleOff().getDiscount()));
        } else {
            holder.txt_hor_productPrice.setText(df.format(Product.convertToPrice(product.getPrice())));
            holder.itemView.findViewById(R.id.layout_hor_discount).setVisibility(View.INVISIBLE);
        }

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                if (isLongClick) {
                    Toast.makeText(view.getContext(), "Long Click", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(view.getContext(), ProductDetailActivity.class);
                    intent.putExtra("product", productList.get(position));
                    view.getContext().startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public interface ItemClickListener {
        void onClick(View view, int position, boolean isLongClick);
    }

}