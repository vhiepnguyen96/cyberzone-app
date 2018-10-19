package com.n8plus.vhiep.cyberzone.ui.store.information.reviews.listreview;

import com.n8plus.vhiep.cyberzone.base.BasePresenter;
import com.n8plus.vhiep.cyberzone.data.model.ReviewStore;

import java.util.List;

public interface ListReviewContract {
    interface View{
        void setAdapterCustomerReview(List<ReviewStore> reviewStoreList);
    }
    interface Presenter extends BasePresenter<View>{
        void loadData();
    }
}
