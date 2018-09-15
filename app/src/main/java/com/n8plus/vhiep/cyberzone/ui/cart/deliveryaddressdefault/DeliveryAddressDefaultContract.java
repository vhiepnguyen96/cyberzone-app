package com.n8plus.vhiep.cyberzone.ui.cart.deliveryaddressdefault;

import com.n8plus.vhiep.cyberzone.base.BasePresenter;

public interface DeliveryAddressDefaultContract {
    interface View {
        void setNameCustomer(String name);
        void setPhoneCustomer(String phone);
        void setAddressCustomer(String address);
    }
    interface Presenter extends BasePresenter<View>{

    }
}
