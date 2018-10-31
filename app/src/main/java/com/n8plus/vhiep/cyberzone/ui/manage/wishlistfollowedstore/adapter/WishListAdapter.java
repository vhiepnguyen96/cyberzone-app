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
import com.n8plus.vhiep.cyberzone.ui.manage.wishlistfollowedstore.wishlist.WishListFragment;
import com.n8plus.vhiep.cyberzone.ui.product.adapter.ProductVerticalAdapter;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.List;

public class WishListAdapter extends BaseAdapter {
    private WishListFragment context;
    private int resource;
    private List<WishList> wishList;

    public WishListAdapter(WishListFragment context, int resource, List<WishList> wishList) {
        this.context = context;
        this.resource = resource;
        this.wishList = wishList;
    }

    public class ViewHolder {
        public ImageView img_wishlist_product;
        public TextView tv_wishlist_product_name, tv_wishlist_product_price, tv_wishlist_product_basic_price, tv_wishlist_product_discount;
        public LinearLayout lnr_product, lnr_product_discount, lnr_remove_wishlist, lnr_add_to_cart;
    }

    @Override
    public int getCount() {
        return wishList.size();
    }

    @Override
    public Object getItem(int position) {
        return wishList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        DecimalFormat df = new DecimalFormat("#.000");
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(resource, null);
            holder = new ViewHolder();
            holder.img_wishlist_product = (ImageView) convertView.findViewById(R.id.img_wishlist_product);
            holder.tv_wishlist_product_name = (TextView) convertView.findViewById(R.id.tv_wishlist_product_name);
            holder.tv_wishlist_product_price = (TextView) convertView.findViewById(R.id.tv_wishlist_product_price);
            holder.tv_wishlist_product_basic_price = (TextView) convertView.findViewById(R.id.tv_wishlist_product_basic_price);
            holder.tv_wishlist_product_discount = (TextView) convertView.findViewById(R.id.tv_wishlist_product_discount);
            holder.lnr_product = (LinearLayout) convertView.findViewById(R.id.lnr_product);
            holder.lnr_product_discount = (LinearLayout) convertView.findViewById(R.id.lnr_product_discount);
            holder.lnr_remove_wishlist = (LinearLayout) convertView.findViewById(R.id.lnr_remove_wishlist);
            holder.lnr_add_to_cart = (LinearLayout) convertView.findViewById(R.id.lnr_add_to_cart);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Product product = wishList.get(position).getProduct();
        if (product.getImageList() != null && product.getImageList().size() > 0) {
            Picasso.get().load(product.getImageList().get(0).getImageURL()).into(holder.img_wishlist_product);
        }
        holder.tv_wishlist_product_name.setText(product.getProductName());

        if (product.getPrice() != null) {
            if (product.getSaleOff() != null && product.getSaleOff().getDiscount() > 0){
                int basicPrice = Integer.valueOf(product.getPrice());
                int discount = product.getSaleOff().getDiscount();
                int salePrice = basicPrice - (basicPrice * discount / 100);

                holder.tv_wishlist_product_basic_price.setText(basicPrice > 1000 ? df.format(Product.convertToPrice(String.valueOf(basicPrice))).replace(",", ".") : String.valueOf(basicPrice));
                holder.tv_wishlist_product_price.setText(salePrice > 1000 ? df.format(Product.convertToPrice(String.valueOf(salePrice))).replace(",", ".") : String.valueOf(salePrice));

                holder.tv_wishlist_product_discount.setText(String.valueOf(product.getSaleOff().getDiscount()));
                holder.lnr_product_discount.setVisibility(View.VISIBLE);
            } else {
                holder.tv_wishlist_product_price.setText(Integer.valueOf(product.getPrice()) > 1000 ? df.format(Product.convertToPrice(product.getPrice())).replace(",", ".") : product.getPrice());
                holder.lnr_product_discount.setVisibility(View.GONE);
            }
        }

        holder.lnr_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (context instanceof WishListFragment) {
                    context.actionMoveToProductDetail(position);
                }
            }
        });

        holder.lnr_remove_wishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (context instanceof WishListFragment) {
                    context.actionRemoveFromWishList(position);
                }
            }
        });

        holder.lnr_add_to_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (context instanceof WishListFragment) {
                    context.actionAddToCart(position);
                }
            }
        });

        return convertView;
    }
}
