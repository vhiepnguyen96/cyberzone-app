package com.n8plus.vhiep.cyberzone.ui.product.adapter;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.n8plus.vhiep.cyberzone.R;
import com.n8plus.vhiep.cyberzone.data.model.Filter;
import com.n8plus.vhiep.cyberzone.data.model.FilterChild;
import com.n8plus.vhiep.cyberzone.data.model.Product;
import com.n8plus.vhiep.cyberzone.ui.productdetails.ProductDetailActivity;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class RecyclerProductAdapter extends RecyclerView.Adapter<RecyclerProductAdapter.MyViewHolder> {
    private List<Product> productList;
    private List<Product> productListCopy = new ArrayList<>();
    private int resource;

    public RecyclerProductAdapter(int resource, List<Product> productList) {
        this.resource = resource;
        this.productList = productList;
        productListCopy.addAll(productList);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        ImageView img_ver_product;
        TextView txt_ver_productName, txt_ver_productPrice, txt_ver_productBasicPrice, txt_ver_discount, tv_product_rating_count_grid, tv_store_location_product, tv_product_quantity_none;
        LinearLayout layout_ver_discount, lnr_product_rating_grid;
        RatingBar rbr_product_rating_gird;
        private ItemClickListener itemClickListener;

        public MyViewHolder(View itemView) {
            super(itemView);
            img_ver_product = (ImageView) itemView.findViewById(R.id.img_ver_product);
            txt_ver_productName = (TextView) itemView.findViewById(R.id.txt_ver_productName);
            txt_ver_productPrice = (TextView) itemView.findViewById(R.id.txt_ver_productPrice);
            txt_ver_productBasicPrice = (TextView) itemView.findViewById(R.id.txt_ver_productBasicPrice);
            txt_ver_discount = (TextView) itemView.findViewById(R.id.txt_ver_discount);
            tv_product_rating_count_grid = (TextView) itemView.findViewById(R.id.tv_product_rating_count_grid);
            tv_store_location_product = (TextView) itemView.findViewById(R.id.tv_store_location_product);
            tv_product_quantity_none = (TextView) itemView.findViewById(R.id.tv_product_quantity_none);
            layout_ver_discount = (LinearLayout) itemView.findViewById(R.id.layout_ver_discount);
            lnr_product_rating_grid = (LinearLayout) itemView.findViewById(R.id.lnr_product_rating_grid);
            rbr_product_rating_gird = (RatingBar) itemView.findViewById(R.id.rbr_product_rating_gird);
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

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(resource, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final Product product = productList.get(position);
        DecimalFormat df = new DecimalFormat("#.000");

        if (product.getImageList() != null) {
            if (product.getImageList().get(0).getImageURL() != null && !product.getImageList().get(0).getImageURL().isEmpty()) {
                Uri uriImage = Uri.parse(product.getImageList().get(0).getImageURL());
                System.out.println("Image url: " + uriImage);
                Picasso.get().load(uriImage).error(R.drawable.ic_image_gray_24dp).into(holder.img_ver_product);
            } else {
                holder.img_ver_product.setImageResource(R.drawable.ic_image_gray_24dp);
            }
        }
        holder.txt_ver_productName.setText(product.getProductName());
        if (product.getSaleOff() != null && product.getSaleOff().getDiscount() > 0) {
            int basicPrice = Integer.valueOf(product.getPrice());
            int discount = product.getSaleOff().getDiscount();
            int salePrice = basicPrice - (basicPrice * discount / 100);

            holder.txt_ver_productBasicPrice.setText(df.format(Product.convertToPrice(String.valueOf(basicPrice))));
            holder.txt_ver_productPrice.setText(df.format(Product.convertToPrice(String.valueOf(salePrice))));

            holder.txt_ver_discount.setText(String.valueOf(product.getSaleOff().getDiscount()));
            holder.layout_ver_discount.setVisibility(View.VISIBLE);
        } else {
            holder.txt_ver_productPrice.setText(df.format(Product.convertToPrice(product.getPrice())));
            holder.layout_ver_discount.setVisibility(View.GONE);
        }

        if (product.getReviewProducts() != null && product.getReviewProducts().size() > 0) {
            holder.lnr_product_rating_grid.setVisibility(View.VISIBLE);
            holder.tv_product_rating_count_grid.setText(String.valueOf(product.getReviewProducts().size()));
            int c5star = 0, c4star = 0, c3star = 0, c2star = 0, c1star = 0;
            for (int i = 0; i < product.getReviewProducts().size(); i++) {
                switch ((int) product.getReviewProducts().get(i).getRatingStar().getRatingStar()) {
                    case 5:
                        c5star++;
                        break;
                    case 4:
                        c4star++;
                        break;
                    case 3:
                        c3star++;
                        break;
                    case 2:
                        c2star++;
                        break;
                    case 1:
                        c1star++;
                        break;
                }
            }
            float average_star = (5 * c5star + 4 * c4star + 3 * c3star + 2 * c2star + 1 * c1star) / product.getReviewProducts().size();
            holder.rbr_product_rating_gird.setRating(average_star);
        }

        holder.tv_store_location_product.setText(product.getStore().getLocation());

        holder.tv_product_quantity_none.setVisibility(product.getQuantity() == 0 ? View.VISIBLE : View.INVISIBLE);

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

    public List<Product> getProductList() {
        return productList;
    }

    public interface ItemClickListener {
        void onClick(View view, int position, boolean isLongClick);
    }

    public void searchProduct(String text) {
        productList = new ArrayList<>();
        if (text.isEmpty()) {
            productList.addAll(productListCopy);
        } else {
            String keyword = text.toLowerCase();
            for (Product item : productListCopy) {
                if (item.getProductName().toLowerCase().contains(keyword)) { // || item.phone.toLowerCase().contains(text)s
                    productList.add(item);
                }
            }
        }
        notifyDataSetChanged();
    }

    public void filterProduct(List<FilterChild> filters) {
        productList = new ArrayList<>();
        if (filters.size() == 0) {
            productList.addAll(productListCopy);
        } else {
            for (int i = 0; i < filters.size(); i++) {
                for (Product item : productListCopy) {
                    if (item.getProductType().getId().equals(filters.get(i).getValue())) {
                        productList.add(item);
                    }
                }
            }
        }
        notifyDataSetChanged();
    }
}
