package com.n8plus.vhiep.cyberzone.ui.manage.registersale;

import com.n8plus.vhiep.cyberzone.base.BasePresenter;

public interface RegisterSaleContract {
    interface View {

    }
    interface Presenter extends BasePresenter<View> {
        void loadData();
    }
}
