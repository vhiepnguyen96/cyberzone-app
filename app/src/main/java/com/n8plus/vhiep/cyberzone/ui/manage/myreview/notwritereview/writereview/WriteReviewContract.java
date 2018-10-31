package com.n8plus.vhiep.cyberzone.ui.manage.myreview.notwritereview.writereview;

import com.n8plus.vhiep.cyberzone.base.BasePresenter;
import com.n8plus.vhiep.cyberzone.data.model.Product;
import com.n8plus.vhiep.cyberzone.data.model.PurchaseItem;
import com.n8plus.vhiep.cyberzone.data.model.ReviewProduct;

public interface WriteReviewContract {
    interface View {
        void setProductName(String productName);

        void setStoreName(String storeName);

        void setCustomerName(String customerName);

        void sendReviewResult(boolean b);

        int getLevelRadioGroup(int id);

        boolean checkIsValidReview();

        void setAlert(String message);
    }

    interface Presenter extends BasePresenter<View> {
        void loadDataReview(PurchaseItem orderItem);

        void loadRatingStar();

        void loadRatingLevel();

        void sendReview(String reviewProduct, int rateProductStar, String reviewStore, int rateStoreLevel);

        String getIdRatingStar(int star);

        String getIdRatingLevel(int level);

        void updateIsReviewOrderItem(String orderItemId);
    }
}
