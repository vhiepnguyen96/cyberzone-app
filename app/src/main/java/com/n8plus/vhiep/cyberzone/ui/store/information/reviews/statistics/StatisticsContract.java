package com.n8plus.vhiep.cyberzone.ui.store.information.reviews.statistics;

import com.n8plus.vhiep.cyberzone.base.BasePresenter;

public interface StatisticsContract {
    interface View{

    }
    interface Presenter extends BasePresenter<View>{
        void loadData();
    }
}
