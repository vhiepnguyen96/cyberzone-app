package com.n8plus.vhiep.cyberzone.ui.product.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.michaelmuenzer.android.scrollablennumberpicker.ScrollableNumberPicker;
import com.n8plus.vhiep.cyberzone.data.model.Product;
import com.n8plus.vhiep.cyberzone.R;

import java.util.List;

public class ProductVerticalAdapter extends BaseAdapter {
    private Context context;
    private int resource;
    private List<Product> productList;

    public ProductVerticalAdapter(Context context, int resource, List<Product> productList) {
        this.context = context;
        this.resource = resource;
        this.productList = productList;
    }

    public class ViewHolder {
        public ImageView img_ver_product, img_order_product, img_cart_product, img_removeFromCart;
        public TextView txt_ver_productName, txt_ver_productPrice, txt_ver_productBasicPrice, txt_ver_discount,
                txt_order_productName , txt_order_productPrice, txt_order_productBasicPrice, txt_order_discount, txt_order_quantity,
                txt_cart_productName , txt_cart_productPrice, txt_cart_productBasicPrice, txt_cart_discount;
        public ScrollableNumberPicker snp_quantity_product;
    }

    @Override
    public int getCount() {
        return productList.size();
    }

    @Override
    public Object getItem(int position) {
        return productList.get(position);
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
            if (resource == R.layout.row_product_item_vertical){
                holder.img_ver_product = (ImageView) convertView.findViewById(R.id.img_ver_product);
                holder.txt_ver_productName = (TextView) convertView.findViewById(R.id.txt_ver_productName);
                holder.txt_ver_productPrice = (TextView) convertView.findViewById(R.id.txt_ver_productPrice);
                holder.txt_ver_productBasicPrice = (TextView) convertView.findViewById(R.id.txt_ver_productBasicPrice);
                holder.txt_ver_discount = (TextView) convertView.findViewById(R.id.txt_ver_discount);
            } else if (resource == R.layout.row_product_in_check_order){
                holder.img_order_product = (ImageView) convertView.findViewById(R.id.img_order_product);
                holder.txt_order_productName = (TextView) convertView.findViewById(R.id.txt_order_productName);
                holder.txt_order_productPrice = (TextView) convertView.findViewById(R.id.txt_order_productPrice);
                holder.txt_order_productBasicPrice = (TextView) convertView.findViewById(R.id.txt_order_productBasicPrice);
                holder.txt_order_discount = (TextView) convertView.findViewById(R.id.txt_order_discount);
                holder.txt_order_quantity = (TextView) convertView.findViewById(R.id.txt_order_quantity);
            } else {
                holder.img_cart_product = (ImageView) convertView.findViewById(R.id.img_cart_product);
                holder.txt_cart_productName = (TextView) convertView.findViewById(R.id.txt_cart_productName);
                holder.txt_cart_productPrice = (TextView) convertView.findViewById(R.id.txt_cart_productPrice);
                holder.txt_cart_productBasicPrice = (TextView) convertView.findViewById(R.id.txt_cart_productBasicPrice);
                holder.txt_cart_discount = (TextView) convertView.findViewById(R.id.txt_cart_discount);
                holder.img_removeFromCart = (ImageView) convertView.findViewById(R.id.img_removeFromCart);
                holder.snp_quantity_product = (ScrollableNumberPicker) convertView.findViewById(R.id.snp_product_quantity);
            }

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Product product = productList.get(position);
        if (resource == R.layout.row_product_item_vertical){
            holder.img_ver_product.setImageResource(product.getImage());
            holder.txt_ver_productName.setText(product.getName());
            if (product.getDiscount() > 0){
                holder.txt_ver_productBasicPrice.setText(product.getPrice().toString());
                float basicPrice = Float.valueOf(product.getPrice());
                int discount = product.getDiscount();
                float salePrice = basicPrice - (basicPrice * discount / 100);
                holder.txt_ver_productPrice.setText(String.format("%.3f", salePrice));
                holder.txt_ver_discount.setText(String.valueOf(product.getDiscount()));
                convertView.findViewById(R.id.layout_ver_discount).setVisibility(View.VISIBLE);
            } else {
                holder.txt_ver_productPrice.setText(product.getPrice().toString());
                convertView.findViewById(R.id.layout_ver_discount).setVisibility(View.INVISIBLE);
            }
        } else if (resource == R.layout.row_product_in_check_order){
            holder.img_order_product.setImageResource(product.getImage());
            holder.txt_order_productName.setText(product.getName());
            if (product.getDiscount() > 0){
                holder.txt_order_productBasicPrice.setText(product.getPrice().toString());
                float basicPrice = Float.valueOf(product.getPrice());
                int discount = product.getDiscount();
                float salePrice = basicPrice - (basicPrice * discount / 100);
                holder.txt_order_productPrice.setText(String.format("%.3f", salePrice));
                holder.txt_order_discount.setText(String.valueOf(product.getDiscount()));
                convertView.findViewById(R.id.layout_order_discount).setVisibility(View.VISIBLE);
            } else {
                holder.txt_order_productPrice.setText(product.getPrice().toString());
                convertView.findViewById(R.id.layout_order_discount).setVisibility(View.INVISIBLE);
            }
        } else {
            holder.img_cart_product.setImageResource(product.getImage());
            holder.txt_cart_productName.setText(product.getName());
            if (product.getDiscount() > 0){
                holder.txt_cart_productBasicPrice.setText(product.getPrice().toString());
                float basicPrice = Float.valueOf(product.getPrice());
                int discount = product.getDiscount();
                float salePrice = basicPrice - (basicPrice * discount / 100);
                holder.txt_cart_productPrice.setText(String.format("%.3f", salePrice));
                holder.txt_cart_discount.setText(String.valueOf(product.getDiscount()));
                convertView.findViewById(R.id.layout_cart_discount).setVisibility(View.VISIBLE);
            } else {
                holder.txt_cart_productPrice.setText(product.getPrice().toString());
                convertView.findViewById(R.id.layout_cart_discount).setVisibility(View.INVISIBLE);
            }
        }

        return convertView;
    }

}