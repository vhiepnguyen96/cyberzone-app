package com.n8plus.vhiep.cyberzone.ui.manage.mainmanage;

import com.n8plus.vhiep.cyberzone.base.BasePresenter;

public interface MainManageContract {
    interface View {

    }
    interface Presenter extends BasePresenter<View> {
        void loadData();
    }
}
