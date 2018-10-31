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
import com.n8plus.vhiep.cyberzone.ui.cart.CartActivity;
import com.n8plus.vhiep.cyberzone.ui.manage.myorders.adapter.MyOrderAdapter;
import com.n8plus.vhiep.cyberzone.ui.manage.myorders.allorder.AllOrderFragment;
import com.n8plus.vhiep.cyberzone.ui.manage.myreview.notwritereview.allpurchase.AllPurchaseFragment;
import com.n8plus.vhiep.cyberzone.ui.manage.myreview.notwritereview.writereview.WriteReviewFragment;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ProductPurchaseAdapter extends BaseAdapter {
    private AllPurchaseFragment context;
    private int resource;
    private List<PurchaseItem> purchaseItemList;
    private List<Date> datePurchaseList;

    public ProductPurchaseAdapter(AllPurchaseFragment context, int resource, List<PurchaseItem> purchaseItemList, List<Date> datePurchaseList) {
        this.context = context;
        this.resource = resource;
        this.purchaseItemList = purchaseItemList;
        this.datePurchaseList = datePurchaseList;
    }

    public class ViewHolder {
        public TextView store_name, date_purchase, product_name;
        public ImageView image_product;
    }

    @Override
    public int getCount() {
        return purchaseItemList.size();
    }

    @Override
    public Object getItem(int position) {
        return purchaseItemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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

        Product product = purchaseItemList.get(position).getProduct();
        if (product.getStore() != null){
            holder.store_name.setText(product.getStore().getStoreName());
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(datePurchaseList.get(position));
        String date = "ngày "+calendar.get(Calendar.DATE) + " Thg " + (calendar.get(Calendar.MONTH) + 1) + " " + calendar.get(Calendar.YEAR);
        holder.date_purchase.setText(date);

        if (product.getImageDefault() != null){
            Picasso.get().load(product.getImageDefault()).into(holder.image_product);
        }
        holder.product_name.setText(product.getProductName());

        convertView.findViewById(R.id.write_review).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (context instanceof AllPurchaseFragment) {
                    ((AllPurchaseFragment) context).writeReview(position);
                }
            }
        });

        return convertView;
    }
}
