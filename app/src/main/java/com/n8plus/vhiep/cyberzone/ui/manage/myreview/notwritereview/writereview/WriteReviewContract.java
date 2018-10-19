package com.n8plus.vhiep.cyberzone.ui.manage.myreview.notwritereview.writereview;

import com.n8plus.vhiep.cyberzone.base.BasePresenter;

public interface WriteReviewContract {
    interface View {
//        void setAdapterAllOrder(List<Order> orderList);
    }
    interface Presenter extends BasePresenter<View> {
        void loadData();
    }
}
