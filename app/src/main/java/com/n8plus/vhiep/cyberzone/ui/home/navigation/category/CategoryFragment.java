package com.n8plus.vhiep.cyberzone.ui.home.navigation.category;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.n8plus.vhiep.cyberzone.ui.home.adapter.CategoryAdapter;
import com.n8plus.vhiep.cyberzone.ui.home.navigation.producttype.ProductTypeFragment;
import com.n8plus.vhiep.cyberzone.data.model.Category;
import com.n8plus.vhiep.cyberzone.R;

import java.util.ArrayList;
import java.util.List;

public class CategoryFragment extends Fragment implements CategoryContract.View {
    private ListView mListViewCategory;
    private CategoryAdapter mCategoryAdapter;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    private CategoryContract.Presenter mCategoryPresenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.menu_category_frag, container, false);
        mCategoryPresenter = new CategoryPresenter(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mListViewCategory = (ListView) view.findViewById(R.id.lv_category);

        mCategoryPresenter.loadData();

        mListViewCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                fragmentManager = getFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left );
                fragmentTransaction.replace(R.id.frameLayout, new ProductTypeFragment());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void setAdapterCategory(List<Category> categoryList) {
        mCategoryAdapter = new CategoryAdapter(mListViewCategory.getContext(), R.layout.row_menu_category, categoryList);
        mListViewCategory.setAdapter(mCategoryAdapter);
    }
}
