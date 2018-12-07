package com.n8plus.vhiep.cyberzone.ui.manage.mydeliveryaddress;

import android.content.Context;
import android.support.annotation.NonNull;

import com.n8plus.vhiep.cyberzone.data.model.Address;
import com.n8plus.vhiep.cyberzone.ui.manage.myprofile.MyProfileContract;

import java.util.ArrayList;
import java.util.List;

public class MyDeliveryAddressPresenter implements MyDeliveryAddressContract.Presenter {
    private Context context;
    private MyDeliveryAddressContract.View mDeliveryAddressView;
    private List<Address> addressList;

    public MyDeliveryAddressPresenter(@NonNull final Context context, @NonNull final MyDeliveryAddressContract.View mDeliveryAddressView) {
        this.context = context;
        this.mDeliveryAddressView = mDeliveryAddressView;
    }

    @Override
    public void loadData() {

    }

}
