package com.n8plus.vhiep.cyberzone.ui.store;

import com.n8plus.vhiep.cyberzone.base.BasePresenter;
import com.n8plus.vhiep.cyberzone.data.model.Store;

public interface StoreContract {
    interface View{
        void setStoreName(String storeName);
        void setPositiveReview(String positiveReview);
        void setFollowStoreResult(boolean b);
        void setFollowCounter(String followCounter);
        void followStoreResult(boolean b);
        void unfollowStoreResult(boolean b);
    }
    interface Presenter extends BasePresenter<View> {
        void loadData(Store store);
        void loadFollowStore(String storeId);
        void checkFollowStore(String storeId, String customerId);
        void loadStoreReviews(String storeId);
        void followStore();
        void unfollowStore();
    }
}
