package com.n8plus.vhiep.cyberzone.ui.manage.myorders;

import com.n8plus.vhiep.cyberzone.base.BasePresenter;

public interface MyOrderContract {
    interface View {

    }
    interface Presenter extends BasePresenter<View> {
        void loadData();
    }
}
