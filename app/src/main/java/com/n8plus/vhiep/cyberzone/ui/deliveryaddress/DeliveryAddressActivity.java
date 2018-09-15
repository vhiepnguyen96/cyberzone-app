package com.n8plus.vhiep.cyberzone.ui.deliveryaddress;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.n8plus.vhiep.cyberzone.ui.deliveryaddress.loaddeliveryaddress.LoadDeliveryAddressFragment;
import com.n8plus.vhiep.cyberzone.R;

public class DeliveryAddressActivity extends AppCompatActivity {
    public FragmentManager fragmentManagerDelivery;
    public FragmentTransaction fragmentTransactionDelivery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delivery_address_act);

        fragmentManagerDelivery = getFragmentManager();
        fragmentTransactionDelivery = fragmentManagerDelivery.beginTransaction();
        fragmentTransactionDelivery.add(R.id.frm_delivery_address, new LoadDeliveryAddressFragment());
        fragmentTransactionDelivery.commit();
    }
}
