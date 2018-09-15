package com.n8plus.vhiep.cyberzone.ui.product;

import com.bignerdranch.expandablerecyclerview.Model.ParentObject;
import com.n8plus.vhiep.cyberzone.base.BasePresenter;
import com.n8plus.vhiep.cyberzone.base.BaseView;
import com.n8plus.vhiep.cyberzone.data.model.Product;

import java.util.ArrayList;
import java.util.List;

public interface ProductContract {
    interface View {
        void setAdapterProduct(List<Product> products);
        void generateFilters(ArrayList<ParentObject> parentObjects);
    }
    interface Presenter extends BasePresenter<View> {
        void loadData();
    }
}
