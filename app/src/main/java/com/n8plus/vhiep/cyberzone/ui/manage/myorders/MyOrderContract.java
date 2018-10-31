package com.n8plus.vhiep.cyberzone.ui.manage.myorders;

import com.n8plus.vhiep.cyberzone.base.BasePresenter;
import com.n8plus.vhiep.cyberzone.data.model.Order;

import java.util.List;

public interface MyOrderContract {
    interface View {

    }
    interface Presenter extends BasePresenter<View> {
        void loadData();
    }
}
