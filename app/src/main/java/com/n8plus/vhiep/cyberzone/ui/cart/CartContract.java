package com.n8plus.vhiep.cyberzone.ui.cart;

import com.n8plus.vhiep.cyberzone.base.BasePresenter;
import com.n8plus.vhiep.cyberzone.data.model.Product;

import java.util.List;

public interface CartContract {
    interface View{
        void setAdapterCart(List<Product> products);
    }
    interface Presenter extends BasePresenter<View> {

    }
}
