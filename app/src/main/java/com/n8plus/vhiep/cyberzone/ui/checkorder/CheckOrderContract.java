package com.n8plus.vhiep.cyberzone.ui.checkorder;

import com.n8plus.vhiep.cyberzone.base.BasePresenter;
import com.n8plus.vhiep.cyberzone.data.model.Product;

import java.util.List;

public interface CheckOrderContract {
    interface View{
        void setAdapterOrder(List<Product> products);
    }
    interface Presenter extends BasePresenter<View>{

    }
}
