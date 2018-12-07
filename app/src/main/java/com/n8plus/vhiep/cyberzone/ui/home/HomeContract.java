package com.n8plus.vhiep.cyberzone.ui.home;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.n8plus.vhiep.cyberzone.base.BasePresenter;
import com.n8plus.vhiep.cyberzone.base.BaseView;
import com.n8plus.vhiep.cyberzone.data.model.Product;
import com.n8plus.vhiep.cyberzone.data.model.ProductType;
import com.n8plus.vhiep.cyberzone.util.TypeLoad;

import java.util.List;

public interface HomeContract {
    interface View {
        void setNameCustomer(String nameCustomer);
        void setAdapterSuggestion(List<Product> productList);
        void setAdapterOnSale(List<Product> productList);
        void setAdapterBestSeller(List<Product> productList);
        void setAdapterPopularCategory(List<ProductType> productTypeList);
        void startLoadMore();
        void onLoadMoreFailed();
        void onReachEnd();
        void setDataListAdapter(List<Product> dataList);
        void addDataListAdapter(List<Product> dataList);
        void setAlert(String message);
        void setCartMenuItem();
        void setNotifyDataSetChanged(int adapter);
        void setNotifyItemChanged(int adapter, int position);
        void setNotifyItemRemoved(int adapter, int position);
        void popularCategoryItemSelected(String productTypeId);
        void moveToProductActivity(TypeLoad type, Bundle data);
        void moveToProductDetail(Product product);
        void showConfirmLogout();
        void setRefreshing(boolean b);
    }
    interface Presenter extends BasePresenter<View> {
        void loadData();
        void loadAllProductType();
        void loadDataCustomer(String accountId);
        void loadCurrentTime();
        void loadProductBestSeller();
        void loadProductOnSale();
        void loadProductSuggestion();
        void loadMoreSuggestion();
        void refreshData();
        void refreshPopularCategory();
        void prepareDataKeyword(String keyword);
        void prepareDataSuggestion();
        void prepareDataBestSeller();
        void prepareDataOnSale();
        void prepareDataProductType(String productTypeId);
        void signOut();
    }
}
