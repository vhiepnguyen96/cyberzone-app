package com.n8plus.vhiep.cyberzone.ui.cart;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.n8plus.vhiep.cyberzone.util.UIUtils;
import com.n8plus.vhiep.cyberzone.ui.checkorder.CheckOrderActivity;
import com.n8plus.vhiep.cyberzone.ui.product.adapter.ProductVerticalAdapter;
import com.n8plus.vhiep.cyberzone.data.model.Overview;
import com.n8plus.vhiep.cyberzone.data.model.Product;
import com.n8plus.vhiep.cyberzone.data.model.Specification;
import com.n8plus.vhiep.cyberzone.R;
import com.n8plus.vhiep.cyberzone.ui.home.HomeActivity;

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity implements CartContract.View {
    private ListView mListViewCart;
    private LinearLayout mLinearCheckOrder;
    private ProductVerticalAdapter mProductInCart;
    private CartPresenter mCartPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart_act);
        initView();

        // Custom
        setBackgroundStatusBar();
        customToolbar();

        // Presenter
        mCartPresenter = new CartPresenter(this);
        mCartPresenter.loadData();

        // Listener
        mLinearCheckOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CartActivity.this, CheckOrderActivity.class));
            }
        });
    }

    public void initView(){
        mListViewCart = (ListView) findViewById(R.id.lsv_cart);
        mLinearCheckOrder = (LinearLayout) findViewById(R.id.lnrCkeckOrder);
    }

    public void setBackgroundStatusBar() {
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.parseColor("#008194"));
    }

    private void customToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar_cart);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
        actionbar.setDisplayShowTitleEnabled(true);
        actionbar.setTitle("Giỏ hàng của tôi");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(CartActivity.this, HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setAdapterCart(List<Product> products) {
        mProductInCart = new ProductVerticalAdapter(mListViewCart.getContext(), R.layout.row_product_in_cart, products);
        mListViewCart.setAdapter(mProductInCart);
        UIUtils.setListViewHeightBasedOnItems(mListViewCart);
    }
}
