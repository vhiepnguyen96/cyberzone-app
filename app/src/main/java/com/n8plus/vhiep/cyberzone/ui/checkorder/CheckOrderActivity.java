package com.n8plus.vhiep.cyberzone.ui.checkorder;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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

import com.n8plus.vhiep.cyberzone.data.model.Address;
import com.n8plus.vhiep.cyberzone.data.model.DeliveryPrice;
import com.n8plus.vhiep.cyberzone.data.model.Order;
import com.n8plus.vhiep.cyberzone.data.model.PurchaseItem;
import com.n8plus.vhiep.cyberzone.ui.choosedeliveryaddress.ChooseDeliveryAddressActivity;
import com.n8plus.vhiep.cyberzone.ui.choosepaymentmethod.ChoosePaymentMethodActivity;
import com.n8plus.vhiep.cyberzone.ui.choosepaymentmethod.ChoosePaymentMethodPresenter;
import com.n8plus.vhiep.cyberzone.ui.login.LoginActivity;
import com.n8plus.vhiep.cyberzone.ui.manage.myorders.adapter.ProductOrderAdapter;
import com.n8plus.vhiep.cyberzone.util.Constant;
import com.n8plus.vhiep.cyberzone.util.UIUtils;
import com.n8plus.vhiep.cyberzone.ui.payment.PaymentActivity;
import com.n8plus.vhiep.cyberzone.ui.cart.CartActivity;
import com.n8plus.vhiep.cyberzone.R;

import java.util.List;

public class CheckOrderActivity extends AppCompatActivity implements CheckOrderContract.View {

    private ListView mListProductPayment;
    private TextView mEditAddress, mAddAddress, mNameCustomer, mPhoneCustomer, mAddressCustomer;
    private LinearLayout mLinearPayment, mLinearAddress;
    private ProductOrderAdapter mProductOrderAdapter;
    private CheckOrderPresenter mCheckOrderPresenter;
    private TextView mCountProduct, mTempPrice, mDeliveryPrice, mTotalPrice;
    private int REQUEST_CODE = 123;

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

        Intent intent = getIntent();
        if (intent != null && intent.getSerializableExtra("purchaseItems") != null && intent.getSerializableExtra("deliveryPrice") != null) {
            mCheckOrderPresenter.loadPurchaseList((List<PurchaseItem>) intent.getSerializableExtra("purchaseItems"));
            mCheckOrderPresenter.loadPrice(intent.getIntExtra("tempPrice", 0), (DeliveryPrice) intent.getSerializableExtra("deliveryPrice"));
            mCheckOrderPresenter.loadDeliveryAddressDefault(Constant.customer.getId());
        }

        // Listenner
        mLinearPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCheckOrderPresenter.prepareDataPayment();
            }
        });

        mEditAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(CheckOrderActivity.this, ChooseDeliveryAddressActivity.class), REQUEST_CODE);
            }
        });

        mAddAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(CheckOrderActivity.this, ChooseDeliveryAddressActivity.class), REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null && data.getSerializableExtra("address") != null){
                    mCheckOrderPresenter.loadDeliveryAddress((Address) data.getSerializableExtra("address"));
                } else {
                    mCheckOrderPresenter.loadDeliveryAddressDefault(Constant.customer.getId());
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {

            }
        }
        super.onActivityResult(requestCode, resultCode, data);
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

    public void initView() {
        mListProductPayment = (ListView) findViewById(R.id.lsv_productPayment);
        mLinearPayment = (LinearLayout) findViewById(R.id.lnr_payment);
        mLinearAddress = (LinearLayout) findViewById(R.id.lnr_address);
        mCountProduct = (TextView) findViewById(R.id.txt_productCount);
        mTempPrice = (TextView) findViewById(R.id.txt_tempPrice);
        mDeliveryPrice = (TextView) findViewById(R.id.txt_deliveryPrice);
        mTotalPrice = (TextView) findViewById(R.id.txt_totalPrice);
        mEditAddress = (TextView) findViewById(R.id.tv_edit_address);
        mAddAddress = (TextView) findViewById(R.id.tv_add_address);
        mNameCustomer = (TextView) findViewById(R.id.tv_name_customer_default);
        mPhoneCustomer = (TextView) findViewById(R.id.tv_phone_customer_default);
        mAddressCustomer = (TextView) findViewById(R.id.tv_address_customer_default);
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
    public void setAdapterOrder(List<PurchaseItem> purchaseItemList) {
        mProductOrderAdapter = new ProductOrderAdapter(mListProductPayment.getContext(), R.layout.row_product_order, purchaseItemList);
        mListProductPayment.setAdapter(mProductOrderAdapter);
        UIUtils.setListViewHeightBasedOnItems(mListProductPayment);
    }

    @Override
    public void showLayoutAddress(boolean b) {
        mLinearAddress.setVisibility(b ? View.VISIBLE : View.GONE);
    }

    @Override
    public void setCountProduct(String countProduct) {
        mCountProduct.setText(countProduct);
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
    public void setNameCustomer(String name) {
        mNameCustomer.setText(name);
    }

    @Override
    public void setPhoneCustomer(String phone) {
        mPhoneCustomer.setText(phone);
    }

    @Override
    public void setAddressCustomer(String address) {
        mAddressCustomer.setText(address);
    }

    @Override
    public void moveToChoosePaymentMethod(Order order) {
        Intent intent = new Intent(CheckOrderActivity.this, ChoosePaymentMethodActivity.class);
        intent.putExtra("order", order);
        startActivity(intent);
    }

    @Override
    public void showAlert(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Thông báo");
        builder.setMessage(message);
        builder.setCancelable(false);
        builder.setPositiveButton("Chấp nhận", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
