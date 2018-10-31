package com.n8plus.vhiep.cyberzone.ui.store.information;

import com.n8plus.vhiep.cyberzone.base.BasePresenter;
import com.n8plus.vhiep.cyberzone.data.model.Category;
import com.n8plus.vhiep.cyberzone.data.model.ReviewStore;
import com.n8plus.vhiep.cyberzone.data.model.Store;

import java.util.List;

public interface InformationContract {
    interface View{
        void setStoreLocation(String storeLocation);
        void setTimeInSystem(String timeInSystem);
        void setPopularCategory(String popularCategory);
        void setAdapterCustomerReview(List<ReviewStore> reviewStoreList);
        void setPositiveReview(String positiveReview);
        void setTotalReview(String totalReview);
        void setGoodReview(int totalReview, int goodReview);
        void setNormalReview(int totalReview, int normalReview);
        void setNotGoodReview(int totalReview, int notGoodReview);
        void setCountMoreReview(String count);
        void alertNoMoreReview();
        void showMoreReview(List<ReviewStore> reviewStores);
    }
    interface Presenter extends BasePresenter<View> {
        void loadData(Store store);
        void loadStoreInfomation(String storeId);
        void loadStoreReviews(String storeId);
        void loadStoreReviews(int limit);
        List<ReviewStore> getLimitStoreReviews(int limit);
        void prepareDataMoreReview();
        String getPopularCategory(List<Category> categories);
    }
}
