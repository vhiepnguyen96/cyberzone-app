package com.n8plus.vhiep.cyberzone.ui.home.navigation.producttype;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.n8plus.vhiep.cyberzone.R;
import com.n8plus.vhiep.cyberzone.data.model.ProductType;
import com.n8plus.vhiep.cyberzone.ui.home.adapter.ProductTypeAdapter;

import java.util.ArrayList;

import static android.support.v4.util.Preconditions.checkNotNull;

public class ProductTypePresenter implements ProductTypeContract.Presenter {
    private ProductTypeContract.View mProductTypeView;
    private ArrayList<ProductType> productTypes;

    public ProductTypePresenter(ProductTypeContract.View mProductTypeView) {
        this.mProductTypeView = mProductTypeView;
    }

    @Override
    public void loadData() {
        prepareData();
        mProductTypeView.setAdapterProductType(productTypes);
    }

    public void prepareData() {
        productTypes = new ArrayList<>();
        productTypes.add(new ProductType("CPU - Bộ xử lý", R.drawable.processor));
        productTypes.add(new ProductType("Bo mạch chủ", R.drawable.motherboard));
        productTypes.add(new ProductType("Card màn hình - VGA", R.drawable.vga_card));
        productTypes.add(new ProductType("Nguồn - PSU", R.drawable.psu));
        productTypes.add(new ProductType("RAM", R.drawable.memory));
        productTypes.add(new ProductType("Case", R.drawable.case_pc));
        productTypes.add(new ProductType("Tản nhiệt", R.drawable.radial_fan));
    }

}
