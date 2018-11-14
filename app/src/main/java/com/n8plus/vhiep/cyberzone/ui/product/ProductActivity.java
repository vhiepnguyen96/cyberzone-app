package com.n8plus.vhiep.cyberzone.ui.product;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.graphics.Color;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager;
import com.bignerdranch.expandablerecyclerview.Model.ParentObject;
import com.n8plus.vhiep.cyberzone.base.BaseRecyclerViewAdapter;
import com.n8plus.vhiep.cyberzone.data.model.FilterChild;
import com.n8plus.vhiep.cyberzone.ui.cart.CartActivity;
import com.n8plus.vhiep.cyberzone.ui.login.LoginActivity;
import com.n8plus.vhiep.cyberzone.ui.manage.ManageActivity;
import com.n8plus.vhiep.cyberzone.ui.product.adapter.FilterExpandableAdapter;
import com.n8plus.vhiep.cyberzone.ui.product.adapter.LoadMoreProductAdapter;
import com.n8plus.vhiep.cyberzone.ui.product.adapter.ProductVerticalAdapter;
import com.n8plus.vhiep.cyberzone.ui.product.adapter.RecyclerProductAdapter;
import com.n8plus.vhiep.cyberzone.data.model.Product;
import com.n8plus.vhiep.cyberzone.R;
import com.n8plus.vhiep.cyberzone.ui.productdetails.ProductDetailActivity;
import com.n8plus.vhiep.cyberzone.util.Constant;
import com.n8plus.vhiep.cyberzone.util.EndlessRecyclerViewScrollListener;
import com.n8plus.vhiep.cyberzone.util.ItemDecorationColumns;
import com.n8plus.vhiep.cyberzone.util.LoadMoreRecyclerViewAdapter;
import com.n8plus.vhiep.cyberzone.util.SessionManager;
import com.n8plus.vhiep.cyberzone.util.TypeLoad;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ProductActivity extends AppCompatActivity implements ProductContract.View, BaseRecyclerViewAdapter.ItemClickListener, LoadMoreRecyclerViewAdapter.RetryLoadMoreListener {
    private static final String TAG = "ProductActivity";
    private int GRID_LAYOUT = 1, LINEAR_LAYOUT = 2;
    private Toolbar mToolbarShop;
    private NavigationView mNavigationFilter;
    private DrawerLayout mDrawerLayoutShop;
    private RelativeLayout mLayoutFilter;
    private LinearLayout mLayoutRecycler, mNotFound, mLinearProgress, mLinearLoading;
    private FrameLayout mFrameProductCount;
    private SearchView mSearchProduct;
    private ImageView mImageBackHome, mImageFilter, mImageLayoutRecycler;
    private Spinner mSpinnerSort;
    private TextView mResetFilter, mFilterSelected, mCountFilter, mTextProductCount;
    private RecyclerView mRecyclerProduct;
    private RecyclerView mRecyclerFilter;
    private LoadMoreProductAdapter mLoadMoreProductAdapter;
    private GridLayoutManager mGridLayoutManager;
    private LinearLayoutManager mLinearLayoutManager;
    private FilterExpandableAdapter mFilterExpandableAdapter;
    private ProductPresenter mProductPresenter;
    private SessionManager mSessionManager;
    private Menu mMenu;
    private TypeLoad mTypeLoad;
    private static int layout = 0;
    private ItemDecorationColumns mItemDecorationColumns;
    private DividerItemDecoration mDividerItemDecoration;
    private String[] mSortArray;
    private List<FilterChild> mFilterChildList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        setContentView(R.layout.products_act);
        initView();

        // Custom view
        setBackgroundStatusBar();
        customToolbar();
        customSearchView();
        customSpinnerSort();
        customRecyclerProduct();

        // Presenter
        mProductPresenter = new ProductPresenter(this);
        mSessionManager = new SessionManager(this);

        Intent intentFromHome = getIntent();
        if (intentFromHome != null && intentFromHome.getSerializableExtra("TypeLoad") != null) {
            mTypeLoad = (TypeLoad) intentFromHome.getSerializableExtra("TypeLoad");
            Bundle bundle = intentFromHome.getBundleExtra("Data");
            switch (mTypeLoad) {
                case BEST_SELLER:
                    mProductPresenter.loadProductBestSeller();
                    break;
                case ON_SALE:
                    mProductPresenter.loadProductOnSale();
                    break;
                case PRODUCT_TYPE:
                    if (bundle != null && bundle.getString("productType") != null) {
                        String productTypeId = bundle.getString("productType");
                        mProductPresenter.loadProductByProductType(productTypeId);
                    } else {
                        setAlert("Error by Product-Type");
                    }
                    break;
                case SUGGESTION:
                    mProductPresenter.loadProductSuggestion();
                    break;
                case KEYWORD:
                    if (bundle != null && bundle.getString("keyword") != null) {
                        String keyword = bundle.getString("keyword");
                        mProductPresenter.loadProductByKeyWord(keyword);
                    } else {
                        setAlert("Error by Keyword");
                    }
                    break;
            }
        } else setAlert("Intent Null");

        // Listener
        mImageBackHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mLayoutFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayoutShop.openDrawer(GravityCompat.END);
                mDrawerLayoutShop.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN);
            }
        });

        mLayoutRecycler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout++;
                if (layout % 2 == 1) {
                    mImageLayoutRecycler.setImageResource(R.drawable.grid);
                    setAdapterProduct(mLoadMoreProductAdapter.getDataList(), LINEAR_LAYOUT);

                } else {
                    mImageLayoutRecycler.setImageResource(R.drawable.linear);
                    setAdapterProduct(mLoadMoreProductAdapter.getDataList(), GRID_LAYOUT);
                }
            }
        });

        mSpinnerSort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (mLoadMoreProductAdapter != null) {
                    if (position == 0) {
                        Collections.sort(mLoadMoreProductAdapter.getDataList(), new Comparator<Product>() {
                            @Override
                            public int compare(Product o1, Product o2) {
                                return Float.valueOf(o2.getAverageReview()).compareTo(Float.valueOf(o1.getAverageReview()));
                            }
                        });

                    } else if (position == 1) {
                        Collections.sort(mLoadMoreProductAdapter.getDataList(), new Comparator<Product>() {
                            @Override
                            public int compare(Product o1, Product o2) {
                                return Float.valueOf(o1.getPrice()).compareTo(Float.valueOf(o2.getPrice()));
                            }
                        });
                    } else if (position == 2) {
                        Collections.sort(mLoadMoreProductAdapter.getDataList(), new Comparator<Product>() {
                            @Override
                            public int compare(Product o1, Product o2) {
                                return Float.valueOf(o2.getPrice()).compareTo(Float.valueOf(o1.getPrice()));
                            }
                        });
                    }
                    mRecyclerProduct.setAdapter(mLoadMoreProductAdapter);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mResetFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProductPresenter.resetFilter();
            }
        });

        mFilterSelected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayoutShop.closeDrawer(Gravity.END);
                mFilterChildList = (List<FilterChild>) (Object) mFilterExpandableAdapter.getFilterChildChoose();
                setCountFilter(mFilterChildList.size());
                mLoadMoreProductAdapter.filterProduct(mFilterChildList);
                showLinearNotFound(mLoadMoreProductAdapter.getDataList().size() == 0 ? true : false);
            }
        });

        mSearchProduct.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mProductPresenter.loadProductByKeyWord(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        mSearchProduct.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    mProductPresenter.loadProductByKeyWord(mSearchProduct.getQuery().toString());
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }


    @Override
    protected void onRestart() {
        setCartMenuItem();
        prepareOptionMenu(mSessionManager.isLoggedIn());
        super.onRestart();
    }

    private void initView() {
        mDrawerLayoutShop = (DrawerLayout) findViewById(R.id.drawer_layout_shop);
        mLayoutRecycler = (LinearLayout) findViewById(R.id.layout_recycler);
        mNotFound = (LinearLayout) findViewById(R.id.lnr_not_found);
        mLinearLoading = (LinearLayout) findViewById(R.id.lnr_loading);
        mLayoutFilter = (RelativeLayout) findViewById(R.id.layout_filter);
        mNavigationFilter = (NavigationView) findViewById(R.id.nav_filter);
        mSearchProduct = (SearchView) findViewById(R.id.search_product);
        mImageBackHome = (ImageView) findViewById(R.id.img_backHome);
        mImageFilter = (ImageView) findViewById(R.id.img_filter);
        mImageLayoutRecycler = (ImageView) findViewById(R.id.img_layout_recycler);
        mSpinnerSort = (Spinner) findViewById(R.id.spn_sort);
        mResetFilter = (TextView) findViewById(R.id.tv_reset_filter);
        mFilterSelected = (TextView) findViewById(R.id.tv_filter_selected);
        mCountFilter = (TextView) findViewById(R.id.txt_count_filter);
        mRecyclerProduct = (RecyclerView) findViewById(R.id.rcv_product);
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

    private void customRecyclerProduct() {
        mItemDecorationColumns = new ItemDecorationColumns(10, 2);
        mDividerItemDecoration = new DividerItemDecoration(mRecyclerProduct.getContext(), DividerItemDecoration.VERTICAL);
        mDividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.divider_linear_layout));
    }

    public void setCountFilter(int count) {
        if (count == 0) {
            mCountFilter.setVisibility(View.INVISIBLE);
        } else {
            mCountFilter.setVisibility(View.VISIBLE);
            mCountFilter.setText(String.valueOf(count));
        }
    }

    public void openDrawerFilter(View view) {
        mDrawerLayoutShop.openDrawer(GravityCompat.START);
    }

    private void customSearchView() {
        SearchView.SearchAutoComplete serAutoComplete = (SearchView.SearchAutoComplete) mSearchProduct.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        serAutoComplete.setHintTextColor(Color.parseColor("#ffffff"));
        serAutoComplete.setTextColor(Color.parseColor("#ffffff"));
        mSearchProduct.setFocusable(false);
        mSearchProduct.clearFocus();
    }

    private void customSpinnerSort() {
        mSortArray = getResources().getStringArray(R.array.sort_array);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.sort_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerSort.setAdapter(adapter);
    }

    public void hideSoftKeyboard(View view) {
        InputMethodManager inputManager =
                (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(
                mSearchProduct.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    @Override
    public void startLoadMore() {
        mLoadMoreProductAdapter.startLoadMore();
    }

    @Override
    public void onLoadMoreFailed() {
        mLoadMoreProductAdapter.onLoadMoreFailed();
    }

    @Override
    public void setDataListAdapter(List<Product> dataList) {
        mLoadMoreProductAdapter.set(dataList);
    }

    @Override
    public void addDataListAdapter(List<Product> dataList) {
        mLoadMoreProductAdapter.add(dataList);
    }

    @Override
    public void setNotifyItemRemoved(int position) {
        mLoadMoreProductAdapter.getDataList().remove(position);
        mLoadMoreProductAdapter.notifyItemRemoved(position);
    }

    @Override
    public void onReachEnd() {
        mLoadMoreProductAdapter.onReachEnd();
    }

    @Override
    public void setAlert(String message) {
        Toast.makeText(ProductActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void moveToProductDetail(Product product) {
        Intent intent = new Intent(ProductActivity.this, ProductDetailActivity.class);
        intent.putExtra("product", product);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mnu_home:
                this.finish();
                break;
            case R.id.mnu_account:
                startActivity(new Intent(ProductActivity.this, ManageActivity.class));
                break;
            case R.id.mnu_signin:
                startActivity(new Intent(ProductActivity.this, LoginActivity.class));
                break;
            case R.id.mnu_signout:
                mSessionManager.signOut();
                prepareOptionMenu(mSessionManager.isLoggedIn());
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_bar, menu);
        mMenu = menu;
        prepareOptionMenu(mSessionManager.isLoggedIn());
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
                startActivity(new Intent(ProductActivity.this, CartActivity.class));
            }
        });
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void setAdapterProduct(List<Product> products, final int layout) {
        if (layout == GRID_LAYOUT) {
            mGridLayoutManager = new GridLayoutManager(this, 2);
            mRecyclerProduct.setLayoutManager(mGridLayoutManager);
            mLoadMoreProductAdapter = new LoadMoreProductAdapter(this, this, this, GRID_LAYOUT);
            mLoadMoreProductAdapter.set(products);
            mRecyclerProduct.setAdapter(mLoadMoreProductAdapter);
            mRecyclerProduct.removeItemDecoration(mDividerItemDecoration);
            if (mRecyclerProduct.getItemDecorationCount() == 0) {
                mRecyclerProduct.addItemDecoration(mItemDecorationColumns);
            }

        } else {
            mLinearLayoutManager = new LinearLayoutManager(mRecyclerProduct.getContext(), LinearLayoutManager.VERTICAL, false);
            mRecyclerProduct.setLayoutManager(mLinearLayoutManager);
            mLoadMoreProductAdapter = new LoadMoreProductAdapter(this, this, this, LINEAR_LAYOUT);
            mLoadMoreProductAdapter.set(products);
            mRecyclerProduct.setAdapter(mLoadMoreProductAdapter);
            mRecyclerProduct.removeItemDecoration(mItemDecorationColumns);
            mRecyclerProduct.addItemDecoration(mDividerItemDecoration);
        }

        if (products.size() >= 10) {
            mRecyclerProduct.addOnScrollListener(new EndlessRecyclerViewScrollListener(layout == GRID_LAYOUT ? mGridLayoutManager : mLinearLayoutManager) {
                @Override
                public void onLoadMore(int page) {
                    Log.d(TAG, "TypeLoad: " + mTypeLoad.name() + ", LoadPage: " + page);
                    if (mFilterChildList == null) {
                        mProductPresenter.loadMoreProduct(mTypeLoad, page);
                    } else {
                        if (mFilterChildList.size() == 0) {
                            mProductPresenter.loadMoreProduct(mTypeLoad, page);
                        } else {
                            this.setCurrentPage(page - 1);
                        }
                    }

                }
            });
        } else {
            mLoadMoreProductAdapter.onReachEnd();
        }
    }

    @Override
    public void setNotifyDataSetChanged() {
        mLoadMoreProductAdapter.notifyDataSetChanged();
    }

    @Override
    public void setNotifyItemChanged(int position) {
        mLoadMoreProductAdapter.notifyItemChanged(position);
    }

    @Override
    public void generateFilters(ArrayList<ParentObject> parentObjects) {
        mFilterExpandableAdapter = new FilterExpandableAdapter(mRecyclerFilter.getContext(), parentObjects);
        mFilterExpandableAdapter.setCustomParentAnimationViewId(R.id.ibn_header_more);
        mFilterExpandableAdapter.setParentClickableViewAnimationDefaultDuration();
        mFilterExpandableAdapter.setParentAndIconExpandOnClick(true);

        ChipsLayoutManager chipsLayoutManager = ChipsLayoutManager.newBuilder(mRecyclerFilter.getContext())
                .setScrollingEnabled(true)
                .setOrientation(ChipsLayoutManager.HORIZONTAL)
                .build();
        mRecyclerFilter.setLayoutManager(chipsLayoutManager);
        mRecyclerFilter.setItemAnimator(new DefaultItemAnimator());
        mRecyclerFilter.setAdapter(mFilterExpandableAdapter);

        setCountFilter(mFilterExpandableAdapter.getFilterChildChoose().size());
    }

    @Override
    public void setCartMenuItem() {
        mFrameProductCount.setVisibility(Constant.countProductInCart() == 0 ? View.INVISIBLE : View.VISIBLE);
        mTextProductCount.setText(String.valueOf(Constant.countProductInCart()));
    }

    @Override
    public void setKeyword(String keyword) {
        mSearchProduct.setQuery(keyword, false);
    }

    @Override
    public void showLinearNotFound(boolean b) {
        mNotFound.setVisibility(b ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showLinearLoading(boolean b) {
        mLinearLoading.setVisibility(b ? View.VISIBLE : View.GONE);
    }

    public void prepareOptionMenu(boolean isLoggedIn) {
        if (isLoggedIn) {
            mMenu.findItem(R.id.mnu_account).setVisible(true);
            mMenu.findItem(R.id.mnu_signout).setVisible(true);
            mMenu.findItem(R.id.mnu_signin).setVisible(false);
        } else {
            mMenu.findItem(R.id.mnu_signin).setVisible(true);
            mMenu.findItem(R.id.mnu_account).setVisible(false);
            mMenu.findItem(R.id.mnu_signout).setVisible(false);
        }
    }

    public void searchProductLocal(String query) {
        mLoadMoreProductAdapter.searchProduct(query);
        if (query.isEmpty()) {
            mSpinnerSort.setSelection(0);
        }
        showLinearNotFound(mLoadMoreProductAdapter.getDataList().size() == 0 ? true : false);
    }

    @Override
    public void onItemClick(View view, int position) {
        moveToProductDetail(mLoadMoreProductAdapter.getDataList().get(position));
    }

    @Override
    public void onRetryLoadMore() {

    }
}
