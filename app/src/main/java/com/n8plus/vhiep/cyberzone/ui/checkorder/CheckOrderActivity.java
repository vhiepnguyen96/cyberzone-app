package com.n8plus.vhiep.cyberzone.ui.checkorder;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
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
import com.n8plus.vhiep.cyberzone.ui.payment.PaymentActivity;
import com.n8plus.vhiep.cyberzone.ui.cart.CartActivity;
import com.n8plus.vhiep.cyberzone.ui.product.adapter.ProductVerticalAdapter;
import com.n8plus.vhiep.cyberzone.ui.cart.deliveryaddressdefault.DeliveryAddressDefaultFragment;
import com.n8plus.vhiep.cyberzone.data.model.Overview;
import com.n8plus.vhiep.cyberzone.data.model.Product;
import com.n8plus.vhiep.cyberzone.data.model.Specification;
import com.n8plus.vhiep.cyberzone.R;

import java.util.ArrayList;
import java.util.List;

public class CheckOrderActivity extends AppCompatActivity implements CheckOrderContract.View {

    private ListView mListProductPayment;
    private LinearLayout mLinearPayment;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private ProductVerticalAdapter mProductPaymentAdapter;
    private CheckOrderPresenter mCheckOrderPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.check_order_act);
        initView();

        // Custom
        setBackgroundStatusBar();
        customToolbar();

        // Presnter
        mCheckOrderPresenter = new CheckOrderPresenter(this);
        mCheckOrderPresenter.loadData();

        // Custom fragment
        fragmentManager = getFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.frm_deliveryAddress, new DeliveryAddressDefaultFragment());
        fragmentTransaction.commit();

        // Listenner
        mLinearPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CheckOrderActivity.this, PaymentActivity.class));
            }
        });
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
        actionbar.setTitle("Kiểm tra đơn hàng");
    }

    public void initView(){
        mListProductPayment = (ListView) findViewById(R.id.lsv_productPayment);
        mLinearPayment = (LinearLayout) findViewById(R.id.lnr_payment);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(CheckOrderActivity.this, CartActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setAdapterOrder(List<Product> products) {
        mProductPaymentAdapter = new ProductVerticalAdapter(mListProductPayment.getContext(), R.layout.row_product_in_check_order, products);
        mListProductPayment.setAdapter(mProductPaymentAdapter);
        UIUtils.setListViewHeightBasedOnItems(mListProductPayment);
    }
}
