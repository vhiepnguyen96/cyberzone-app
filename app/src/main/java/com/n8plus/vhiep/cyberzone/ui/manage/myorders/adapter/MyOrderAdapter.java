package com.n8plus.vhiep.cyberzone.ui.manage.myorders.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.n8plus.vhiep.cyberzone.R;
import com.n8plus.vhiep.cyberzone.data.model.Order;
import com.n8plus.vhiep.cyberzone.ui.product.adapter.ProductVerticalAdapter;
import com.n8plus.vhiep.cyberzone.util.UIUtils;

import java.util.List;

public class MyOrderAdapter extends BaseAdapter {
    private Context context;
    private int resource;
    private List<Order> orderList;

    private ProductOrderAdapter mProductOrderAdapter;

    public MyOrderAdapter(Context context, int resource, List<Order> orderList) {
        this.context = context;
        this.resource = resource;
        this.orderList = orderList;
    }

    public class ViewHolder {
        public TextView id, purchase_date, total_quantity, total_price, state;
        public ListView purchaseList;
        public Button btn_payment_order;
    }

    @Override
    public int getCount() {
        return orderList.size();
    }

    @Override
    public Object getItem(int position) {
        return orderList.get(position);
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
            holder.id = (TextView) convertView.findViewById(R.id.tv_order_id);
            holder.purchase_date = (TextView) convertView.findViewById(R.id.tv_order_purchase_date);
            holder.total_quantity = (TextView) convertView.findViewById(R.id.tv_order_total_quantity);
            holder.total_price = (TextView) convertView.findViewById(R.id.tv_order_total_price);
            holder.state = (TextView) convertView.findViewById(R.id.tv_order_state);
            holder.purchaseList = (ListView) convertView.findViewById(R.id.lv_purchaseProduct);
            holder.btn_payment_order = (Button) convertView.findViewById(R.id.btn_payment_order);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Order order = orderList.get(position);
        holder.id.setText(order.getOrderId());
        holder.purchase_date.setText(order.getPurchaseDate().toString());
        holder.total_quantity.setText(String.valueOf(order.getTotalQuantity()));
        holder.total_price.setText(String.valueOf(order.getTotalPrice()));
        holder.state.setText(order.getOrderState());

        // List product
        mProductOrderAdapter = new ProductOrderAdapter(holder.purchaseList.getContext(), R.layout.row_product_order, order.getPurchaseList());
        holder.purchaseList.setAdapter(mProductOrderAdapter);
        UIUtils.setListViewHeightBasedOnItems(holder.purchaseList);

        if (order.getOrderState().equals("Đang chờ thanh toán")){
            holder.btn_payment_order.setVisibility(View.VISIBLE);
        } else {
            holder.btn_payment_order.setVisibility(View.INVISIBLE);
        }

        holder.btn_payment_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "Thanh toán", Toast.LENGTH_SHORT).show();
            }
        });

        return convertView;
    }
}
