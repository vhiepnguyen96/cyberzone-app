package com.n8plus.vhiep.cyberzone.ui.store.homepage;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;

import com.n8plus.vhiep.cyberzone.R;
import com.n8plus.vhiep.cyberzone.data.model.Product;
import com.n8plus.vhiep.cyberzone.ui.home.HomeContract;
import com.n8plus.vhiep.cyberzone.ui.product.adapter.ProductVerticalAdapter;

import java.util.List;

public class HomePageFragment extends Fragment implements HomePageContract.View {
    private Spinner mSpinnerSort;
    private ListView mListAllProduct;
    private LinearLayout mLinearSort;
    private ProductVerticalAdapter mProductVerticalAdapter;
    private HomePagePresenter mHomePagePresenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_page_frag, container, false);
        mHomePagePresenter = new HomePagePresenter(this);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        // Init view
        mSpinnerSort = (Spinner) view.findViewById(R.id.spn_sort_in_store);
        mListAllProduct = (ListView) view.findViewById(R.id.lsv_all_product_in_store);
        mLinearSort = (LinearLayout) view.findViewById(R.id.lnr_sort_in_store);

        // Custom
        customSpinnerFilter();
        mLinearSort.setElevation(10.f);

        //Presenter
        mHomePagePresenter.loadData();

        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void setAdapterProduct(List<Product> products) {
        ViewCompat.setNestedScrollingEnabled(mListAllProduct, true);
        mProductVerticalAdapter = new ProductVerticalAdapter(mListAllProduct.getContext(), R.layout.row_product_item_vertical, products);
        mListAllProduct.setAdapter(mProductVerticalAdapter);
    }

    private void customSpinnerFilter(){
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(mSpinnerSort.getContext(),
                R.array.sort_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerSort.setAdapter(adapter);
    }
}
