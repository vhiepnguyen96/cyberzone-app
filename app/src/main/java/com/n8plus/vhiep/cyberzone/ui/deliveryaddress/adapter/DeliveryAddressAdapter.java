package com.n8plus.vhiep.cyberzone.ui.deliveryaddress.adapter;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.n8plus.vhiep.cyberzone.data.model.Address;
import com.n8plus.vhiep.cyberzone.R;
import com.n8plus.vhiep.cyberzone.ui.deliveryaddress.editdeliveryaddress.EditDeliveryAddressFragment;

import java.util.List;

public class DeliveryAddressAdapter extends BaseAdapter {

    private Context context;
    private int resource;
    private List<Address> arrAddress;

    public DeliveryAddressAdapter(Context context, int resource, List<Address> arrAddress) {
        this.context = context;
        this.resource = resource;
        this.arrAddress = arrAddress;
    }

    public class ViewHolder {
        TextView tv_name_customer, tv_delivery_address;
        ImageView img_edit_delivery_address, img_del_delivery_address;
    }

    @Override
    public int getCount() {
        return arrAddress.size();
    }

    @Override
    public Object getItem(int position) {
        return arrAddress.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(resource, null);
            viewHolder = new ViewHolder();
            viewHolder.tv_name_customer = (TextView) convertView.findViewById(R.id.tv_name_customer);
            viewHolder.tv_delivery_address = (TextView) convertView.findViewById(R.id.tv_delivery_address);
            viewHolder.img_edit_delivery_address = (ImageView) convertView.findViewById(R.id.img_edit_delivery_address);
            viewHolder.img_del_delivery_address = (ImageView) convertView.findViewById(R.id.img_del_delivery_address);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Address address = arrAddress.get(position);
        viewHolder.tv_name_customer.setText(address.getNameCustomer());
        viewHolder.tv_delivery_address.setText(address.getAddress());

        viewHolder.img_edit_delivery_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = ((Activity) context).getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frm_delivery_address, new EditDeliveryAddressFragment());
                fragmentTransaction.commit();
            }
        });

        return convertView;
    }
}
