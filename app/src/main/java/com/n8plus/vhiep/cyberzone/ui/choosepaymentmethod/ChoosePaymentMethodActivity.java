package com.n8plus.vhiep.cyberzone.ui.choosepaymentmethod;

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
import android.widget.AdapterView;
import android.widget.ListView;

import com.n8plus.vhiep.cyberzone.R;
import com.n8plus.vhiep.cyberzone.data.model.Order;
import com.n8plus.vhiep.cyberzone.data.model.PaymentMethod;
import com.n8plus.vhiep.cyberzone.ui.cart.CartActivity;
import com.n8plus.vhiep.cyberzone.ui.checkorder.CheckOrderActivity;
import com.n8plus.vhiep.cyberzone.ui.choosedeliveryaddress.ChooseDeliveryAddressActivity;
import com.n8plus.vhiep.cyberzone.ui.choosepaymentmethod.adapter.PaymentMethodAdapter;
import com.n8plus.vhiep.cyberzone.ui.payment.PaymentActivity;

import java.util.List;

public class ChoosePaymentMethodActivity extends AppCompatActivity implements ChoosePaymentMethodContract.View {
    ListView mListPaymentMethod;
    ChoosePaymentMethodPresenter mChoosePaymentMethodPresenter;
    PaymentMethodAdapter mPaymentMethodAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_payment_method_act);
        initView();

        // Custom
//        setBackgroundStatusBar();
        customToolbar();

        // Presenter
        mChoosePaymentMethodPresenter = new ChoosePaymentMethodPresenter(this);
        mChoosePaymentMethodPresenter.loadPaymentMethod();
        mChoosePaymentMethodPresenter.loadOrderState();

        Intent intent = getIntent();
        if (intent != null && intent.getSerializableExtra("order") != null){
            mChoosePaymentMethodPresenter.loadOrder((Order) intent.getSerializableExtra("order"));
        }

        // Listener
        mListPaymentMethod.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mChoosePaymentMethodPresenter.choosePaymentMethod(position);
            }
        });
    }

    private void initView(){
        mListPaymentMethod = (ListView) findViewById(R.id.lsv_payment_method);
    }

    public void setBackgroundStatusBar() {
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.parseColor("#008194"));
    }

    private void customToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar_payment_method);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
        actionbar.setDisplayShowTitleEnabled(true);
        actionbar.setTitle("Chọn hình thức thanh toán");
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setAdapterPaymentMethod(List<PaymentMethod> paymentMethods) {
        mPaymentMethodAdapter = new PaymentMethodAdapter(mListPaymentMethod.getContext(), R.layout.row_payment_method, paymentMethods);
        mListPaymentMethod.setAdapter(mPaymentMethodAdapter);
    }

    @Override
    public void moveToPayment(Order order) {
        Intent intent = new Intent(this, PaymentActivity.class);
        intent.putExtra("order", order);
        startActivity(intent);
    }
}
