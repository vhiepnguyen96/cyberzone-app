package com.n8plus.vhiep.cyberzone.ui.home.navigation.category;

import android.widget.BaseAdapter;
import android.widget.ListView;

import com.n8plus.vhiep.cyberzone.R;
import com.n8plus.vhiep.cyberzone.data.model.Category;
import com.n8plus.vhiep.cyberzone.ui.home.adapter.CategoryAdapter;

import java.util.ArrayList;

public class CategoryPresenter implements CategoryContract.Presenter {
    private final CategoryContract.View mCategoryView;
    private ArrayList<Category> categories;

    public CategoryPresenter(CategoryContract.View mCategoryView) {
        this.mCategoryView = mCategoryView;
    }

    @Override
    public void loadData() {
        prepareData();
        mCategoryView.setAdapterCategory(categories);
    }

    public void prepareData() {
        categories = new ArrayList<>();
        categories.add(new Category("Linh kiện máy tính", R.drawable.ic_keyboard_arrow_right_black_24dp));
        categories.add(new Category("Màn hình máy tính", R.drawable.ic_keyboard_arrow_right_black_24dp));
        categories.add(new Category("Ổ cứng HDD/SSD", R.drawable.ic_keyboard_arrow_right_black_24dp));
        categories.add(new Category("Chuột, Bàn phím, Webcam", R.drawable.ic_keyboard_arrow_right_black_24dp));
        categories.add(new Category("Tai nghe & Loa", R.drawable.ic_keyboard_arrow_right_black_24dp));
    }

}
