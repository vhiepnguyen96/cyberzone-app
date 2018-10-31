package com.n8plus.vhiep.cyberzone.ui.manage.myorders.waitforpayment;

import com.n8plus.vhiep.cyberzone.base.BasePresenter;
import com.n8plus.vhiep.cyberzone.data.model.Order;
import com.n8plus.vhiep.cyberzone.ui.manage.myorders.MyOrderContract;

import java.util.List;

public interface WaitForPaymentContract {
    interface View {
        void setAdapterWaitForPayment(List<Order> waitForPaymentList);
        void setNotifyDataSetChanged();
    }
    interface Presenter extends BasePresenter<View> {
        void loadOrderWaitToPayment(String customerId);
    }
}
