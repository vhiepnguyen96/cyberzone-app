package com.n8plus.vhiep.cyberzone.ui.productdetails;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.AppBarLayout;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.n8plus.vhiep.cyberzone.data.model.Overview;
import com.n8plus.vhiep.cyberzone.data.model.Product;
import com.n8plus.vhiep.cyberzone.data.model.ProductImage;
import com.n8plus.vhiep.cyberzone.data.model.PurchaseItem;
import com.n8plus.vhiep.cyberzone.data.model.ReviewProduct;
import com.n8plus.vhiep.cyberzone.data.model.Store;
import com.n8plus.vhiep.cyberzone.ui.home.HomeActivity;
import com.n8plus.vhiep.cyberzone.ui.login.LoginActivity;
import com.n8plus.vhiep.cyberzone.ui.manage.ManageActivity;
import com.n8plus.vhiep.cyberzone.ui.productdetails.adapter.CustomerRatingAdapter;
import com.n8plus.vhiep.cyberzone.ui.productdetails.adapter.PolicyAdapter;
import com.n8plus.vhiep.cyberzone.ui.productdetails.adapter.SlideImageAdapter;
import com.n8plus.vhiep.cyberzone.ui.productdetails.adapter.SpecificationAdapter;
import com.n8plus.vhiep.cyberzone.ui.cart.CartActivity;
import com.n8plus.vhiep.cyberzone.ui.productdetails.specification.SpecificationFragment;
import com.n8plus.vhiep.cyberzone.data.model.Policy;
import com.n8plus.vhiep.cyberzone.data.model.Specification;
import com.n8plus.vhiep.cyberzone.R;
import com.n8plus.vhiep.cyberzone.ui.product.ProductActivity;
import com.n8plus.vhiep.cyberzone.ui.store.StoreActivity;
import com.n8plus.vhiep.cyberzone.util.Constant;
import com.n8plus.vhiep.cyberzone.util.SessionManager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import at.blogc.android.views.ExpandableTextView;
import me.relex.circleindicator.CircleIndicator;

import static android.support.v4.util.Preconditions.checkNotNull;

public class ProductDetailActivity extends AppCompatActivity implements ProductDetailContract.View {
    private static final String TAG = ProductDetailActivity.class.getSimpleName();
    private ViewPager mSlideImage;
    private CircleIndicator mIndicatorSlide;
    private static int currentPage = 0;
    private ImageView mImageBackShop, mImageCartCustom, mImageFavorite;
    private TextView mMoreSpecification, mExpandOverview, mProductName, mPriceBasic, mPriceSale, mProductDiscount,
            mTextProductCount, mStoreName, mStoreLocation, mGoToStore, mCount5star, mCount4star, mCount3star,
            mCount2star, mCount1star, mTotalRate, mCountRate, mBuyNow, mAddToCart;
    private AppBarLayout mAppbarProductDetail;
    private Toolbar mToolbarProductDetails;
    private RecyclerView mRecyclerSpecificationLite, mRecyclerPoclicyProduct, mRecyclerRatingProduct;
    private RecyclerView.LayoutManager mLayoutManagerLite, mPolicyLayoutManager, mRatingLayoutManager;
    private ExpandableTextView mExpandableOverview;
    private NestedScrollView mScrollViewProductDetail;
    private RelativeLayout mRelativeCart;
    private RatingBar mRatingOverview, mRatingProduct;
    private ProgressBar mProgress5star, mProgress4star, mProgress3star, mProgress2star, mProgress1star;
    private LinearLayout mLinearBgCart, mLinearPriceOnSale;
    private FrameLayout mFrameProductCount;
    private SpecificationAdapter mSpecificationLiteAdapter;
    private PolicyAdapter mPolicyAdapter;
    private CustomerRatingAdapter mRatingProductAdapter;
    private Menu mMenu;
    private ProductDetailPresenter mProductDetailPresenter;
    private SessionManager mSessionManager;
    private ArrayList<Specification> mSpecifications = new ArrayList<Specification>();
    private static int wishlist = 0;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_details_act);
        initView();

        // Custom
        customToolbar();
        customExpandableOverview();

        // Presenter
        mProductDetailPresenter = new ProductDetailPresenter(this);
        mSessionManager = new SessionManager(this);

        Intent intent = getIntent();
        if (intent != null) {
            Product product = (Product) intent.getSerializableExtra("product");
            mProductDetailPresenter.loadProductDetail(product);
        }

        // Listener
        mImageBackShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mImageFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSessionManager.isLoggedIn()) {
                    wishlist++;
                    if (wishlist % 2 == 1) {
                        mProductDetailPresenter.addToWishList();
                    } else {
                        mProductDetailPresenter.removeFromWishList();
                    }
                } else {
                    showRequireLogin();
                }

            }
        });

        mMoreSpecification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("arrSpecifications", (Serializable) mSpecifications);
                SpecificationFragment specificationFragment = new SpecificationFragment();
                specificationFragment.setArguments(bundle);
                specificationFragment.show(getSupportFragmentManager(), specificationFragment.getTag());
            }
        });

        mExpandOverview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mExpandableOverview.isExpanded()) {
                    mExpandableOverview.collapse();
                    mExpandOverview.setText("Mở rộng");
                } else {
                    mExpandableOverview.expand();
                    mExpandOverview.setText("Thu gọn");
                }
            }
        });

        mGoToStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProductDetailPresenter.prepareDataStore();
            }
        });

        mBuyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProductDetailPresenter.buyNow();
            }
        });

        mAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProductDetailPresenter.addToCart();
            }
        });

    }


    @Override
    protected void onRestart() {
        setCartMenuItem();
        super.onRestart();
    }

    private void initView() {
        mSlideImage = (ViewPager) findViewById(R.id.vpg_slide);
        mIndicatorSlide = (CircleIndicator) findViewById(R.id.indicator_slide);
        mImageBackShop = (ImageView) findViewById(R.id.img_backShop);
        mImageFavorite = (ImageView) findViewById(R.id.img_favorite);
        mRecyclerSpecificationLite = (RecyclerView) findViewById(R.id.rcv_specificationLite);
        mRecyclerPoclicyProduct = (RecyclerView) findViewById(R.id.rcv_poclicyProduct);
        mRecyclerRatingProduct = (RecyclerView) findViewById(R.id.rcv_rating_product);
        mMoreSpecification = (TextView) findViewById(R.id.txt_moreSpecification);
        mProductName = (TextView) findViewById(R.id.tv_product_name);
        mPriceBasic = (TextView) findViewById(R.id.tv_product_price_basic);
        mPriceSale = (TextView) findViewById(R.id.tv_product_price_sale);
        mProductDiscount = (TextView) findViewById(R.id.tv_product_discount);
        mMoreSpecification = (TextView) findViewById(R.id.txt_moreSpecification);
        mStoreName = (TextView) findViewById(R.id.tv_store_name);
        mStoreLocation = (TextView) findViewById(R.id.tv_store_location);
        mGoToStore = (TextView) findViewById(R.id.tv_go_to_store);
        mExpandableOverview = (ExpandableTextView) findViewById(R.id.exp_overview);
        mExpandOverview = (TextView) findViewById(R.id.txt_expandOverview);
        mScrollViewProductDetail = (NestedScrollView) findViewById(R.id.scv_product_detail);
        mAppbarProductDetail = (AppBarLayout) findViewById(R.id.appbar_productDetail);
        mLinearPriceOnSale = (LinearLayout) findViewById(R.id.lnr_price_on_sales);
        mRatingProduct = (RatingBar) findViewById(R.id.rbr_rate_product);
        mRatingOverview = (RatingBar) findViewById(R.id.rbr_rate_average);
        mProgress5star = (ProgressBar) findViewById(R.id.pb_rate_5star);
        mProgress4star = (ProgressBar) findViewById(R.id.pb_rate_4star);
        mProgress3star = (ProgressBar) findViewById(R.id.pb_rate_3star);
        mProgress2star = (ProgressBar) findViewById(R.id.pb_rate_2star);
        mProgress1star = (ProgressBar) findViewById(R.id.pb_rate_1star);
        mCount5star = (TextView) findViewById(R.id.tv_count_rate_5star);
        mCount4star = (TextView) findViewById(R.id.tv_count_rate_4star);
        mCount3star = (TextView) findViewById(R.id.tv_count_rate_3star);
        mCount2star = (TextView) findViewById(R.id.tv_count_rate_2star);
        mCount1star = (TextView) findViewById(R.id.tv_count_rate_1star);
        mTotalRate = (TextView) findViewById(R.id.tv_total_rate);
        mCountRate = (TextView) findViewById(R.id.tv_product_rating_count);
        mBuyNow = (TextView) findViewById(R.id.tv_buy_now);
        mAddToCart = (TextView) findViewById(R.id.tv_add_to_cart);
    }

    private void customToolbar() {
        mToolbarProductDetails = findViewById(R.id.toolbar_productDetails);
        setSupportActionBar(mToolbarProductDetails);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayShowTitleEnabled(false);
    }

    private void customExpandableOverview() {
        mExpandableOverview.setInterpolator(new OvershootInterpolator());
        mExpandableOverview.setExpandInterpolator(new OvershootInterpolator());
        mExpandableOverview.setCollapseInterpolator(new OvershootInterpolator());
    }

    private ArrayList<Specification> getSpecificationLite(ArrayList<Specification> arrSpecifications) {
        ArrayList<Specification> arrayList = new ArrayList<>();
        if (arrSpecifications.size() > 5) {
            for (int i = 0; i < 5; i++) {
                arrayList.add(arrSpecifications.get(i));
            }
        } else {
            arrayList.addAll(arrSpecifications);
        }
        return arrayList;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mnu_home:
                Intent intent = new Intent(ProductDetailActivity.this, HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                break;
            case R.id.mnu_account:
                startActivity(new Intent(ProductDetailActivity.this, ManageActivity.class));
                break;
            case R.id.mnu_signin:
                startActivity(new Intent(ProductDetailActivity.this, LoginActivity.class));
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

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        final MenuItem cartMenuItem = menu.findItem(R.id.mnu_cart);
        mRelativeCart = (RelativeLayout) cartMenuItem.getActionView();
        mImageCartCustom = (ImageView) mRelativeCart.findViewById(R.id.img_cart_custom);
        mLinearBgCart = (LinearLayout) mRelativeCart.findViewById(R.id.lnr_bgCart);
        mFrameProductCount = (FrameLayout) mRelativeCart.findViewById(R.id.view_count_product);
        mTextProductCount = (TextView) mRelativeCart.findViewById(R.id.countProduct);

        setCartMenuItem();

        mRelativeCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProductDetailActivity.this, CartActivity.class));
            }
        });

        mScrollViewProductDetail.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY > mToolbarProductDetails.getHeight()) {
                    mToolbarProductDetails.setBackgroundColor(Color.parseColor("#ffffff"));
                    mAppbarProductDetail.setElevation(10.f);
                    mImageBackShop.setImageResource(R.drawable.ic_arrow_back_primary_24dp);
                    mImageBackShop.setBackgroundColor(Color.parseColor("#ffffff"));
                    mLinearBgCart.setBackgroundColor(Color.TRANSPARENT);
                    mImageCartCustom.setImageResource(R.drawable.cart_primary);

                } else {
                    mToolbarProductDetails.setBackgroundColor(Color.TRANSPARENT);
                    mImageBackShop.setImageResource(R.drawable.ic_arrow_back_black_24dp);
                    mImageBackShop.setBackgroundResource(R.drawable.rounded_blue);
                    mAppbarProductDetail.setElevation(0);
                    mLinearBgCart.setBackgroundResource(R.drawable.rounded_blue);
                    mImageCartCustom.setImageResource(R.drawable.cart_white);
                }
            }
        });

        return super.onPrepareOptionsMenu(menu);
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

    public void showRequireLogin() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Thông báo");
        builder.setMessage("Bạn chưa đăng nhập!");
        builder.setCancelable(false);
        builder.setPositiveButton("Đăng nhập ngay", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                startActivity(new Intent(ProductDetailActivity.this, LoginActivity.class));
                dialogInterface.dismiss();
                finish();
            }
        });
        builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void setImageList(List<ProductImage> imageList) {
        mSlideImage.setAdapter(new SlideImageAdapter(ProductDetailActivity.this, imageList));
        mIndicatorSlide.setViewPager(mSlideImage);
    }

    @Override
    public void setProductName(String name) {
        mProductName.setText(name);
    }

    @Override
    public void setProductPrice(String price) {
        mPriceBasic.setText(price);
    }

    @Override
    public void setProductPriceSale(String priceSale) {
        mPriceSale.setText(priceSale);
    }

    @Override
    public void setLayoutPriceSale(boolean b) {
        mLinearPriceOnSale.setVisibility(b ? View.VISIBLE : View.GONE);
    }

    @Override
    public void setProductDiscount(int discount) {
        mProductDiscount.setText(String.valueOf(discount));
    }

    @Override
    public void setProductSpecification(List<Specification> specifications) {
        if (specifications.size() > 0) {
            mSpecifications = (ArrayList<Specification>) specifications;
            mSpecificationLiteAdapter = new SpecificationAdapter(getSpecificationLite((ArrayList<Specification>) specifications));
            mLayoutManagerLite = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            mRecyclerSpecificationLite.setLayoutManager(mLayoutManagerLite);
            mRecyclerSpecificationLite.setAdapter(mSpecificationLiteAdapter);
            mRecyclerSpecificationLite.setNestedScrollingEnabled(false);
        }
    }

    @Override
    public void setProductOverviews(List<Overview> overviews) {
        if (overviews.size() > 0) {
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < overviews.size(); i++) {
                if (overviews.get(i).getTitle() != null && !overviews.get(i).getTitle().isEmpty()) {
                    builder.append(overviews.get(i).getTitle() + "\n");
                }
                builder.append(overviews.get(i).getValue() + "\n");
            }
            mExpandableOverview.setText(builder);
        }
    }

    @Override
    public void setProductPolicies(List<Policy> policies) {
        mPolicyAdapter = new PolicyAdapter(policies);
        mPolicyLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerPoclicyProduct.setLayoutManager(mPolicyLayoutManager);
        mRecyclerPoclicyProduct.setAdapter(mPolicyAdapter);
        mRecyclerPoclicyProduct.setNestedScrollingEnabled(false);
    }

    @Override
    public void setStoreName(String storeName) {
        mStoreName.setText(storeName);
    }

    @Override
    public void setStoreLocation(String location) {
        mStoreLocation.setText(location);
    }

    @Override
    public void setAdapterRatingProduct(List<ReviewProduct> ratingProductList) {
        mRatingProductAdapter = new CustomerRatingAdapter(ratingProductList);
        mRatingLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerRatingProduct.setLayoutManager(mRatingLayoutManager);
        mRecyclerRatingProduct.setAdapter(mRatingProductAdapter);
        mRecyclerRatingProduct.setNestedScrollingEnabled(false);
    }

    @Override
    public void setRatingBar(int count, float rating) {
        mTotalRate.setText(String.valueOf(count));
        mCountRate.setText(String.valueOf(count));
        mRatingProduct.setRating(rating);
        mRatingOverview.setRating(rating);
    }

    @Override
    public void setRating5star(int max, int count) {
        mProgress5star.setMax(max);
        mProgress5star.setProgress(count);
        mCount5star.setText(String.valueOf(count));
    }

    @Override
    public void setRating4star(int max, int count) {
        mProgress4star.setMax(max);
        mProgress4star.setProgress(count);
        mCount4star.setText(String.valueOf(count));
    }

    @Override
    public void setRating3star(int max, int count) {
        mProgress3star.setMax(max);
        mProgress3star.setProgress(count);
        mCount3star.setText(String.valueOf(count));
    }

    @Override
    public void setRating2star(int max, int count) {
        mProgress2star.setMax(max);
        mProgress2star.setProgress(count);
        mCount2star.setText(String.valueOf(count));
    }

    @Override
    public void setRating1star(int max, int count) {
        mProgress1star.setMax(max);
        mProgress1star.setProgress(count);
        mCount1star.setText(String.valueOf(count));
    }

    @Override
    public void setReviewNone(boolean b) {
        findViewById(R.id.lnr_review_none).setVisibility(b ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public void setLayoutRatingProduct(boolean b) {
        findViewById(R.id.lnr_rate_product).setVisibility(b ? View.VISIBLE : View.GONE);
    }

    @Override
    public void moveToStore(Store store) {
        Intent intent = new Intent(ProductDetailActivity.this, StoreActivity.class);
        intent.putExtra("store", store);
        startActivity(intent);
    }

    @Override
    public void moveToCart() {
        startActivity(new Intent(ProductDetailActivity.this, CartActivity.class));
    }

    @Override
    public void addToCartAlert(boolean b) {
        Toast.makeText(this, b ? "Thêm vào giỏ hàng thành công" : "Thêm vào giỏ hàng thất bại", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setCartMenuItem() {
        mFrameProductCount.setVisibility(Constant.countProductInCart() == 0 ? View.INVISIBLE : View.VISIBLE);
        mTextProductCount.setText(String.valueOf(Constant.countProductInCart()));
    }

    @Override
    public void setWishListResult(boolean b) {
        mImageFavorite.setImageResource(b ? R.drawable.ic_favorite_red_24dp : R.drawable.ic_favorite_border_black_24dp);
        wishlist = b ? 1 : 0;
    }

    @Override
    public void addToWishListResult(boolean b) {
        Toast.makeText(this, b ? "Đã thêm vào danh sách yêu thích!" : "Vui lòng thử lại!", Toast.LENGTH_SHORT).show();
        mImageFavorite.setImageResource(b ? R.drawable.ic_favorite_red_24dp : R.drawable.ic_favorite_border_black_24dp);
    }

    @Override
    public void removeFromWishListResult(boolean b) {
        Toast.makeText(this, b ? "Đã xóa khỏi danh sách yêu thích!" : "Vui lòng thử lại!", Toast.LENGTH_SHORT).show();
        mImageFavorite.setImageResource(b ? R.drawable.ic_favorite_border_black_24dp : R.drawable.ic_favorite_red_24dp);
    }


    // Auto start of viewpager
//        final Handler handler = new Handler();
//        final Runnable Update = new Runnable() {
//            public void run() {
//                if (currentPage == imageProducts.length) {
//                    currentPage = 0;
//                }
//                vpg_slide.setCurrentItem(currentPage++, true);
//            }
//        };
//        Timer swipeTimer = new Timer();
//        swipeTimer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                handler.post(Update);
//            }
//        }, 2500, 2500);
}
