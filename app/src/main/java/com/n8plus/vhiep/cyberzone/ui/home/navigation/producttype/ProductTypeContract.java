package com.n8plus.vhiep.cyberzone.ui.home.navigation.producttype;

import android.widget.BaseAdapter;
import android.widget.ListView;

import com.n8plus.vhiep.cyberzone.base.BasePresenter;
import com.n8plus.vhiep.cyberzone.base.BaseView;
import com.n8plus.vhiep.cyberzone.data.model.Product;
import com.n8plus.vhiep.cyberzone.data.model.ProductType;

import java.util.List;

public interface ProductTypeContract {
    interface View {
        void setAdapterProductType(List<ProductType> productTypes);
    }
    interface Presenter extends BasePresenter<View> {
        void loadData();
    }
}
