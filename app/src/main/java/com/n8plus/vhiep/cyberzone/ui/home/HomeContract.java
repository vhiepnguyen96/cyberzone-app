package com.n8plus.vhiep.cyberzone.ui.home;

import android.support.v7.widget.RecyclerView;

import com.n8plus.vhiep.cyberzone.base.BasePresenter;
import com.n8plus.vhiep.cyberzone.base.BaseView;
import com.n8plus.vhiep.cyberzone.data.model.Product;
import com.n8plus.vhiep.cyberzone.data.model.ProductType;

import java.util.List;

public interface HomeContract {
    interface View {
        void setNameCustomer(String nameCustomer);
        void setAdapterSuggestion(List<Product> productList);
        void setAdapterOnSale(List<Product> productList);
        void setAdapterBestSeller(List<Product> productList);
        void setAdapterPopularCategory(List<ProductType> productTypeList);
        void setCartMenuItem();
        void setNotifyDataSetChanged(String adapter);
        void popularCategoryItemSelected(String productTypeId);
        void moveToProductActivity(List<Product> products);
        void moveToProductActivity(String productTypeId, String keyword);
        void moveToLoginActivity();
    }
    interface Presenter extends BasePresenter<View> {
        void loadData();
        void loadAllProductType();
        void loadDataCustomer(String accountId);
        void refreshPopularCategory();
        void prepareDataKeyword(String keyword);
        void prepareDataSuggestion();
        void prepareDataBestSeller();
        void prepareDataOnSale();
        void prepareDataProductType(String productTypeId);
        void signOut();
    }
}
