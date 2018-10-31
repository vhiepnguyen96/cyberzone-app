package com.n8plus.vhiep.cyberzone.ui.payment;

import com.n8plus.vhiep.cyberzone.base.BasePresenter;
import com.n8plus.vhiep.cyberzone.data.model.Address;
import com.n8plus.vhiep.cyberzone.data.model.Order;
import com.stripe.android.model.Token;

public interface PaymentContract {
    interface View{
        void setLayoutPayemnt(String payemntMethod);
        void setOrderId(String orderId);
        void setCountProduct(String countProduct);
        void setTempPrice(String tempPrice);
        void setDeliveryPrice(String deliveryPrice);
        void setTotalPrice(String totalPrice);
        void setTotalPriceUSD(String totalPriceUSD);
        void setDeliveryAddress(Address deliveryAddress);
    }
    interface Presenter extends BasePresenter<View>{
        void loadDataPayment(Order order);
        void chectOut(Token token);
        void confirmOrder();
    }
}
