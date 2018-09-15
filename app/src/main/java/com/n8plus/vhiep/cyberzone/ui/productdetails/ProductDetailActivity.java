package com.n8plus.vhiep.cyberzone.ui.productdetails;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.AppBarLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.style.TtsSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.n8plus.vhiep.cyberzone.data.model.Overview;
import com.n8plus.vhiep.cyberzone.data.model.Product;
import com.n8plus.vhiep.cyberzone.ui.product.ProductContract;
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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import at.blogc.android.views.ExpandableTextView;
import me.relex.circleindicator.CircleIndicator;

import static android.support.v4.util.Preconditions.checkNotNull;

public class ProductDetailActivity extends AppCompatActivity implements ProductDetailContract.View{
    private static final String TAG = ProductDetailActivity.class.getSimpleName();
    private ViewPager mSlideImage;
    private CircleIndicator mIndicatorSlide;
    private static int currentPage = 0;
    private ImageView mImageBackShop, mImageCartCustom;
    private TextView mMoreSpecification, mExpandOverview, mProductName, mPriceBasic, mPriceSale, mProductDiscount, mTextProductCount, mStoreName, mStoreLocation, mGoToStore;
    private AppBarLayout mAppbarProductDetail;
    private Toolbar mToolbarProductDetails;
    private RecyclerView mRecyclerSpecificationLite, mRecyclerPoclicyProduct;
    private RecyclerView.LayoutManager mLayoutManagerLite, mPolicyLayoutManager;
    private ExpandableTextView mExpandableOverview;
    private ScrollView mScrollViewProductDetail;
    private RelativeLayout mRelativeCart;
    private LinearLayout mLinearBgCart, mLinearPriceOnSale;
    private FrameLayout mFrameProductCount;
    private SpecificationAdapter mSpecificationLiteAdapter;
    private PolicyAdapter mPolicyAdapter;
    private ProductDetailPresenter mProductDetailPresenter;

    private ArrayList<Integer> imageArr = new ArrayList<Integer>();
    private ArrayList<Specification> mSpecifications = new ArrayList<Specification>();

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
        mProductDetailPresenter.loadData();


        // Listener
        mImageBackShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductDetailActivity.this, ProductActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
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
                if (mExpandableOverview.isExpanded())
                {
                    mExpandableOverview.collapse();
                    mExpandOverview.setText("Mở rộng");
                }
                else
                {
                    mExpandableOverview.expand();
                    mExpandOverview.setText("Thu gọn");
                }
            }
        });

        mGoToStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductDetailActivity.this, StoreActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });

    }

    private void initView() {
        mSlideImage = (ViewPager) findViewById(R.id.vpg_slide);
        mIndicatorSlide = (CircleIndicator) findViewById(R.id.indicator_slide);
        mImageBackShop = (ImageView) findViewById(R.id.img_backShop);
        mRecyclerSpecificationLite = (RecyclerView) findViewById(R.id.rcv_specificationLite);
        mRecyclerPoclicyProduct = (RecyclerView) findViewById(R.id.rcv_poclicyProduct);
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
        mScrollViewProductDetail = (ScrollView) findViewById(R.id.scv_product_detail);
        mAppbarProductDetail = (AppBarLayout) findViewById(R.id.appbar_productDetail);
        mLinearPriceOnSale = (LinearLayout) findViewById(R.id.lnr_price_on_sales);

    }

    private void customToolbar() {
        mToolbarProductDetails = findViewById(R.id.toolbar_productDetails);
        setSupportActionBar(mToolbarProductDetails);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayShowTitleEnabled(false);
    }

    private void customExpandableOverview(){
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
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_bar, menu);
        menu.findItem(R.id.mnu_search).setVisible(false);
        menu.findItem(R.id.mnu_wishlist).setVisible(false);
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

        mRelativeCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProductDetailActivity.this, CartActivity.class));
            }
        });

        mScrollViewProductDetail.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY > mToolbarProductDetails.getHeight()){
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

    @Override
    public void initSlideImage(Integer[] productImages) {
        for (int i = 0; i < productImages.length; i++)
            imageArr.add(productImages[i]);

        mSlideImage.setAdapter(new SlideImageAdapter(ProductDetailActivity.this, imageArr));
        mIndicatorSlide.setViewPager(mSlideImage);

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

    @Override
    public void setProductName(String name) {
        mProductName.setText(name);
    }

    @Override
    public void setProductPrice(String price, int discount) {
        float basicPrice = Float.valueOf(price);
        if (discount > 0){
            float salePrice = basicPrice - (basicPrice * discount / 100);
            mPriceBasic.setText(String.format("%.3f", basicPrice));
            mPriceSale.setText(String.format("%.3f", salePrice));
            mLinearPriceOnSale.setVisibility(View.VISIBLE);
        } else {
            mPriceSale.setText(String.format("%.3f", basicPrice));
            mLinearPriceOnSale.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void setProductDiscount(int discount) {
        mProductDiscount.setText(String.valueOf(discount));
    }

    @Override
    public void setProductSpecification(List<Specification> specifications) {
        mSpecifications = (ArrayList<Specification>) specifications;
        mSpecificationLiteAdapter = new SpecificationAdapter(getSpecificationLite((ArrayList<Specification>) specifications));
        mLayoutManagerLite = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerSpecificationLite.setLayoutManager(mLayoutManagerLite);
        mRecyclerSpecificationLite.setAdapter(mSpecificationLiteAdapter);
    }

    @Override
    public void setProductOverviews(List<Overview> overviews) {
        StringBuilder builder = new StringBuilder();
        for (int i =0; i<overviews.size(); i++){
            if(!overviews.get(i).getTitle().isEmpty()){
                builder.append(overviews.get(i).getTitle()+"\n");
            }
            builder.append(overviews.get(i).getDescription()+"\n");
        }
        mExpandableOverview.setText(builder);
    }

    @Override
    public void setProductPolicies(List<Policy> policies) {
        mPolicyAdapter = new PolicyAdapter(policies);
        mPolicyLayoutManager =  new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerPoclicyProduct.setLayoutManager(mPolicyLayoutManager);
        mRecyclerPoclicyProduct.setAdapter(mPolicyAdapter);
    }

    @Override
    public void setStoreName(String storeName) {
        mStoreName.setText(storeName);
    }

    @Override
    public void setStoreLocation(String location) {
        mStoreLocation.setText(location);
    }
}
