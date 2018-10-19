package com.n8plus.vhiep.cyberzone.ui.manage.wishlistfollowedstore.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.n8plus.vhiep.cyberzone.R;
import com.n8plus.vhiep.cyberzone.data.model.Product;
import com.n8plus.vhiep.cyberzone.data.model.WishList;
import com.n8plus.vhiep.cyberzone.ui.product.adapter.ProductVerticalAdapter;
import com.squareup.picasso.Picasso;

import java.util.List;

public class WishListAdapter extends BaseAdapter {
    private Context context;
    private int resource;
    private WishList wishList;

    public WishListAdapter(Context context, int resource, WishList wishList) {
        this.context = context;
        this.resource = resource;
        this.wishList = wishList;
    }

    public class ViewHolder {
        public ImageView img_wishlist_product;
        public TextView tv_wishlist_name_store, tv_wishlist_product_name, tv_wishlist_product_price, tv_wishlist_productbasic_price, tv_wishlist_discount;
        public LinearLayout lnr_remove_wishlist, lnr_add_to_cart;
    }

    @Override
    public int getCount() {
        return wishList.getProductList().size();
    }

    @Override
    public Object getItem(int position) {
        return wishList.getProductList().get(position);
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
            holder.img_wishlist_product = (ImageView) convertView.findViewById(R.id.img_wishlist_product);
            holder.tv_wishlist_name_store = (TextView) convertView.findViewById(R.id.tv_wishlist_name_store);
            holder.tv_wishlist_product_name = (TextView) convertView.findViewById(R.id.tv_wishlist_product_name);
            holder.tv_wishlist_product_price = (TextView) convertView.findViewById(R.id.tv_wishlist_product_price);
            holder.tv_wishlist_productbasic_price = (TextView) convertView.findViewById(R.id.tv_wishlist_productbasic_price);
            holder.tv_wishlist_discount = (TextView) convertView.findViewById(R.id.tv_wishlist_discount);
            holder.lnr_remove_wishlist = (LinearLayout) convertView.findViewById(R.id.lnr_remove_wishlist);
            holder.lnr_add_to_cart = (LinearLayout) convertView.findViewById(R.id.lnr_add_to_cart);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Product product = wishList.getProductList().get(position);
        holder.tv_wishlist_name_store.setText(product.getStore().getStoreName());
        if (product.getImageList() != null && product.getImageList().size() > 0){
            Picasso.get().load(product.getImageList().get(0).getImageURL()).into(holder.img_wishlist_product);
        }
//        holder.img_wishlist_product.setImageResource(product.getImageList().get(0).getImageId());
        holder.tv_wishlist_product_name.setText(product.getProductName());
        if (product.getSaleOff().getDiscount() > 0) {
            holder.tv_wishlist_productbasic_price.setText(product.getPrice().toString());
            float basicPrice = Float.valueOf(product.getPrice());
            int discount = product.getSaleOff().getDiscount();
            float salePrice = basicPrice - (basicPrice * discount / 100);
            holder.tv_wishlist_product_price.setText(String.format("%.3f", salePrice));
            holder.tv_wishlist_discount.setText(String.valueOf(product.getSaleOff().getDiscount()));
            convertView.findViewById(R.id.layout_wishlist_discount).setVisibility(View.VISIBLE);
        } else {
            holder.tv_wishlist_product_price.setText(product.getPrice().toString());
            convertView.findViewById(R.id.layout_wishlist_discount).setVisibility(View.INVISIBLE);
        }

        return convertView;
    }
}
