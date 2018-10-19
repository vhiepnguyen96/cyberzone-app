package com.n8plus.vhiep.cyberzone.ui.home;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
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
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.n8plus.vhiep.cyberzone.ui.product.adapter.RecyclerProductAdapter;
import com.n8plus.vhiep.cyberzone.util.Constant;
import com.n8plus.vhiep.cyberzone.util.ItemDecorationColumns;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements HomeContract.View {
    private RecyclerView mRecyclerSuggestion, mRecyclerOnSales, mRecyclerBestSeller, mRecyclerPopularCategory;
    private RecyclerProductAdapter mSuggestionAdapter, mOnSaleAdapter, mBestSellerAdapter;
    private RecyclerView.LayoutManager mLayoutSuggestion, mLayoutOnSale, mLayoutBestSeller, mLayoutPopularCategory;
    private PopularCategoryAdapter mPopularCategoryAdapter;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private FrameLayout mFrameProductCount;
    private ImageButton mAvatar, mCloseDrawer;
    private TextView mTextName, mMoreSuggestion, mMoreOnSale, mMoreBestSeller, mTextProductCount, mMorePopularCategory;
    private SearchView mSearchHome;
    private SwipeRefreshLayout mRefreshLayout;
    private NestedScrollView mScrollViewMain;
    private HomePresenter mHomePresenter;
    private DividerItemDecoration mOnSaleItemDecoration, mBestSellerItemDecoration;

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
        customDivider();
        mHomePresenter = new HomePresenter(this);

        // Load fragment
        fragmentManager = getSupportFragmentManager();
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

        // Custom refresh layout
        mRefreshLayout.setColorSchemeResources(R.color.colorPrimary);

        // Set data
        mHomePresenter.loadData();
        mHomePresenter.loadAllProductType();

        // Listener
        mCloseDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.closeDrawers();
            }
        });

        mMoreBestSeller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHomePresenter.prepareDataBestSeller();
            }
        });

        mMoreOnSale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHomePresenter.prepareDataOnSale();
            }
        });

        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mRefreshLayout.setRefreshing(true);
                mHomePresenter.loadData();
                mHomePresenter.loadAllProductType();
                mRefreshLayout.setRefreshing(false);
            }
        });

        mMoreSuggestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHomePresenter.prepareDataSuggestion();
            }
        });

        mMorePopularCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHomePresenter.refreshPopularCategory();
            }
        });

        mScrollViewMain.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    hideKeyboard(v);
                }
                return false;
            }
        });
    }

    @Override
    protected void onRestart() {
        setCartMenuItem();
        super.onRestart();
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
        mMoreSuggestion = (TextView) findViewById(R.id.txt_moreSuggestion);
        mMoreOnSale = (TextView) findViewById(R.id.txt_moreOnSale);
        mMoreBestSeller = (TextView) findViewById(R.id.txt_moreBestSeller);
        mMorePopularCategory = (TextView) findViewById(R.id.txt_morePopularCategory);
        mSearchHome = (SearchView) findViewById(R.id.search_home);
        mRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        mScrollViewMain = (NestedScrollView) findViewById(R.id.scroll_view_main);
        mRecyclerSuggestion = (RecyclerView) findViewById(R.id.recycler_suggestions);
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

    private void customDivider() {
        mRecyclerPopularCategory.addItemDecoration(new ItemDecorationColumns(4, 3));
        mOnSaleItemDecoration = new DividerItemDecoration(mRecyclerOnSales.getContext(), DividerItemDecoration.HORIZONTAL);
        mOnSaleItemDecoration.setDrawable(getResources().getDrawable(R.drawable.divider_horizontal));
        mBestSellerItemDecoration = new DividerItemDecoration(mRecyclerBestSeller.getContext(), DividerItemDecoration.HORIZONTAL);
        mBestSellerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.divider_horizontal));
        mRecyclerBestSeller.addItemDecoration(mBestSellerItemDecoration);
        mRecyclerOnSales.addItemDecoration(mOnSaleItemDecoration);
    }

    public void hideKeyboard(View view) {
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
        menu.findItem(R.id.mnu_wishlist).setVisible(false);
        menu.findItem(R.id.mnu_cart).setActionView(R.layout.cart_menu_item);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        final MenuItem cartMenuItem = menu.findItem(R.id.mnu_cart);
        RelativeLayout mRelativeCart = (RelativeLayout) cartMenuItem.getActionView();
        mFrameProductCount = (FrameLayout) mRelativeCart.findViewById(R.id.view_count_product);
        mTextProductCount = (TextView) mRelativeCart.findViewById(R.id.countProduct);

        setCartMenuItem();

        mRelativeCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, CartActivity.class));
            }
        });
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void setAdapterBestSeller(List<Product> productList) {
        mLayoutBestSeller = new LinearLayoutManager(mRecyclerBestSeller.getContext(), LinearLayoutManager.HORIZONTAL, false);
        mBestSellerAdapter = new RecyclerProductAdapter(R.layout.row_product_grid_layout, productList);
        mRecyclerBestSeller.setLayoutManager(mLayoutBestSeller);
        mRecyclerBestSeller.setAdapter(mBestSellerAdapter);
    }

    @Override
    public void setAdapterOnSale(List<Product> productList) {
        mLayoutOnSale = new LinearLayoutManager(mRecyclerOnSales.getContext(), LinearLayoutManager.HORIZONTAL, false);
        mOnSaleAdapter = new RecyclerProductAdapter(R.layout.row_product_grid_layout, productList);
        mRecyclerOnSales.setLayoutManager(mLayoutOnSale);
        mRecyclerOnSales.setAdapter(mOnSaleAdapter);
    }

    @Override
    public void setAdapterPopularCategory(List<ProductType> productTypeList) {
        mRecyclerPopularCategory.setHasFixedSize(true);
        mRecyclerPopularCategory.setNestedScrollingEnabled(false);
        mPopularCategoryAdapter = new PopularCategoryAdapter(productTypeList);
        mRecyclerPopularCategory.setLayoutManager(new GridLayoutManager(mRecyclerPopularCategory.getContext(), 3));
        mRecyclerPopularCategory.setAdapter(mPopularCategoryAdapter);
    }

    @Override
    public void setAdapterSuggestion(List<Product> productList) {
        mRecyclerSuggestion.setNestedScrollingEnabled(false);
        mSuggestionAdapter = new RecyclerProductAdapter(R.layout.row_product_grid_layout, productList);
        mRecyclerSuggestion.setLayoutManager(new GridLayoutManager(mRecyclerSuggestion.getContext(), 2));
        mRecyclerSuggestion.setAdapter(mSuggestionAdapter);
        mRecyclerSuggestion.addItemDecoration(new ItemDecorationColumns(10, 2));
    }

    @Override
    public void setCartMenuItem() {
        mFrameProductCount.setVisibility(Constant.countProductInCart() == 0 ? View.INVISIBLE : View.VISIBLE);
        mTextProductCount.setText(String.valueOf(Constant.countProductInCart()));
    }

    @Override
    public void setNotifyDataSetChanged(String adapter) {
        if (adapter.contains("BestSeller")) {
            mBestSellerAdapter.notifyDataSetChanged();
        } else if (adapter.contains("OnSale")) {
            mOnSaleAdapter.notifyDataSetChanged();
        } else {
            mSuggestionAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void popularCategoryItemSelected(String productTypeId) {
        mHomePresenter.prepareDataProductType(productTypeId);
    }

    @Override
    public void moveToProductActivity(List<Product> products) {
        Intent intent = new Intent(HomeActivity.this, ProductActivity.class);
        if (products.size() > 0) {
            intent.putExtra("products", (Serializable) products);
        }
        startActivity(intent);
    }

    @Override
    public void moveToProductActivity(String productTypeId) {
        Intent intent = new Intent(HomeActivity.this, ProductActivity.class);
        intent.putExtra("productTypeId", productTypeId);
        startActivity(intent);
    }
}

