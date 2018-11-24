package com.n8plus.vhiep.cyberzone.ui.choosepaymentmethod;

import com.n8plus.vhiep.cyberzone.base.BasePresenter;
import com.n8plus.vhiep.cyberzone.data.model.Order;
import com.n8plus.vhiep.cyberzone.data.model.OrderState;
import com.n8plus.vhiep.cyberzone.data.model.PaymentMethod;

import java.util.List;

public interface ChoosePaymentMethodContract {
    interface View {
        void setAdapterPaymentMethod(List<PaymentMethod> paymentMethods);
        void moveToPayment(Order order, List<OrderState> orderStates);
        void saveOrderResult(boolean b);
    }
    interface Presenter extends BasePresenter<View> {
        void loadPaymentMethod();
        void loadOrderState();
        void loadOrder(Order order);
        void choosePaymentMethod(int position);
        void saveOrder();
        void saveOrderItems(String orderId);
        void updateQuantityProduct(String productId, int quantity);
    }
}
