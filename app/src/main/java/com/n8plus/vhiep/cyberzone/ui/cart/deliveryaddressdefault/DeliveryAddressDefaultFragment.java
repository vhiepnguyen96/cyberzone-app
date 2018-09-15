package com.n8plus.vhiep.cyberzone.ui.cart.deliveryaddressdefault;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.n8plus.vhiep.cyberzone.ui.deliveryaddress.DeliveryAddressActivity;
import com.n8plus.vhiep.cyberzone.R;

public class DeliveryAddressDefaultFragment extends Fragment implements DeliveryAddressDefaultContract.View, View.OnClickListener {
    private TextView mEditAddress, mNameCustomer, mPhoneCustomer, mAddressCustomer;
    private DeliveryAddressDefaultPresenter mAddressDefaultPresenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.cart_delivery_address_frag, container, false);
        mAddressDefaultPresenter = new DeliveryAddressDefaultPresenter(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mEditAddress = (TextView) view.findViewById(R.id.tv_edit_address);
        mNameCustomer = (TextView) view.findViewById(R.id.tv_name_customer_default);
        mPhoneCustomer = (TextView) view.findViewById(R.id.tv_phone_customer_default);
        mAddressCustomer = (TextView) view.findViewById(R.id.tv_address_customer_default);

        // Presenter
        mAddressDefaultPresenter.loadData();

        // Listener
        mEditAddress.setOnClickListener(this);

        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_edit_address:
                startActivity(new Intent(v.getContext(), DeliveryAddressActivity.class));
                break;
        }
    }

    @Override
    public void setNameCustomer(String name) {
        mNameCustomer.setText(name);
    }

    @Override
    public void setPhoneCustomer(String phone) {
        mPhoneCustomer.setText(phone);
    }

    @Override
    public void setAddressCustomer(String address) {
        mAddressCustomer.setText(address);
    }
}

