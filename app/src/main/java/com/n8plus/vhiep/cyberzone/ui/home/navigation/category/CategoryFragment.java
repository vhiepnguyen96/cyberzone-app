package com.n8plus.vhiep.cyberzone.ui.home.navigation.category;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.n8plus.vhiep.cyberzone.ui.home.adapter.CategoryAdapter;
import com.n8plus.vhiep.cyberzone.ui.home.navigation.producttype.ProductTypeFragment;
import com.n8plus.vhiep.cyberzone.data.model.Category;
import com.n8plus.vhiep.cyberzone.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CategoryFragment extends Fragment implements CategoryContract.View {
    private ListView mListViewCategory;
    private CategoryAdapter mCategoryAdapter;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    private CategoryContract.Presenter mCategoryPresenter;
    List<Category> categories;

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
                Bundle bundle = new Bundle();
                bundle.putSerializable("category", categories.get(position));
                ProductTypeFragment fragment = new ProductTypeFragment();
                fragment.setArguments(bundle);
                fragmentTransaction.replace(R.id.frameLayout, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void setAdapterCategory(List<Category> categoryList) {
        categories = categoryList;
        mCategoryAdapter = new CategoryAdapter(mListViewCategory.getContext(), R.layout.row_menu_category, categoryList);
        mListViewCategory.setAdapter(mCategoryAdapter);
    }
}
