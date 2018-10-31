package com.n8plus.vhiep.cyberzone.ui.store.homepage;

import com.n8plus.vhiep.cyberzone.base.BasePresenter;
import com.n8plus.vhiep.cyberzone.data.model.Product;
import com.n8plus.vhiep.cyberzone.data.model.Store;

import java.util.List;

public interface HomePageContract {
    interface View{
        void setAdapterProduct(List<Product> products, String layout);
        void setNotifyDataSetChanged();
    }
    interface Presenter extends BasePresenter<View>{
        void loadData(Store store);
        void loadStoreProduct(String storeId);
        void changeProductGridLayout();
        void changeProductLinearLayout();
    }
}
