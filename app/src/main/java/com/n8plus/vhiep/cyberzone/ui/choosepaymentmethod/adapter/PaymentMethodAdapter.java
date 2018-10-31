package com.n8plus.vhiep.cyberzone.ui.choosepaymentmethod.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.n8plus.vhiep.cyberzone.R;
import com.n8plus.vhiep.cyberzone.data.model.PaymentMethod;

import java.util.List;

public class PaymentMethodAdapter extends BaseAdapter {
    private Context context;
    private int resource;
    private List<PaymentMethod> paymentMethods;

    public PaymentMethodAdapter(Context context, int resource, List<PaymentMethod> paymentMethods) {
        this.context = context;
        this.resource = resource;
        this.paymentMethods = paymentMethods;
    }

    public class ViewHolder {
        public TextView name, description;
    }

    @Override
    public int getCount() {
        return paymentMethods.size();
    }

    @Override
    public Object getItem(int position) {
        return paymentMethods.get(position);
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
            holder.name = (TextView) convertView.findViewById(R.id.tv_payment_method_name);
            holder.description = (TextView) convertView.findViewById(R.id.tv_payment_method_description);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.name.setText(paymentMethods.get(position).getName());
        holder.description.setText(paymentMethods.get(position).getDescription());

        return convertView;
    }
}
