package com.n8plus.vhiep.cyberzone.ui.productdetails;

import com.n8plus.vhiep.cyberzone.base.BasePresenter;
import com.n8plus.vhiep.cyberzone.data.model.Overview;
import com.n8plus.vhiep.cyberzone.data.model.Policy;
import com.n8plus.vhiep.cyberzone.data.model.Product;
import com.n8plus.vhiep.cyberzone.data.model.Specification;
import com.n8plus.vhiep.cyberzone.data.model.Store;

import java.net.Socket;
import java.util.List;

public interface ProductDetailContract {
    interface View {
        void initSlideImage(Integer[] productImages);
        void setProductName(String name);
        void setProductPrice(String price, int discount);
        void setProductDiscount(int discount);
        void setProductSpecification(List<Specification> specifications);
        void setProductOverviews(List<Overview> overviews);
        void setProductPolicies(List<Policy> policies);
        void setStoreName(String storeName);
        void setStoreLocation(String location);
    }
    interface Presenter extends BasePresenter<View> {
        void loadData();
    }
}
