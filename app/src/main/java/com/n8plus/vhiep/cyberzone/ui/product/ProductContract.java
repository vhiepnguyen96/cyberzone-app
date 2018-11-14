package com.n8plus.vhiep.cyberzone.ui.product;

import com.bignerdranch.expandablerecyclerview.Model.ParentObject;
import com.n8plus.vhiep.cyberzone.base.BasePresenter;
import com.n8plus.vhiep.cyberzone.base.BaseView;
import com.n8plus.vhiep.cyberzone.data.model.Filter;
import com.n8plus.vhiep.cyberzone.data.model.Product;
import com.n8plus.vhiep.cyberzone.util.TypeLoad;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public interface ProductContract {
    interface View {
        void setAdapterProduct(List<Product> products, int layout);
        void setNotifyDataSetChanged();
        void setNotifyItemChanged(int position);
        void generateFilters(ArrayList<ParentObject> parentObjects);
        void setCartMenuItem();
        void setKeyword(String keyword);
        void showLinearNotFound(boolean b);
        void showLinearLoading(boolean b);
        void startLoadMore();
        void onLoadMoreFailed();
        void setDataListAdapter(List<Product> dataList);
        void addDataListAdapter(List<Product> dataList);
        void setNotifyItemRemoved(int position);
        void onReachEnd();
        void setAlert(String message);
        void moveToProductDetail(Product product);
    }
    interface Presenter extends BasePresenter<View> {
        void loadCurrentTime();
        Date getCurrentTime();
        void loadFilter();
        void loadProductBestSeller();
        void loadProductOnSale();
        void loadProductByProductType(String productTypeId);
        void loadProductSuggestion();
        void loadProductByKeyWord(String keyword);
        void loadMoreProduct(TypeLoad typeLoad, int page);
        void changeProductGridLayout();
        void changeProductLinearLayout();
        void resetFilter();
    }
}
