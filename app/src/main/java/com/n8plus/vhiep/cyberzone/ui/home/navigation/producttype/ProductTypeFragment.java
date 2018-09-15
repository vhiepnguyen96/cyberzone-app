package com.n8plus.vhiep.cyberzone.ui.home.navigation.producttype;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.n8plus.vhiep.cyberzone.ui.home.adapter.ProductTypeAdapter;
import com.n8plus.vhiep.cyberzone.ui.home.navigation.category.CategoryFragment;
import com.n8plus.vhiep.cyberzone.data.model.ProductType;
import com.n8plus.vhiep.cyberzone.R;

import java.util.ArrayList;
import java.util.List;

public class ProductTypeFragment extends Fragment implements ProductTypeContract.View {
    private ListView mListViewCategoryItem;
    private TextView mBackCategory, mTypeName;
    private ProductTypeAdapter mProductTypeAdapter;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    private ProductTypeContract.Presenter mProductTypePreseter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.menu_product_type_frag, container, false);
        mProductTypePreseter = new ProductTypePresenter(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mListViewCategoryItem = (ListView) view.findViewById(R.id.lv_categoryItem);
        mBackCategory = (TextView) view.findViewById(R.id.txt_backCategory);
        mTypeName = (TextView) view.findViewById(R.id.txt_typeName);

        mProductTypePreseter.loadData();

        mBackCategory.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View v) {
                fragmentManager = getFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right );
                fragmentTransaction.replace(R.id.frameLayout, new CategoryFragment());
                fragmentTransaction.commit();
            }
        });

        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void setAdapterProductType(List<ProductType> productTypes) {
        mProductTypeAdapter = new ProductTypeAdapter(mListViewCategoryItem.getContext(), R.layout.row_menu_product_type, productTypes);
        mListViewCategoryItem.setAdapter(mProductTypeAdapter);
    }
}
