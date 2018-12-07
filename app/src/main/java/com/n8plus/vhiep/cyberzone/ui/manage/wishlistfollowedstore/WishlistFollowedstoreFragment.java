package com.n8plus.vhiep.cyberzone.ui.manage.wishlistfollowedstore;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.n8plus.vhiep.cyberzone.ui.manage.mainmanage.MainManageFragment;
import com.n8plus.vhiep.cyberzone.ui.manage.myprofile.MyProfileContract;
import com.n8plus.vhiep.cyberzone.ui.manage.wishlistfollowedstore.followedstore.FollowedStoreFragment;
import com.n8plus.vhiep.cyberzone.ui.manage.wishlistfollowedstore.wishlist.WishListFragment;
import com.n8plus.vhiep.cyberzone.ui.store.adapter.SectionsPageAdapter;

public class WishlistFollowedstoreFragment extends Fragment implements WishlistFollowedstoreContract.View{
    private Toolbar mToolbar;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private SectionsPageAdapter mSectionsPageAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.my_wishlist_and_followedstore_frag, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mToolbar = (Toolbar) view.findViewById(R.id.toolbar_wislist_followstore);
        mTabLayout = (TabLayout) view.findViewById(R.id.tabs_wishlist_followedstore);
        mViewPager = (ViewPager) view.findViewById(R.id.pager_wishlist_followedstore);
        setHasOptionsMenu(true);
        setupViewPager(mViewPager);
        customToolbar();

        // Custom tab
        mSectionsPageAdapter = new SectionsPageAdapter(getActivity().getSupportFragmentManager());
        setupViewPager(mViewPager);
        mTabLayout.setupWithViewPager(mViewPager);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mViewPager.getAdapter().notifyDataSetChanged();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        super.onViewCreated(view, savedInstanceState);
    }

    private void setupViewPager(ViewPager viewPager){
        SectionsPageAdapter adapter = new SectionsPageAdapter(getActivity().getSupportFragmentManager());
        adapter.adđFragment(new WishListFragment(), "Sản phẩm yêu thích");
        adapter.adđFragment(new FollowedStoreFragment(), "Gian hàng theo dõi");
        viewPager.setAdapter(adapter);
    }

    private void customToolbar(){
        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
        ActionBar actionbar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
        actionbar.setTitle("Sản phẩm & gian hàng");
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
