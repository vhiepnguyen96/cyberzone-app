package com.n8plus.vhiep.cyberzone.ui.manage.mydeliveryaddress;

import com.n8plus.vhiep.cyberzone.data.model.Address;
import com.n8plus.vhiep.cyberzone.ui.manage.myprofile.MyProfileContract;

import java.util.ArrayList;
import java.util.List;

public class MyDeliveryAddressPresenter implements MyDeliveryAddressContract.Presenter {
    private MyDeliveryAddressContract.View mDeliveryAddressView;
    private List<Address> addressList;

    public MyDeliveryAddressPresenter(MyDeliveryAddressContract.View mDeliveryAddressView) {
        this.mDeliveryAddressView = mDeliveryAddressView;
    }

    @Override
    public void loadData() {

    }

}
