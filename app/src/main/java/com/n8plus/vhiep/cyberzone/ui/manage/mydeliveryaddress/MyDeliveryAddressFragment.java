package com.n8plus.vhiep.cyberzone.ui.manage.mydeliveryaddress;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.n8plus.vhiep.cyberzone.R;
import com.n8plus.vhiep.cyberzone.data.model.Address;
import com.n8plus.vhiep.cyberzone.ui.choosedeliveryaddress.adapter.DeliveryAddressAdapter;
import com.n8plus.vhiep.cyberzone.ui.manage.mainmanage.MainManageFragment;
import com.n8plus.vhiep.cyberzone.ui.manage.mydeliveryaddress.deliveryaddress.DeliveryAddressFragment;
import com.n8plus.vhiep.cyberzone.ui.manage.mydeliveryaddress.loaddeliveryaddress.LoadDeliveryAddressFragment;

import java.util.List;

public class MyDeliveryAddressFragment extends Fragment implements MyDeliveryAddressContract.View {
    private Toolbar mToolbar;
    private ListView mListDeliveryAddress;
    private Button mAddAddress;
    private DeliveryAddressAdapter mDeliveryAddressAdapter;
    private MyDeliveryAddressPresenter myDeliveryAddressPresenter;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.my_delivery_address_frag, container, false);
        myDeliveryAddressPresenter = new MyDeliveryAddressPresenter(getContext(),this);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mToolbar = (Toolbar) view.findViewById(R.id.toolbar_delivery_address);
        setHasOptionsMenu(true);

        // Custom
        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
        ActionBar actionbar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
        actionbar.setTitle("Quản lý địa chỉ giao hàng");

        // Presenter
        myDeliveryAddressPresenter.loadData();

        fragmentManager = getFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frm_my_delivery_address, new DeliveryAddressFragment());
        fragmentTransaction.commit();

        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                ActionBar actionbar = ((AppCompatActivity) getActivity()).getSupportActionBar();
                String title = (String) actionbar.getTitle();
                if (title.equals("Quản lý địa chỉ giao hàng")){
                    fragmentTransaction.replace(R.id.frm_manage, new MainManageFragment());
                }
                fragmentTransaction.commit();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
