package com.n8plus.vhiep.cyberzone.ui.manage.mydeliveryaddress.deliveryaddress;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.n8plus.vhiep.cyberzone.R;
import com.n8plus.vhiep.cyberzone.ui.manage.mydeliveryaddress.MyDeliveryAddressPresenter;
import com.n8plus.vhiep.cyberzone.ui.manage.mydeliveryaddress.loaddeliveryaddress.LoadDeliveryAddressFragment;

public class DeliveryAddressFragment extends Fragment{
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.delivery_address_frag, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frm_delivery_address, new LoadDeliveryAddressFragment());
        fragmentTransaction.commit();

        super.onViewCreated(view, savedInstanceState);
    }
}
