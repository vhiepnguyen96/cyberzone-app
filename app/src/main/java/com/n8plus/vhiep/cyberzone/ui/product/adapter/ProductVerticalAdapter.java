package com.n8plus.vhiep.cyberzone.ui.product.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.michaelmuenzer.android.scrollablennumberpicker.ScrollableNumberPicker;
import com.n8plus.vhiep.cyberzone.data.model.Product;
import com.n8plus.vhiep.cyberzone.R;
import com.squareup.picasso.Picasso;

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
        public ImageView img_ver_product;
        public TextView txt_ver_productName, txt_ver_productPrice, txt_ver_productBasicPrice, txt_ver_discount, tv_product_rating_count_vertical;
        public RatingBar rbr_product_rating_vertical;
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
            holder.img_ver_product = (ImageView) convertView.findViewById(R.id.img_ver_product);
            holder.txt_ver_productName = (TextView) convertView.findViewById(R.id.txt_ver_productName);
            holder.txt_ver_productPrice = (TextView) convertView.findViewById(R.id.txt_ver_productPrice);
            holder.txt_ver_productBasicPrice = (TextView) convertView.findViewById(R.id.txt_ver_productBasicPrice);
            holder.txt_ver_discount = (TextView) convertView.findViewById(R.id.txt_ver_discount);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Product product = productList.get(position);
        if (product.getImageList() != null && product.getImageList().size() > 0){
            Picasso.get().load(product.getImageList().get(0).getImageURL()).into(holder.img_ver_product);
        }
//        holder.img_ver_product.setImageResource(product.getImageList().get(0).getImageId());
        holder.txt_ver_productName.setText(product.getProductName());
        if (product.getSaleOff().getDiscount() > 0) {
            holder.txt_ver_productBasicPrice.setText(product.getPrice().toString());
            float basicPrice = Float.valueOf(product.getPrice());
            int discount = product.getSaleOff().getDiscount();
            float salePrice = basicPrice - (basicPrice * discount / 100);
            holder.txt_ver_productPrice.setText(String.format("%.3f", salePrice));
            holder.txt_ver_discount.setText(String.valueOf(product.getSaleOff().getDiscount()));
            convertView.findViewById(R.id.layout_ver_discount).setVisibility(View.VISIBLE);
        } else {
            holder.txt_ver_productPrice.setText(product.getPrice().toString());
            convertView.findViewById(R.id.layout_ver_discount).setVisibility(View.INVISIBLE);
        }


        return convertView;
    }

}