package com.n8plus.vhiep.cyberzone.ui.payment;

import com.n8plus.vhiep.cyberzone.base.BasePresenter;
import com.n8plus.vhiep.cyberzone.data.model.Address;
import com.n8plus.vhiep.cyberzone.data.model.Order;
import com.n8plus.vhiep.cyberzone.data.model.OrderState;
import com.stripe.android.model.Card;
import com.stripe.android.model.Source;
import com.stripe.android.model.Token;

import java.util.List;

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
        void moveToHome();
        void setPaymentResult(boolean b);
        void updateOrderStateResult(boolean b);
        void showProgressDialog();
        void hideProgressDialog();
    }
    interface Presenter extends BasePresenter<View>{
        void loadDataPayment(Order order);
        void loadOrderState();
        void loadOrderState(List<OrderState> orderStates);
        void chectOut(Token token);
        void confirmOrder();
        void changeOrderState(Order order);
    }
}
