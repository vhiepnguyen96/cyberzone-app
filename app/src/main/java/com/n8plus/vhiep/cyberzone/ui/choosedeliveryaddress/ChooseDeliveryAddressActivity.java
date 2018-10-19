package com.n8plus.vhiep.cyberzone.ui.choosedeliveryaddress;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.n8plus.vhiep.cyberzone.ui.checkorder.CheckOrderActivity;
import com.n8plus.vhiep.cyberzone.ui.manage.mydeliveryaddress.deliveryaddress.DeliveryAddressFragment;
import com.n8plus.vhiep.cyberzone.R;
import com.n8plus.vhiep.cyberzone.ui.manage.mydeliveryaddress.loaddeliveryaddress.LoadDeliveryAddressFragment;

public class ChooseDeliveryAddressActivity extends AppCompatActivity {
    public FragmentManager fragmentManagerDelivery;
    public FragmentTransaction fragmentTransactionDelivery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delivery_address_act);
        customToolbar();

        fragmentManagerDelivery = getSupportFragmentManager();
        fragmentTransactionDelivery = fragmentManagerDelivery.beginTransaction();
        fragmentTransactionDelivery.add(R.id.frm_choose_delivery_address, new DeliveryAddressFragment());
        fragmentTransactionDelivery.commit();
    }

    private void customToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar_choose_delivery_address);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
        actionbar.setDisplayShowTitleEnabled(true);
        actionbar.setTitle("Chọn địa chỉ giao hàng");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                ActionBar actionbar = getSupportActionBar();
                String title = (String) actionbar.getTitle();
                if (title.equals("Chọn địa chỉ giao hàng")){
                    Intent intent = new Intent(ChooseDeliveryAddressActivity.this, CheckOrderActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
