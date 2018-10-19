package com.n8plus.vhiep.cyberzone.ui.store.homepage;

import com.n8plus.vhiep.cyberzone.base.BasePresenter;
import com.n8plus.vhiep.cyberzone.data.model.Product;

import java.util.List;

public interface HomePageContract {
    interface View{
        void setAdapterProduct(List<Product> products);
    }
    interface Presenter extends BasePresenter<View>{
        void loadData();
    }
}
