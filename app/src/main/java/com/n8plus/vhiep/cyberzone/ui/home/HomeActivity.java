package com.n8plus.vhiep.cyberzone.ui.home;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.TextView;

import com.n8plus.vhiep.cyberzone.R;
import com.n8plus.vhiep.cyberzone.ui.home.adapter.PopularCategoryAdapter;
import com.n8plus.vhiep.cyberzone.ui.home.adapter.ProductHorizontalAdapter;
import com.n8plus.vhiep.cyberzone.ui.cart.CartActivity;
import com.n8plus.vhiep.cyberzone.ui.manage.ManageActivity;
import com.n8plus.vhiep.cyberzone.ui.product.ProductActivity;
import com.n8plus.vhiep.cyberzone.ui.home.navigation.category.CategoryFragment;
import com.n8plus.vhiep.cyberzone.data.model.Overview;
import com.n8plus.vhiep.cyberzone.data.model.Product;
import com.n8plus.vhiep.cyberzone.data.model.ProductType;
import com.n8plus.vhiep.cyberzone.data.model.Specification;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements HomeContract.View{
    private RecyclerView mRecyclerNewArrivals, mRecyclerOnSales, mRecyclerBestSeller, mRecyclerPopularCategory;
    private ProductHorizontalAdapter mOnSaleAdapter, mNewArrivalAdapter, mBestSellerAdapter;
    private RecyclerView.LayoutManager mLayoutNewArrival, mLayoutOnSale, mLayoutBestSeller, mLayoutPopularCategory;
    private PopularCategoryAdapter mPopularCategoryAdapter;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private ImageButton mAvatar, mCloseDrawer;
    private TextView mTextName, mMoreNewArrival, mMoreOnSale, mMoreBestSeller;
    private SearchView mSearchHome;
    private NestedScrollView mScrollViewMain;
    private HomePresenter mHomePresenter;

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_act);
        setBackgroundStatusBar();
        customToolbar();

        initViews();
        setFullScreenNavigationView();
        mHomePresenter = new HomePresenter(this);

        // Load fragment
        fragmentManager = getFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, new CategoryFragment());
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

        // Custom SearchView
        SearchView.SearchAutoComplete serAutoComplete = (SearchView.SearchAutoComplete) mSearchHome.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        serAutoComplete.setHintTextColor(Color.parseColor("#c1c1c1"));
        serAutoComplete.setTextColor(Color.parseColor("#000000"));
        mSearchHome.setFocusable(false);
        mSearchHome.clearFocus();

        // Set data
        mHomePresenter.loadData();

        // Listener
        mCloseDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.closeDrawers();
            }
        });

        mMoreNewArrival.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, ProductActivity.class));
            }
        });

        mScrollViewMain.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_MOVE){
                    hideKeyboard(v);
                }
                return false;
            }
        });
    }

    public void setBackgroundStatusBar() {
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.parseColor("#008194"));
    }

    private void initViews() {
        mDrawerLayout = findViewById(R.id.drawer_layout);
        mNavigationView = findViewById(R.id.nav_view);
        mAvatar = (ImageButton) findViewById(R.id.ibn_avatar);
        mCloseDrawer = (ImageButton) findViewById(R.id.ibn_close);
        mTextName = (TextView) findViewById(R.id.txt_name);
        mMoreNewArrival = (TextView) findViewById(R.id.txt_moreNewArrival);
        mMoreOnSale = (TextView) findViewById(R.id.txt_moreOnSale);
        mMoreBestSeller = (TextView) findViewById(R.id.txt_moreBestSeller);
        mSearchHome = (SearchView) findViewById(R.id.search_home);
        mScrollViewMain = (NestedScrollView) findViewById(R.id.scroll_view_main);
        mRecyclerNewArrivals = (RecyclerView) findViewById(R.id.recycler_new_arrivals);
        mRecyclerOnSales = (RecyclerView) findViewById(R.id.recycler_on_sales);
        mRecyclerBestSeller = (RecyclerView) findViewById(R.id.recycler_best_seller);
        mRecyclerPopularCategory = (RecyclerView) findViewById(R.id.recycler_popular_category);
    }

    private void setFullScreenNavigationView() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        DrawerLayout.LayoutParams params = (DrawerLayout.LayoutParams) mNavigationView.getLayoutParams();
        params.width = metrics.widthPixels;
        mNavigationView.setLayoutParams(params);
    }

    private void customToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar_home);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.menu_white);
        actionbar.setDisplayShowTitleEnabled(false);
    }



    public void hideKeyboard(View view){
        InputMethodManager inputManager =
                (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(
                mSearchHome.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                break;
            case R.id.mnu_cart:
                startActivity(new Intent(HomeActivity.this, CartActivity.class));
                break;
            case R.id.mnu_account:
                startActivity(new Intent(HomeActivity.this, ManageActivity.class));
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_bar, menu);
        menu.findItem(R.id.mnu_search).setVisible(false);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void setAdapterNewArrival(List<Product> productList) {
        mLayoutNewArrival =  new LinearLayoutManager(mRecyclerNewArrivals.getContext(), LinearLayoutManager.HORIZONTAL, false);
        mNewArrivalAdapter = new ProductHorizontalAdapter(productList);
        mRecyclerNewArrivals.setLayoutManager(mLayoutNewArrival);
        mRecyclerNewArrivals.setAdapter(mNewArrivalAdapter);
    }

    @Override
    public void setAdapterOnSale(List<Product> productList) {
        mLayoutOnSale =  new LinearLayoutManager(mRecyclerOnSales.getContext(), LinearLayoutManager.HORIZONTAL, false);
        mOnSaleAdapter = new ProductHorizontalAdapter(productList);
        mRecyclerOnSales.setLayoutManager(mLayoutOnSale);
        mRecyclerOnSales.setAdapter(mOnSaleAdapter);
    }

    @Override
    public void setAdapterBestSeller(List<Product> productList) {
        mLayoutBestSeller =  new LinearLayoutManager(mRecyclerBestSeller.getContext(), LinearLayoutManager.HORIZONTAL, false);
        mBestSellerAdapter = new ProductHorizontalAdapter(productList);
        mRecyclerBestSeller.setLayoutManager(mLayoutBestSeller);
        mRecyclerBestSeller.setAdapter(mBestSellerAdapter);
    }

    @Override
    public void setAdapterPopularCategory(List<ProductType> productTypeList) {
        mLayoutPopularCategory =  new LinearLayoutManager(mRecyclerPopularCategory.getContext(), LinearLayoutManager.HORIZONTAL, false);
        mPopularCategoryAdapter = new PopularCategoryAdapter(productTypeList);
        mRecyclerPopularCategory.setLayoutManager(mLayoutPopularCategory);
        mRecyclerPopularCategory.setAdapter(mPopularCategoryAdapter);
    }
}

