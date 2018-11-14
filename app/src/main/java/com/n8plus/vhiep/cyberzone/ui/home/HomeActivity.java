package com.n8plus.vhiep.cyberzone.ui.home;

import android.os.Handler;
import android.os.Parcelable;
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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.n8plus.vhiep.cyberzone.R;
import com.n8plus.vhiep.cyberzone.base.BaseRecyclerViewAdapter;
import com.n8plus.vhiep.cyberzone.data.model.Account;
import com.n8plus.vhiep.cyberzone.ui.home.adapter.PopularCategoryAdapter;
import com.n8plus.vhiep.cyberzone.ui.home.adapter.ProductHorizontalAdapter;
import com.n8plus.vhiep.cyberzone.ui.cart.CartActivity;
import com.n8plus.vhiep.cyberzone.ui.login.LoginActivity;
import com.n8plus.vhiep.cyberzone.ui.manage.ManageActivity;
import com.n8plus.vhiep.cyberzone.ui.product.ProductActivity;
import com.n8plus.vhiep.cyberzone.ui.home.navigation.category.CategoryFragment;
import com.n8plus.vhiep.cyberzone.data.model.Overview;
import com.n8plus.vhiep.cyberzone.data.model.Product;
import com.n8plus.vhiep.cyberzone.data.model.ProductType;
import com.n8plus.vhiep.cyberzone.data.model.Specification;
import com.n8plus.vhiep.cyberzone.ui.product.adapter.LoadMoreProductAdapter;
import com.n8plus.vhiep.cyberzone.ui.product.adapter.RecyclerProductAdapter;
import com.n8plus.vhiep.cyberzone.ui.productdetails.ProductDetailActivity;
import com.n8plus.vhiep.cyberzone.util.Constant;
import com.n8plus.vhiep.cyberzone.util.EndlessRecyclerViewScrollListener;
import com.n8plus.vhiep.cyberzone.util.ILoadMore;
import com.n8plus.vhiep.cyberzone.util.ItemDecorationColumns;
import com.n8plus.vhiep.cyberzone.util.LoadMoreRecyclerViewAdapter;
import com.n8plus.vhiep.cyberzone.util.SessionManager;
import com.n8plus.vhiep.cyberzone.util.TypeLoad;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements HomeContract.View, BaseRecyclerViewAdapter.ItemClickListener, LoadMoreRecyclerViewAdapter.RetryLoadMoreListener {
    private final String TAG = "HomeActivity";
    private int GRID_LAYOUT = 1, LINEAR_LAYOUT = 2;
    private final int BEST_SELLER_ADAPTER = 1, ON_SALE_ADAPTER = 2, SUGGESTION_ADAPTER = 3;
    private RecyclerView mRecyclerSuggestion, mRecyclerOnSales, mRecyclerBestSeller, mRecyclerPopularCategory;
    private RecyclerProductAdapter mOnSaleAdapter, mBestSellerAdapter;
    private LoadMoreProductAdapter mLoadMoreSuggestionAdapter;
    private RecyclerView.LayoutManager mLayoutOnSale, mLayoutBestSeller, mLayoutPopularCategory;
    private GridLayoutManager mLayoutSuggestion;
    private PopularCategoryAdapter mPopularCategoryAdapter;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private FrameLayout mFrameProductCount;
    private ImageButton mAvatar, mCloseDrawer;
    private TextView mTextName, mMoreSuggestion, mMoreOnSale, mMoreBestSeller, mTextProductCount, mMorePopularCategory;
    private SearchView mSearchHome;
    private SwipeRefreshLayout mRefreshLayout;
    private NestedScrollView mNestedScrollView;
    private HomePresenter mHomePresenter;
    private SessionManager mSessionManager;
    private DividerItemDecoration mOnSaleItemDecoration, mBestSellerItemDecoration;
    private LinearLayout mLinearProgress;
    private ProgressBar mProgressLoadMore;
    private Parcelable mBestSellerState, mOnSaleState, mSuggestionState;
    private Menu mMenuHome;
    int currentItems, totalItems, scrollOutItems;
    boolean isLoading = false;

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
        mSessionManager = new SessionManager(this);

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
        mHomePresenter.loadDataCustomer(mSessionManager.getAccountLogin());

        // Listener
        mCloseDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.closeDrawers();
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

        mNestedScrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    hideKeyboard(v);
                }
                return false;
            }
        });

//        mNestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
//            @Override
//            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
//                if (v.getChildAt(v.getChildCount() - 1) != null) {
//                    if ((scrollY >= (v.getChildAt(v.getChildCount() - 1).getMeasuredHeight() - v.getMeasuredHeight())) && scrollY > oldScrollY) {
//                        if (!isLoading) {
//                            currentItems = mLayoutSuggestion.getChildCount();
//                            totalItems = mLayoutSuggestion.getItemCount();
//                            scrollOutItems = mLayoutSuggestion.findFirstVisibleItemPosition();
//                            if ((currentItems + scrollOutItems) >= totalItems) {
//                                Log.d(TAG, "LoadMore");
//                                setVisibilityProgressBar(true);
//                                isLoading = true;
//                                mHomePresenter.loadMoreSuggestion();
//                            }
//                        }
//                    }
//                }
//            }
//        });

        mSearchHome.setOnQueryTextListener(new SearchView.OnQueryTextListener()

        {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mHomePresenter.prepareDataKeyword(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    @Override
    protected void onRestart() {
        setCartMenuItem();
        prepareOptionMenu(mSessionManager.isLoggedIn());
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
        mNestedScrollView = (NestedScrollView) findViewById(R.id.scroll_view_main);
        mRecyclerSuggestion = (RecyclerView) findViewById(R.id.recycler_suggestions);
        mRecyclerOnSales = (RecyclerView) findViewById(R.id.recycler_on_sales);
        mRecyclerBestSeller = (RecyclerView) findViewById(R.id.recycler_best_seller);
        mRecyclerPopularCategory = (RecyclerView) findViewById(R.id.recycler_popular_category);
        mLinearProgress = (LinearLayout) findViewById(R.id.lnr_progress);
        mProgressLoadMore = (ProgressBar) findViewById(R.id.progress_load_more);
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
        mOnSaleItemDecoration = new DividerItemDecoration(mRecyclerOnSales.getContext(), DividerItemDecoration.HORIZONTAL);
        mOnSaleItemDecoration.setDrawable(getResources().getDrawable(R.drawable.divider_horizontal));
        mBestSellerItemDecoration = new DividerItemDecoration(mRecyclerBestSeller.getContext(), DividerItemDecoration.HORIZONTAL);
        mBestSellerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.divider_horizontal));
        mRecyclerBestSeller.addItemDecoration(mBestSellerItemDecoration);
        mRecyclerOnSales.addItemDecoration(mOnSaleItemDecoration);

        mRecyclerPopularCategory.addItemDecoration(new ItemDecorationColumns(4, 3));
        mRecyclerSuggestion.addItemDecoration(new ItemDecorationColumns(8, 2));
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
            case R.id.mnu_signin:
                startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                break;
            case R.id.mnu_signout:
                mSessionManager.signOut();
                prepareOptionMenu(mSessionManager.isLoggedIn());
                setNameCustomer("");
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_bar, menu);
        mMenuHome = menu;
        prepareOptionMenu(mSessionManager.isLoggedIn());
        menu.findItem(R.id.mnu_cart).setActionView(R.layout.cart_menu_item);
        return super.onCreateOptionsMenu(menu);
    }

    public void prepareOptionMenu(boolean isLoggedIn) {
        if (mMenuHome != null) {
            if (isLoggedIn) {
                mMenuHome.findItem(R.id.mnu_account).setVisible(true);
                mMenuHome.findItem(R.id.mnu_signout).setVisible(true);
                mMenuHome.findItem(R.id.mnu_signin).setVisible(false);
            } else {
                mMenuHome.findItem(R.id.mnu_signin).setVisible(true);
                mMenuHome.findItem(R.id.mnu_account).setVisible(false);
                mMenuHome.findItem(R.id.mnu_signout).setVisible(false);
            }
        }
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
        mRecyclerBestSeller.setLayoutManager(mLayoutBestSeller);
        mBestSellerAdapter = new RecyclerProductAdapter(R.layout.row_product_grid_layout, productList, LINEAR_LAYOUT);
        mRecyclerBestSeller.setAdapter(mBestSellerAdapter);
        mRecyclerBestSeller.setNestedScrollingEnabled(true);
    }

    @Override
    public void setAdapterOnSale(List<Product> productList) {
        mLayoutOnSale = new LinearLayoutManager(mRecyclerOnSales.getContext(), LinearLayoutManager.HORIZONTAL, false);
        mOnSaleAdapter = new RecyclerProductAdapter(R.layout.row_product_grid_layout, productList, LINEAR_LAYOUT);
        mRecyclerOnSales.setLayoutManager(mLayoutOnSale);
        mRecyclerOnSales.setAdapter(mOnSaleAdapter);
        mRecyclerOnSales.setNestedScrollingEnabled(true);
    }

    @Override
    public void setAdapterPopularCategory(List<ProductType> productTypeList) {
        mPopularCategoryAdapter = new PopularCategoryAdapter(productTypeList);
        mRecyclerPopularCategory.setLayoutManager(new GridLayoutManager(mRecyclerPopularCategory.getContext(), 3));
        mRecyclerPopularCategory.setAdapter(mPopularCategoryAdapter);
        mRecyclerPopularCategory.setNestedScrollingEnabled(false);
    }

    @Override
    public void setAdapterSuggestion(List<Product> productList) {
        mLayoutSuggestion = new GridLayoutManager(mRecyclerSuggestion.getContext(), 2);
        mRecyclerSuggestion.setLayoutManager(mLayoutSuggestion);
        mLoadMoreSuggestionAdapter = new LoadMoreProductAdapter(this, this, this, GRID_LAYOUT);
        mLoadMoreSuggestionAdapter.set(productList);
        mRecyclerSuggestion.setAdapter(mLoadMoreSuggestionAdapter);
        mRecyclerSuggestion.setNestedScrollingEnabled(false);

        EndlessRecyclerViewScrollListener endlessScrollListener = new EndlessRecyclerViewScrollListener(mLayoutSuggestion) {
            @Override
            public void onLoadMore(int page) {
                Log.d(TAG, "LoadPage: " + page);
                mHomePresenter.loadMoreSuggestion(page);
            }
        };
        mRecyclerSuggestion.addOnScrollListener(endlessScrollListener);
    }

    @Override
    public void startLoadMore() {
        mLoadMoreSuggestionAdapter.startLoadMore();
    }

    @Override
    public void onLoadMoreFailed() {
        mLoadMoreSuggestionAdapter.onLoadMoreFailed();
    }

    @Override
    public void onReachEnd() {
        mLoadMoreSuggestionAdapter.onReachEnd();
    }

    @Override
    public void setDataListAdapter(List<Product> dataList) {
        mLoadMoreSuggestionAdapter.set(dataList);
    }

    @Override
    public void addDataListAdapter(List<Product> dataList) {
        mLoadMoreSuggestionAdapter.add(dataList);
    }

    @Override
    public void setAlert(String message) {
        Toast.makeText(HomeActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setNameCustomer(String nameCustomer) {
        mTextName.setText("Chào, " + nameCustomer);
    }

    @Override
    public void setCartMenuItem() {
        if (mFrameProductCount != null) {
            mFrameProductCount.setVisibility(Constant.countProductInCart() == 0 ? View.INVISIBLE : View.VISIBLE);
            mTextProductCount.setText(String.valueOf(Constant.countProductInCart()));
        }
    }

    @Override
    public void setNotifyDataSetChanged(int adapter) {
        switch (adapter) {
            case BEST_SELLER_ADAPTER:
                mBestSellerAdapter.notifyDataSetChanged();
                break;
            case ON_SALE_ADAPTER:
                mOnSaleAdapter.notifyDataSetChanged();
                break;
            case SUGGESTION_ADAPTER:
                mLoadMoreSuggestionAdapter.notifyDataSetChanged();
                break;
        }
    }

    @Override
    public void setNotifyItemChanged(int adapter, int position) {
        switch (adapter) {
            case BEST_SELLER_ADAPTER:
                mBestSellerAdapter.notifyItemChanged(position);
                break;
            case ON_SALE_ADAPTER:
                mOnSaleAdapter.notifyItemChanged(position);
                break;
            case SUGGESTION_ADAPTER:
                mLoadMoreSuggestionAdapter.notifyItemChanged(position);
                break;
        }
    }

    @Override
    public void popularCategoryItemSelected(String productTypeId) {
        mHomePresenter.prepareDataProductType(productTypeId);
    }

    @Override
    public void moveToProductActivity(TypeLoad type, Bundle data) {
        Intent intent = new Intent(HomeActivity.this, ProductActivity.class);
        intent.putExtra("TypeLoad", type);
        intent.putExtra("Data", data);
        startActivity(intent);
    }

    @Override
    public void moveToProductDetail(Product product) {
        Intent intent = new Intent(HomeActivity.this, ProductDetailActivity.class);
        intent.putExtra("product", product);
        startActivity(intent);
    }

    @Override
    public void onItemClick(View view, int position) {
        moveToProductDetail(mLoadMoreSuggestionAdapter.getDataList().get(position));
    }

    @Override
    public void onRetryLoadMore() {

    }
}

