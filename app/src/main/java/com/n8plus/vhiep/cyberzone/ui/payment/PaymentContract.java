package com.n8plus.vhiep.cyberzone.ui.payment;

import com.n8plus.vhiep.cyberzone.base.BasePresenter;

public interface PaymentContract {
    interface View{
        void setTotalProduct(String totalProduct);
        void setSubtotalPrice(String subtotalPrice);
        void setShippingFee(String shippingFee);
        void setTotalPrice(String totalPrice);
    }
    interface Presenter extends BasePresenter<View>{

    }
}
