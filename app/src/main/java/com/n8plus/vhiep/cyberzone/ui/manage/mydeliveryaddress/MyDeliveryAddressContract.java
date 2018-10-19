package com.n8plus.vhiep.cyberzone.ui.manage.mydeliveryaddress;

import com.n8plus.vhiep.cyberzone.base.BasePresenter;
import com.n8plus.vhiep.cyberzone.data.model.Address;

import java.util.List;

public interface MyDeliveryAddressContract {
    interface View {
    }
    interface Presenter extends BasePresenter<View> {
        void loadData();
    }
}
