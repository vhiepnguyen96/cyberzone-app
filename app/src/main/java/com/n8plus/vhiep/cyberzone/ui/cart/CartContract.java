package com.n8plus.vhiep.cyberzone.ui.cart;

import com.n8plus.vhiep.cyberzone.base.BasePresenter;
import com.n8plus.vhiep.cyberzone.data.model.DeliveryPrice;
import com.n8plus.vhiep.cyberzone.data.model.Product;
import com.n8plus.vhiep.cyberzone.data.model.PurchaseItem;

import java.util.List;

public interface CartContract {
    interface View{
        void setAdapterCart(List<PurchaseItem> purchaseItemList);
        void setProductCount(String productCount);
        void setTempPrice(String tempPrice);
        void setDeliveryPrice(String deliveryPrice);
        void setTotalPrice(String totalPrice);
        void setCartNone(boolean b);
        void moveToCheckOrder(List<PurchaseItem> purchaseItems, int tempPrice, DeliveryPrice deliveryPrice);
    }
    interface Presenter extends BasePresenter<View> {
        void loadData();
        void orderNow();
    }
}
