package com.n8plus.vhiep.cyberzone.ui.payment;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.n8plus.vhiep.cyberzone.R;
import com.n8plus.vhiep.cyberzone.ui.checkorder.CheckOrderActivity;
import com.stripe.android.view.CardMultilineWidget;

public class PaymentActivity extends AppCompatActivity implements PaymentContract.View{
    private TextView mTotalProduct, mSubTotalPrice, mShippingFee, mTotalPrice;
    private CardMultilineWidget mCardStripe;
    private Button mPayment;
    private PaymentPresenter mPaymentPresenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment_act);
        initView();

        // Custom
        setBackgroundStatusBar();
        customToolbar();

        // Presenter
        mPaymentPresenter = new PaymentPresenter(this);
        mPaymentPresenter.loadData();


        // Listener
        mPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void initView(){
        mTotalProduct = (TextView) findViewById(R.id.tv_total_product);
        mSubTotalPrice = (TextView) findViewById(R.id.tv_subtotal_price);
        mShippingFee = (TextView) findViewById(R.id.tv_shipping_fee);
        mTotalPrice = (TextView) findViewById(R.id.tv_total_price);
        mCardStripe = (CardMultilineWidget) findViewById(R.id.card_stripe);
        mPayment = (Button) findViewById(R.id.btn_payment);
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
        actionbar.setTitle("Xác nhận thanh toán");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(PaymentActivity.this, CheckOrderActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setTotalProduct(String totalProduct) {
        mTotalProduct.setText(totalProduct);
    }

    @Override
    public void setSubtotalPrice(String subtotalPrice) {
        mSubTotalPrice.setText(subtotalPrice);
    }

    @Override
    public void setShippingFee(String shippingFee) {
        mShippingFee.setText(shippingFee);
    }

    @Override
    public void setTotalPrice(String totalPrice) {
        mTotalPrice.setText(totalPrice);
    }
}
