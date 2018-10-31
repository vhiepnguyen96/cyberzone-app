package com.n8plus.vhiep.cyberzone.ui.manage.myorders;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.n8plus.vhiep.cyberzone.R;
import com.n8plus.vhiep.cyberzone.data.model.Order;
import com.n8plus.vhiep.cyberzone.ui.manage.mainmanage.MainManageFragment;
import com.n8plus.vhiep.cyberzone.ui.manage.myorders.allorder.AllOrderFragment;
import com.n8plus.vhiep.cyberzone.ui.manage.myorders.waitforpayment.WaitForPaymentFragment;
import com.n8plus.vhiep.cyberzone.ui.manage.myprofile.MyProfileContract;
import com.n8plus.vhiep.cyberzone.ui.store.adapter.SectionsPageAdapter;
import com.n8plus.vhiep.cyberzone.ui.store.homepage.HomePageFragment;
import com.n8plus.vhiep.cyberzone.ui.store.information.InformationFragment;
import com.n8plus.vhiep.cyberzone.util.Constant;

import java.util.List;

public class MyOrderFragment extends Fragment implements MyOrderContract.View {
    private Toolbar mToolbar;
    private TabLayout mTabOrder;
    private ViewPager mViewPager;
    private SectionsPageAdapter mSectionsPageAdapter;
    private MyOrderPresenter mMyOrderPresenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.my_order_frag, container, false);
        mMyOrderPresenter = new MyOrderPresenter(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mToolbar = (Toolbar) view.findViewById(R.id.toolbar_my_order);
        mViewPager = (ViewPager) view.findViewById(R.id.pager_order);
        mTabOrder = (TabLayout) view.findViewById(R.id.tabs_order);
        setHasOptionsMenu(true);

        // Custom tab
        mSectionsPageAdapter = new SectionsPageAdapter(getActivity().getSupportFragmentManager());
        setupViewPager(mViewPager);
        mTabOrder.setupWithViewPager(mViewPager);

        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
        ActionBar actionbar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
        actionbar.setTitle("Quản lý đơn hàng");

        super.onViewCreated(view, savedInstanceState);
    }

    private void setupViewPager(ViewPager viewPager){
        SectionsPageAdapter adapter = new SectionsPageAdapter(getActivity().getSupportFragmentManager());
        adapter.adđFragment(new AllOrderFragment(), "Tất cả");
        adapter.adđFragment(new WaitForPaymentFragment(), "Chờ thanh toán");
        viewPager.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frm_manage, new MainManageFragment());
                fragmentTransaction.commit();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
