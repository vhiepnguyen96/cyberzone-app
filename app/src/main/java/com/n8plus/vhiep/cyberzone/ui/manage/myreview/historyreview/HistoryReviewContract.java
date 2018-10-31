package com.n8plus.vhiep.cyberzone.ui.manage.myreview.historyreview;

import com.n8plus.vhiep.cyberzone.base.BasePresenter;
import com.n8plus.vhiep.cyberzone.data.model.HistoryReview;
import com.n8plus.vhiep.cyberzone.data.model.Order;
import com.n8plus.vhiep.cyberzone.data.model.ReviewProduct;
import com.n8plus.vhiep.cyberzone.data.model.ReviewStore;

import java.util.List;

public interface HistoryReviewContract {
    interface View {
        void setAdapterHistoryReview(List<HistoryReview> historyReviewList);
        void setNotifyDataSetChanged();
    }
    interface Presenter extends BasePresenter<View> {
        void loadData();
        void loadHistoryReview(String customerId);
        void setDataHistoryReview(List<ReviewProduct> reviewProducts, List<ReviewStore> reviewStores);
        void loadImageProduct(int position);
    }
}
