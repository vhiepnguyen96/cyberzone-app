package com.n8plus.vhiep.cyberzone.ui.manage.myreview;

import com.n8plus.vhiep.cyberzone.base.BasePresenter;

public interface MyReviewContract {
    interface View {

    }
    interface Presenter extends BasePresenter<View> {
        void loadData();
    }
}
