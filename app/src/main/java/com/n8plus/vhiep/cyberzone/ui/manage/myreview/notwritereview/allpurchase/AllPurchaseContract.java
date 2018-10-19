package com.n8plus.vhiep.cyberzone.ui.manage.myreview.notwritereview.allpurchase;

import com.n8plus.vhiep.cyberzone.base.BasePresenter;
import com.n8plus.vhiep.cyberzone.data.model.Order;

import java.util.List;

public interface AllPurchaseContract {
    interface View {
        void setAdapterAllPurchase(List<Order> orderList);
    }
    interface Presenter extends BasePresenter<View> {
        void loadData();
    }
}
