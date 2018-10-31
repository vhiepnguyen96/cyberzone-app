package com.n8plus.vhiep.cyberzone.ui.store;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.n8plus.vhiep.cyberzone.R;
import com.n8plus.vhiep.cyberzone.data.model.Store;
import com.n8plus.vhiep.cyberzone.ui.store.adapter.SectionsPageAdapter;
import com.n8plus.vhiep.cyberzone.ui.store.homepage.HomePageFragment;
import com.n8plus.vhiep.cyberzone.ui.store.information.InformationFragment;

public class StoreActivity extends AppCompatActivity implements StoreContract.View {
    private Toolbar mToolbarStore;
    private ViewPager mViewPager;
    private SectionsPageAdapter mSectionsPageAdapter;
    private TabLayout mTabLayout;
    private TextView mStoreName, mPositiveReview, mFollow, mFollowCounter;
    private StorePresenter mStorePresenter;
    private static int follow = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.store_act);

        mViewPager = (ViewPager) findViewById(R.id.pager_store);
        mTabLayout = (TabLayout) findViewById(R.id.tab_layout_store);
        mStoreName = (TextView) findViewById(R.id.tv_store_name);
        mPositiveReview = (TextView) findViewById(R.id.tv_positive_reviews);
        mFollow = (TextView) findViewById(R.id.tv_follow_store);
        mFollowCounter = (TextView) findViewById(R.id.tv_follow_counter);

        mStorePresenter = new StorePresenter(this);

        // Custom tab
        mSectionsPageAdapter = new SectionsPageAdapter(getSupportFragmentManager());

        HomePageFragment homePageFragment = new HomePageFragment();
        InformationFragment informationFragment = new InformationFragment();

        Intent intent = getIntent();
        if (intent != null && intent.getSerializableExtra("store") != null) {
            mStorePresenter.loadData((Store) intent.getSerializableExtra("store"));
            Bundle bundle = new Bundle();
            bundle.putSerializable("store", intent.getSerializableExtra("store"));

            homePageFragment.setArguments(bundle);
            informationFragment.setArguments(bundle);
        }

        mSectionsPageAdapter.adđFragment(homePageFragment, "Tất cả sản phẩm");
        mSectionsPageAdapter.adđFragment(informationFragment, "Thông tin gian hàng");

        mViewPager.setAdapter(mSectionsPageAdapter);
        mTabLayout.setupWithViewPager(mViewPager);

        // Listener
        mFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                follow++;
                if (follow % 2 == 1) {
                    mStorePresenter.followStore();
                } else {
                    mStorePresenter.unfollowStore();
                }
            }
        });
    }

    @Override
    public void setStoreName(String storeName) {
        mStoreName.setText(storeName);
    }

    @Override
    public void setPositiveReview(String positiveReview) {
        mPositiveReview.setText(positiveReview);
    }

    @Override
    public void setFollowStoreResult(boolean b) {
        mFollow.setBackgroundResource(b ? R.drawable.button_radius_gray : R.drawable.button_radius_red);
        mFollow.setText(b ? "Bỏ theo dõi" : "Theo dõi");
        mFollow.setTextColor(b ? Color.parseColor("#1B2326") : Color.parseColor("#ffffff"));
        follow = b ? 1 : 0;
    }

    @Override
    public void setFollowCounter(String followCounter) {
        mFollowCounter.setText(followCounter);
    }

    @Override
    public void followStoreResult(boolean b) {
        Toast.makeText(this, b ? "Đã theo dõi!" : "Vui lòng thử lại!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void unfollowStoreResult(boolean b) {
        Toast.makeText(this, b ? "Đã bỏ theo dõi!" : "Vui lòng thử lại!", Toast.LENGTH_SHORT).show();
    }
}
