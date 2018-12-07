package com.n8plus.vhiep.cyberzone.ui.manage.myreview.notwritereview.allpurchase;

import com.n8plus.vhiep.cyberzone.base.BasePresenter;
import com.n8plus.vhiep.cyberzone.data.model.Order;
import com.n8plus.vhiep.cyberzone.data.model.Product;
import com.n8plus.vhiep.cyberzone.data.model.PurchaseItem;

import java.util.Date;
import java.util.List;

public interface AllPurchaseContract {
    interface View {
        void setLayoutNone(boolean b);
        void setLayoutLoading(boolean b);
        void setAdapterAllPurchase(List<PurchaseItem> purchaseItemList, List<Date> datePurchaseList);
        void setNotifyDataSetChanged();
        void writeReview(int position);
        void moveToWriteReview(PurchaseItem purchaseItem);
    }
    interface Presenter extends BasePresenter<View> {
        void loadData();
        void loadOrderState();
        void loadOrderPurchased(String customerId, String orderStateId);
        void loadOrderItems(int position);
        boolean checkProductIsExists(PurchaseItem orderItem);
        void setDataAdapterReview(List<Order> orderList);
        void prepareDataWriteReview(int position);
    }
}
