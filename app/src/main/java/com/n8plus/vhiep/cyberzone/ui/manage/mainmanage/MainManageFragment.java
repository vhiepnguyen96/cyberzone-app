package com.n8plus.vhiep.cyberzone.ui.manage.mainmanage;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.n8plus.vhiep.cyberzone.R;
import com.n8plus.vhiep.cyberzone.ui.manage.mydeliveryaddress.MyDeliveryAddressFragment;
import com.n8plus.vhiep.cyberzone.ui.manage.myorders.MyOrderFragment;
import com.n8plus.vhiep.cyberzone.ui.manage.myprofile.MyProfileFragment;
import com.n8plus.vhiep.cyberzone.ui.manage.myreview.MyReviewFragment;
import com.n8plus.vhiep.cyberzone.ui.manage.registersale.RegisterSaleFragment;
import com.n8plus.vhiep.cyberzone.ui.manage.wishlistfollowedstore.WishlistFollowedstoreFragment;

public class MainManageFragment extends Fragment implements MainManageContract.View {
    private TextView mHelloCustomer;
    private LinearLayout mMyProfile, mMyDeliveryAddress, mMyOrder, mWishListFollowedStore, mRegisterSale, mMyReview;
    private FragmentManager mFragmentManager;
    private FragmentTransaction mFragmentTransaction;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_manage_frag, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mHelloCustomer = (TextView) view.findViewById(R.id.tv_hello_customer);
        mMyProfile = (LinearLayout) view.findViewById(R.id.lnr_my_profile);
        mMyDeliveryAddress = (LinearLayout) view.findViewById(R.id.lnr_my_delivery);
        mMyOrder = (LinearLayout) view.findViewById(R.id.lnr_my_order);
        mMyReview = (LinearLayout) view.findViewById(R.id.lnr_my_review);
        mWishListFollowedStore = (LinearLayout) view.findViewById(R.id.lnr_my_wishlist_followedstore);
        mRegisterSale = (LinearLayout) view.findViewById(R.id.lnr_register_sale);

        mMyProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFragmentManager = getFragmentManager();
                mFragmentTransaction = mFragmentManager.beginTransaction();
                mFragmentTransaction.replace(R.id.frm_manage, new MyProfileFragment());
                mFragmentTransaction.commit();
            }
        });
        mMyDeliveryAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFragmentManager = getFragmentManager();
                mFragmentTransaction = mFragmentManager.beginTransaction();
                mFragmentTransaction.replace(R.id.frm_manage, new MyDeliveryAddressFragment());
                mFragmentTransaction.commit();
            }
        });
        mMyOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFragmentManager = getFragmentManager();
                mFragmentTransaction = mFragmentManager.beginTransaction();
                mFragmentTransaction.replace(R.id.frm_manage, new MyOrderFragment());
                mFragmentTransaction.commit();
            }
        });

        mMyReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFragmentManager = getFragmentManager();
                mFragmentTransaction = mFragmentManager.beginTransaction();
                mFragmentTransaction.replace(R.id.frm_manage, new MyReviewFragment());
                mFragmentTransaction.commit();
            }
        });
        mWishListFollowedStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFragmentManager = getFragmentManager();
                mFragmentTransaction = mFragmentManager.beginTransaction();
                mFragmentTransaction.replace(R.id.frm_manage, new WishlistFollowedstoreFragment());
                mFragmentTransaction.commit();
            }
        });
        mRegisterSale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFragmentManager = getFragmentManager();
                mFragmentTransaction = mFragmentManager.beginTransaction();
                mFragmentTransaction.replace(R.id.frm_manage, new RegisterSaleFragment());
                mFragmentTransaction.commit();
            }
        });
        super.onViewCreated(view, savedInstanceState);
    }
}
