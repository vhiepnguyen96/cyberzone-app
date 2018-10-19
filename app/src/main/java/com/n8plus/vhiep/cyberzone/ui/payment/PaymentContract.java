package com.n8plus.vhiep.cyberzone.ui.payment;

import com.n8plus.vhiep.cyberzone.base.BasePresenter;
import com.stripe.android.model.Token;

public interface PaymentContract {
    interface View{
        void setCountProduct(String countProduct);
        void setTempPrice(String tempPrice);
        void setDeliveryPrice(String deliveryPrice);
        void setTotalPrice(String totalPrice);
    }
    interface Presenter extends BasePresenter<View>{
        void loadDataPayment(String countProduct, int tempPrice, int deliveryPrice);
        void chectOut(Token token);
    }
}
