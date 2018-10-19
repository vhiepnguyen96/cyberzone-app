package com.n8plus.vhiep.cyberzone.ui.manage.myreview.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.n8plus.vhiep.cyberzone.R;
import com.n8plus.vhiep.cyberzone.data.model.Order;
import com.n8plus.vhiep.cyberzone.data.model.Product;
import com.n8plus.vhiep.cyberzone.data.model.PurchaseItem;
import com.n8plus.vhiep.cyberzone.ui.manage.myorders.adapter.MyOrderAdapter;
import com.n8plus.vhiep.cyberzone.ui.manage.myreview.notwritereview.writereview.WriteReviewFragment;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.util.List;

public class ProductPurchaseAdapter extends BaseAdapter {
    private Context context;
    private int resource;
    private List<Product> productList;

    public ProductPurchaseAdapter(Context context, int resource, List<Product> productList) {
        this.context = context;
        this.resource = resource;
        this.productList = productList;
    }

    public class ViewHolder {
        public TextView store_name, date_purchase, product_name;
        public ImageView image_product;
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
            holder.store_name = (TextView) convertView.findViewById(R.id.tv_store_name_purchase);
            holder.date_purchase = (TextView) convertView.findViewById(R.id.tv_date_purchase);
            holder.product_name = (TextView) convertView.findViewById(R.id.tv_product_name_purchase);
            holder.image_product = (ImageView) convertView.findViewById(R.id.img_product_purchase);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Product product = productList.get(position);
        holder.store_name.setText(product.getStore().getStoreName());
//        holder.date_purchase.setText(DateFormat.getDateInstance(DateFormat.SHORT).format(order.getPurchaseDate()));
        if (product.getImageList() != null && product.getImageList().size() > 0){
            Picasso.get().load(product.getImageList().get(0).getImageURL()).into(holder.image_product);
        }
//        holder.image_product.setImageResource(product.getImageList().get(0).getImageId());
        holder.product_name.setText(product.getProductName());

        convertView.findViewById(R.id.write_review).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction = ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frm_not_write_review, new WriteReviewFragment());
                fragmentTransaction.commit();
            }
        });

        return convertView;
    }
}
