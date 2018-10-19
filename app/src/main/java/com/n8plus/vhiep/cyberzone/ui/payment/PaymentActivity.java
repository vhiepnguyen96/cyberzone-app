package com.n8plus.vhiep.cyberzone.ui.payment;

import android.app.Activity;
import android.content.Intent;
import android.database.Observable;
import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.n8plus.vhiep.cyberzone.R;
import com.n8plus.vhiep.cyberzone.ui.checkorder.CheckOrderActivity;
import com.n8plus.vhiep.cyberzone.util.Constant;
import com.n8plus.vhiep.cyberzone.util.ErrorDialogHandler;
import com.n8plus.vhiep.cyberzone.util.MySingleton;
import com.stripe.android.PaymentConfiguration;
import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.exception.APIConnectionException;
import com.stripe.android.exception.APIException;
import com.stripe.android.exception.AuthenticationException;
import com.stripe.android.exception.CardException;
import com.stripe.android.exception.InvalidRequestException;
import com.stripe.android.model.Card;
import com.stripe.android.model.Source;
import com.stripe.android.model.SourceParams;
import com.stripe.android.model.Token;
import com.stripe.android.view.CardMultilineWidget;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;


public class PaymentActivity extends AppCompatActivity implements PaymentContract.View {
    private TextView mTotalProduct, mSubTotalPrice, mShippingFee, mTotalPrice;
    private CardMultilineWidget mCardInputWidget;
    private Button mPayment;
    private PaymentPresenter mPaymentPresenter;
    ErrorDialogHandler mErrorDialogHandler;
    private Stripe mStripe;
    private Card mCard;
    private Integer mAmount;
    private String mName;
    private Token mToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment_act);
        initView();

        // Custom
        setBackgroundStatusBar();
        customToolbar();
        mErrorDialogHandler = new ErrorDialogHandler(getSupportFragmentManager());

        // Presenter
        mPaymentPresenter = new PaymentPresenter(this);

        Intent intent = getIntent();
        if (intent != null) {
            mPaymentPresenter.loadDataPayment(intent.getStringExtra("countProduct"), intent.getIntExtra("tempPrice", 0), intent.getIntExtra("deliveryPrice", 0));
        }

        // Listener
        mPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitCard();
            }
        });
    }

    public void submitCard() {
        Card cardToSave = mCardInputWidget.getCard();

//        Card cardToSave = new Card("4242424242424242", 01, 19, "444");

        if (cardToSave == null) {
            mErrorDialogHandler.showError("Invalid Card Data");
            return;
        }

        mStripe = new Stripe(getApplicationContext());
        mStripe.createToken(cardToSave, "pk_test_CeyyXLIDl0bfY9IiYwTIYZAU", new TokenCallback() {
            @Override
            public void onError(Exception error) {
                Toast.makeText(getApplicationContext(),
                        error.getLocalizedMessage(),
                        Toast.LENGTH_LONG).show();
            }

            @Override
            public void onSuccess(Token token) {
                Log.i("Token", token.getId());
                Toast.makeText(getApplicationContext(), "Token: "+token.getId(), Toast.LENGTH_SHORT).show();
                mPaymentPresenter.chectOut(token);
            }
        });

//        mStripe.createToken(
//                cardToSave,
//                new TokenCallback() {
//                    @Override
//                    public void onError(Exception error) {
//                        Toast.makeText(getApplicationContext(),
//                                error.getLocalizedMessage(),
//                                Toast.LENGTH_LONG).show();
//                    }
//
//                    @Override
//                    public void onSuccess(Token token) {
//                        Toast.makeText(getApplicationContext(), "Token: "+token.getId(), Toast.LENGTH_SHORT).show();
//                        mPaymentPresenter.chectOut(token);
//                    }
//                }
//        );
    }

    private void initView() {
        mTotalProduct = (TextView) findViewById(R.id.tv_total_product);
        mSubTotalPrice = (TextView) findViewById(R.id.tv_subtotal_price);
        mShippingFee = (TextView) findViewById(R.id.tv_shipping_fee);
        mTotalPrice = (TextView) findViewById(R.id.tv_total_price);
        mCardInputWidget = (CardMultilineWidget) findViewById(R.id.card_stripe);
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
    public void setCountProduct(String countProduct) {
        mTotalProduct.setText(countProduct);
    }

    @Override
    public void setTempPrice(String tempPrice) {
        mSubTotalPrice.setText(tempPrice);
    }

    @Override
    public void setDeliveryPrice(String deliveryPrice) {
        mShippingFee.setText(deliveryPrice);
    }

    @Override
    public void setTotalPrice(String totalPrice) {
        mTotalPrice.setText(totalPrice);
    }
}
