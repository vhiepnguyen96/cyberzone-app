package com.n8plus.vhiep.cyberzone.ui.choosedeliveryaddress.adapter;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.n8plus.vhiep.cyberzone.data.model.Address;
import com.n8plus.vhiep.cyberzone.R;
import com.n8plus.vhiep.cyberzone.ui.manage.mydeliveryaddress.editdeliveryaddress.EditDeliveryAddressFragment;
import com.n8plus.vhiep.cyberzone.ui.manage.mydeliveryaddress.loaddeliveryaddress.LoadDeliveryAddressFragment;

import java.util.List;

public class DeliveryAddressAdapter extends BaseAdapter {

    private LoadDeliveryAddressFragment context;
    private int resource;
    private List<Address> arrAddress;

    public DeliveryAddressAdapter(LoadDeliveryAddressFragment context, int resource, List<Address> arrAddress) {
        this.context = context;
        this.resource = resource;
        this.arrAddress = arrAddress;
    }

    public class ViewHolder {
        TextView tv_name_customer, tv_phone_customer, tv_delivery_address;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(resource, null);
            viewHolder = new ViewHolder();
            viewHolder.tv_name_customer = (TextView) convertView.findViewById(R.id.tv_name_customer);
            viewHolder.tv_delivery_address = (TextView) convertView.findViewById(R.id.tv_delivery_address);
            viewHolder.img_edit_delivery_address = (ImageView) convertView.findViewById(R.id.img_edit_delivery_address);
            viewHolder.img_del_delivery_address = (ImageView) convertView.findViewById(R.id.img_del_delivery_address);
            viewHolder.tv_phone_customer = (TextView) convertView.findViewById(R.id.tv_phone_customer);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Address address = arrAddress.get(position);
        viewHolder.tv_name_customer.setText(address.getPresentation());
        viewHolder.tv_phone_customer.setText(address.getPhone());
        viewHolder.tv_delivery_address.setText(address.getAddress());

        viewHolder.img_edit_delivery_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (context instanceof LoadDeliveryAddressFragment) {
                    context.editDeliveryAddress(position);
                }
            }
        });

        viewHolder.img_del_delivery_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (context instanceof LoadDeliveryAddressFragment) {
                    context.deleteDeliveryAddress(position);
                }
            }
        });

        return convertView;
    }
}
