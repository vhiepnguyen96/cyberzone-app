package com.n8plus.vhiep.cyberzone.ui.manage.myreview;

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

import com.n8plus.vhiep.cyberzone.R;
import com.n8plus.vhiep.cyberzone.ui.manage.mainmanage.MainManageFragment;
import com.n8plus.vhiep.cyberzone.ui.manage.myorders.MyOrderContract;
import com.n8plus.vhiep.cyberzone.ui.manage.myorders.allorder.AllOrderFragment;
import com.n8plus.vhiep.cyberzone.ui.manage.myorders.waitforpayment.WaitForPaymentFragment;
import com.n8plus.vhiep.cyberzone.ui.manage.myreview.historyreview.HistoryReviewFragment;
import com.n8plus.vhiep.cyberzone.ui.manage.myreview.notwritereview.NotWriteReviewFragment;
import com.n8plus.vhiep.cyberzone.ui.store.adapter.SectionsPageAdapter;

public class MyReviewFragment extends Fragment implements MyOrderContract.View {
    private Toolbar mToolbar;
    private TabLayout mTabReview;
    private ViewPager mViewPager;
    private SectionsPageAdapter mSectionsPageAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.my_review_frag, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mToolbar = (Toolbar) view.findViewById(R.id.toolbar_my_review);
        mViewPager = (ViewPager) view.findViewById(R.id.pager_review);
        mTabReview = (TabLayout) view.findViewById(R.id.tabs_review);
        setHasOptionsMenu(true);

        // Custom tab
        mSectionsPageAdapter = new SectionsPageAdapter(getActivity().getSupportFragmentManager());
        setupViewPager(mViewPager);
        mTabReview.setupWithViewPager(mViewPager);

        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
        ActionBar actionbar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
        actionbar.setTitle("Đánh giá của tôi");

        super.onViewCreated(view, savedInstanceState);
    }

    private void setupViewPager(ViewPager viewPager){
        SectionsPageAdapter adapter = new SectionsPageAdapter(getActivity().getSupportFragmentManager());
        adapter.adđFragment(new NotWriteReviewFragment(), "Chưa viết đánh giá");
        adapter.adđFragment(new HistoryReviewFragment(), "Lịch sử đánh giá");
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
