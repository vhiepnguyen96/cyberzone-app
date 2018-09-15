package com.n8plus.vhiep.cyberzone.ui.store;


import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.n8plus.vhiep.cyberzone.R;
import com.n8plus.vhiep.cyberzone.ui.store.adapter.SectionsPageAdapter;
import com.n8plus.vhiep.cyberzone.ui.store.homepage.HomePageFragment;
import com.n8plus.vhiep.cyberzone.ui.store.information.InformationFragment;

public class StoreActivity extends AppCompatActivity {
    private Toolbar mToolbarStore;
    private ViewPager mViewPager;
    private SectionsPageAdapter mSectionsPageAdapter;
    private TabLayout mTabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.store_act);

        mViewPager = (ViewPager) findViewById(R.id.pager_store);
        mTabLayout = (TabLayout) findViewById(R.id.tab_layout_store);

        // Custom tab
        mSectionsPageAdapter = new SectionsPageAdapter(getSupportFragmentManager());
        setupViewPager(mViewPager);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    private void setupViewPager(ViewPager viewPager){
        SectionsPageAdapter adapter = new SectionsPageAdapter(getSupportFragmentManager());
        adapter.adđFragment(new HomePageFragment(), "Tất cả sản phẩm");
        adapter.adđFragment(new InformationFragment(), "Thông tin gian hàng");
        viewPager.setAdapter(adapter);
    }
}
