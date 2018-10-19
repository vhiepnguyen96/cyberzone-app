package com.n8plus.vhiep.cyberzone.ui.manage.myreview.historyreview;

import com.n8plus.vhiep.cyberzone.base.BasePresenter;
import com.n8plus.vhiep.cyberzone.data.model.HistoryReview;
import com.n8plus.vhiep.cyberzone.data.model.Order;

import java.util.List;

public interface HistoryReviewContract {
    interface View {
        void setAdapterHistoryReview(List<HistoryReview> historyReviewList);
    }
    interface Presenter extends BasePresenter<View> {
        void loadData();
    }
}
