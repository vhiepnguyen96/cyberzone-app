package com.n8plus.vhiep.cyberzone.ui.home.navigation.category;

import android.widget.BaseAdapter;
import android.widget.ListView;

import com.n8plus.vhiep.cyberzone.base.BasePresenter;
import com.n8plus.vhiep.cyberzone.base.BaseView;
import com.n8plus.vhiep.cyberzone.data.model.Category;

import java.util.List;

public interface CategoryContract {
    interface View {
        void setAdapterCategory(List<Category> categoryList);
    }
    interface Presenter extends BasePresenter<View> {
        void loadData();
    }
}
