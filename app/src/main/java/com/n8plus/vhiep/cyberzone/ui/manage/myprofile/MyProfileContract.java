package com.n8plus.vhiep.cyberzone.ui.manage.myprofile;

import com.n8plus.vhiep.cyberzone.base.BasePresenter;

public interface MyProfileContract {
    interface View {

    }
    interface Presenter extends BasePresenter<View> {
        void loadData();
    }
}
