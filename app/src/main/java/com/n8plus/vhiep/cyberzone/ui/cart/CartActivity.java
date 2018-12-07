package com.n8plus.vhiep.cyberzone.ui.cart;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.n8plus.vhiep.cyberzone.data.model.DeliveryPrice;
import com.n8plus.vhiep.cyberzone.data.model.PurchaseItem;
import com.n8plus.vhiep.cyberzone.ui.login.LoginActivity;
import com.n8plus.vhiep.cyberzone.ui.manage.myorders.adapter.ProductOrderAdapter;
import com.n8plus.vhiep.cyberzone.util.Constant;
import com.n8plus.vhiep.cyberzone.util.UIUtils;
import com.n8plus.vhiep.cyberzone.ui.checkorder.CheckOrderActivity;
import com.n8plus.vhiep.cyberzone.ui.product.adapter.ProductVerticalAdapter;
import com.n8plus.vhiep.cyberzone.data.model.Overview;
import com.n8plus.vhiep.cyberzone.data.model.Product;
import com.n8plus.vhiep.cyberzone.data.model.Specification;
import com.n8plus.vhiep.cyberzone.R;
import com.n8plus.vhiep.cyberzone.ui.home.HomeActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity implements CartContract.View {
    private ListView mListViewCart;
    private LinearLayout mLinearCheckOrder;
    private ProductOrderAdapter mProductInCart;
    private CartPresenter mCartPresenter;
    private TextView mTotalPrice, mDeliveryPrice, mTempPrice, mProductCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart_act);
        initView();

        // Custom
        customToolbar();

        // Presenter
        mCartPresenter = new CartPresenter(getApplicationContext(), this);
        mCartPresenter.loadData();

        // Listener
        mLinearCheckOrder.setOnClickListener(v -> mCartPresenter.orderNow());
    }

    public void initView() {
        mListViewCart = (ListView) findViewById(R.id.lsv_cart);
        mLinearCheckOrder = (LinearLayout) findViewById(R.id.lnrCkeckOrder);
        mProductCount = (TextView) findViewById(R.id.txt_productCount);
        mTempPrice = (TextView) findViewById(R.id.txt_tempPrice);
        mDeliveryPrice = (TextView) findViewById(R.id.txt_deliveryPrice);
        mTotalPrice = (TextView) findViewById(R.id.txt_totalPrice);
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
    public void setAdapterCart(List<PurchaseItem> purchaseItemList) {
        mProductInCart = new ProductOrderAdapter(mListViewCart.getContext(), R.layout.row_product_in_cart, purchaseItemList);
        mListViewCart.setAdapter(mProductInCart);
        UIUtils.setListViewHeightBasedOnItems(mListViewCart);
    }

    @Override
    public void setProductCount(String productCount) {
        mProductCount.setText(productCount);
    }

    @Override
    public void setTempPrice(String tempPrice) {
        mTempPrice.setText(tempPrice);
    }

    @Override
    public void setDeliveryPrice(String deliveryPrice) {
        mDeliveryPrice.setText(deliveryPrice);
    }

    @Override
    public void setTotalPrice(String totalPrice) {
        mTotalPrice.setText(totalPrice);
    }

    @Override
    public void setCartNone(boolean b) {
        findViewById(R.id.lnr_cart_none).setVisibility(b ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public void moveToCheckOrder(List<PurchaseItem> purchaseItems, int tempPrice, DeliveryPrice deliveryPrice) {
        if (purchaseItems.size() > 0) {
            if (Constant.customer != null) {
                Intent intent = new Intent(CartActivity.this, CheckOrderActivity.class);
                intent.putExtra("purchaseItems", (Serializable) purchaseItems);
                intent.putExtra("tempPrice", tempPrice);
                intent.putExtra("deliveryPrice", deliveryPrice);
                startActivity(intent);
            } else {
                showRequireLogin();
            }
        } else {
            Toast.makeText(this, "Vui lòng kiểm tra lại giỏ hàng!", Toast.LENGTH_SHORT).show();
        }
    }

    public void updateCart() {
        mCartPresenter.loadData();
    }

    public void showRequireLogin() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Thông báo");
        builder.setMessage("Bạn cần đăng nhập để mua hàng !");
        builder.setCancelable(false);
        builder.setPositiveButton("Đăng nhập ngay", (dialogInterface, i) -> {
            startActivity(new Intent(CartActivity.this, LoginActivity.class));
            dialogInterface.dismiss();
            finish();
        });
        builder.setNegativeButton("Hủy", (dialogInterface, i) -> dialogInterface.dismiss());
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
