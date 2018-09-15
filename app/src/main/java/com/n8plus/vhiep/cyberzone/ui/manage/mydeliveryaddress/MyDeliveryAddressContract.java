package com.n8plus.vhiep.cyberzone.ui.manage.mydeliveryaddress;

import com.n8plus.vhiep.cyberzone.base.BasePresenter;

public interface MyDeliveryAddressContract {
    interface View {

    }
    interface Presenter extends BasePresenter<View> {
        void loadData();
    }
}
