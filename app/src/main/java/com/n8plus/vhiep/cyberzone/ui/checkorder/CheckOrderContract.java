package com.n8plus.vhiep.cyberzone.ui.checkorder;

import com.n8plus.vhiep.cyberzone.base.BasePresenter;
import com.n8plus.vhiep.cyberzone.data.model.Address;
import com.n8plus.vhiep.cyberzone.data.model.DeliveryPrice;
import com.n8plus.vhiep.cyberzone.data.model.Order;
import com.n8plus.vhiep.cyberzone.data.model.Product;
import com.n8plus.vhiep.cyberzone.data.model.PurchaseItem;

import java.util.List;

public interface CheckOrderContract {
    interface View{
        void setAdapterOrder(List<PurchaseItem> purchaseItemList);
        void showLayoutAddress(boolean b);
        void setCountProduct(String countProduct);
        void setTempPrice(String tempPrice);
        void setDeliveryPrice(String deliveryPrice);
        void setTotalPrice(String totalPrice);
        void setNameCustomer(String name);
        void setPhoneCustomer(String phone);
        void setAddressCustomer(String address);
        void moveToChoosePaymentMethod(Order order);
        void showAlert(String message);
    }
    interface Presenter extends BasePresenter<View>{
        void loadPurchaseList(List<PurchaseItem> purchaseItems);
        void loadPrice(int tempPrice, DeliveryPrice deliveryPrice);
        void loadDeliveryAddress(Address address);
        void loadDeliveryAddressDefault(String customerId);
        void prepareDataPayment();
    }
}
