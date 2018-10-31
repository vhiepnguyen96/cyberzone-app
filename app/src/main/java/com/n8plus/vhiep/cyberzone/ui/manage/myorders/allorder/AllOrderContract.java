package com.n8plus.vhiep.cyberzone.ui.manage.myorders.allorder;

import com.n8plus.vhiep.cyberzone.base.BasePresenter;
import com.n8plus.vhiep.cyberzone.data.model.Order;

import java.util.List;

public interface AllOrderContract {
    interface View {
        void setAdapterAllOrder(List<Order> orderList);
        void setNotifyDataSetChanged();
    }
    interface Presenter extends BasePresenter<View> {
        void loadAllOrder(String customerId);
    }
}
