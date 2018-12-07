package com.n8plus.vhiep.cyberzone.ui.manage.myreview.notwritereview;

import com.n8plus.vhiep.cyberzone.base.BasePresenter;
import com.n8plus.vhiep.cyberzone.data.model.Order;

import java.util.List;

public interface NotWriteReviewContract {
    interface View {

    }
    interface Presenter extends BasePresenter<View> {
        void loadData();
    }
}
