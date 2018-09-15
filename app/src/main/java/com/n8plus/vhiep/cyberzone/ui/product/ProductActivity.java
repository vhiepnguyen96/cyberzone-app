package com.n8plus.vhiep.cyberzone.ui.product;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.graphics.Color;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;

import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager;
import com.bignerdranch.expandablerecyclerview.Model.ParentObject;
import com.n8plus.vhiep.cyberzone.ui.product.adapter.FilterExpandableAdapter;
import com.n8plus.vhiep.cyberzone.ui.product.adapter.ProductVerticalAdapter;
import com.n8plus.vhiep.cyberzone.ui.productdetails.ProductDetailActivity;
import com.n8plus.vhiep.cyberzone.data.model.Filter;
import com.n8plus.vhiep.cyberzone.data.model.FilterChild;
import com.n8plus.vhiep.cyberzone.data.model.Overview;
import com.n8plus.vhiep.cyberzone.data.model.Product;
import com.n8plus.vhiep.cyberzone.data.model.Specification;
import com.n8plus.vhiep.cyberzone.R;
import com.n8plus.vhiep.cyberzone.ui.home.HomeActivity;

import java.util.ArrayList;
import java.util.List;

import static android.support.v4.util.Preconditions.checkNotNull;

public class ProductActivity extends AppCompatActivity implements ProductContract.View{
    private Toolbar mToolbarShop;
    private NavigationView mNavigationFilter;
    private DrawerLayout mDrawerLayoutShop;
    private LinearLayout mLinearFilter;
    private SearchView mSearchProduct;
    private ImageView mImageBackHome, mImageFilter;
    private Spinner mSpinnerSort;
    private ListView mListViewProduct;
    private RecyclerView mRecyclerFilter;
    private ProductVerticalAdapter mProductVerticalAdapter;
    private ProductPresenter mProductPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        setContentView(R.layout.products_act);
        initView();

        // Custom view
        setBackgroundStatusBar();
        customToolbar();
        customSpinnerFilter();
        customSearchView();

        // Presenter
        mProductPresenter = new ProductPresenter(this);
        mProductPresenter.loadData();

        // Listener
        mImageBackHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductActivity.this, HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });

        mLinearFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayoutShop.openDrawer(GravityCompat.END);
            }
        });

        mListViewProduct.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_MOVE){
                    hideSoftKeyboard(v);
                }
                return false;
            }
        });

    }

    private void initView(){
        mDrawerLayoutShop = (DrawerLayout) findViewById(R.id.drawer_layout_shop);
        mLinearFilter = (LinearLayout) findViewById(R.id.lnr_filter);
        mNavigationFilter = (NavigationView) findViewById(R.id.nav_filter);
        mSearchProduct = (SearchView) findViewById(R.id.search_product);
        mImageBackHome = (ImageView) findViewById(R.id.img_backHome);
        mImageFilter = (ImageView) findViewById(R.id.img_filter);
        mSpinnerSort = (Spinner) findViewById(R.id.spn_sort);
        mListViewProduct = (ListView) findViewById(R.id.lsv_product);
        mRecyclerFilter = (RecyclerView) findViewById(R.id.rcv_filter);
    }

    public void setBackgroundStatusBar() {
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.parseColor("#008194"));
    }

    private void customToolbar() {
        mToolbarShop = findViewById(R.id.toolbar_shop);
        setSupportActionBar(mToolbarShop);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayShowTitleEnabled(false);
    }

    public void openDrawerFilter(View view){
        mDrawerLayoutShop.openDrawer(GravityCompat.START);
    }

    private void customSearchView(){
        SearchView.SearchAutoComplete serAutoComplete = (SearchView.SearchAutoComplete) mSearchProduct.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        serAutoComplete.setHintTextColor(Color.parseColor("#ffffff"));
        serAutoComplete.setTextColor(Color.parseColor("#ffffff"));
        mSearchProduct.setFocusable(false);
        mSearchProduct.clearFocus();
    }

    private void customSpinnerFilter(){
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.sort_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerSort.setAdapter(adapter);
    }

    public void filterSelected(View v){
        mDrawerLayoutShop.closeDrawer(Gravity.END);
    }

    public void hideSoftKeyboard(View view){
        InputMethodManager inputManager =
                (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(
                mSearchProduct.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mnu_cart:
                startActivity(new Intent(ProductActivity.this, ProductDetailActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_bar, menu);
        menu.findItem(R.id.mnu_wishlist).setVisible(false);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void setAdapterProduct(List<Product> products) {
        ViewCompat.setNestedScrollingEnabled(mListViewProduct, true);
        mProductVerticalAdapter = new ProductVerticalAdapter(mListViewProduct.getContext(), R.layout.row_product_item_vertical, products);
        mListViewProduct.setAdapter(mProductVerticalAdapter);
    }

    @Override
    public void generateFilters(ArrayList<ParentObject> parentObjects) {
        FilterExpandableAdapter filterExpandableAdapter = new FilterExpandableAdapter(mRecyclerFilter.getContext(), parentObjects);
        filterExpandableAdapter.setCustomParentAnimationViewId(R.id.ibn_header_more);
        filterExpandableAdapter.setParentClickableViewAnimationDefaultDuration();
        filterExpandableAdapter.setParentAndIconExpandOnClick(true);

        ChipsLayoutManager chipsLayoutManager = ChipsLayoutManager.newBuilder(mRecyclerFilter.getContext())
                .setScrollingEnabled(true)
                .setOrientation(ChipsLayoutManager.HORIZONTAL)
                .build();
        mRecyclerFilter.setLayoutManager(chipsLayoutManager);
        mRecyclerFilter.setItemAnimator(new DefaultItemAnimator());
        mRecyclerFilter.setAdapter(filterExpandableAdapter);
    }
}
