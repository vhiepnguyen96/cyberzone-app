package com.n8plus.vhiep.cyberzone.ui.product;

import com.bignerdranch.expandablerecyclerview.Model.ParentObject;
import com.n8plus.vhiep.cyberzone.base.BasePresenter;
import com.n8plus.vhiep.cyberzone.base.BaseView;
import com.n8plus.vhiep.cyberzone.data.model.Filter;
import com.n8plus.vhiep.cyberzone.data.model.Product;

import java.util.ArrayList;
import java.util.List;

public interface ProductContract {
    interface View {
        void setAdapterProduct(List<Product> products, String layout);
        void setNotifyDataSetChanged();
        void generateFilters(ArrayList<ParentObject> parentObjects);
        void setCartMenuItem();
        void setKeyword(String keyword);
    }
    interface Presenter extends BasePresenter<View> {
        void loadProductDefault();
        void loadProductByList(List<Product> productList);
        void loadProductByProductType(String productTypeId);
        void loadProductByKeyWord(String keyword);
        void changeProductGridLayout();
        void changeProductLinearLayout();
        void resetFilter();
    }
}
