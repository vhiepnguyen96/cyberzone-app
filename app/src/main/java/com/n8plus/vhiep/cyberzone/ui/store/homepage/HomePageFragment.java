package com.n8plus.vhiep.cyberzone.ui.store.homepage;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;

import com.n8plus.vhiep.cyberzone.R;
import com.n8plus.vhiep.cyberzone.data.model.Product;
import com.n8plus.vhiep.cyberzone.ui.home.HomeContract;
import com.n8plus.vhiep.cyberzone.ui.product.adapter.ProductVerticalAdapter;
import com.n8plus.vhiep.cyberzone.ui.product.adapter.RecyclerProductAdapter;
import com.n8plus.vhiep.cyberzone.util.ItemDecorationColumns;

import java.util.List;

public class HomePageFragment extends Fragment implements HomePageContract.View {
    private Spinner mSpinnerSort;
    private ImageView mImageLayout;
    private RecyclerView mRecyclerAllProduct;
    private LinearLayout mLinearSort;
    private RecyclerProductAdapter mRecyclerProductAdapter;
    private HomePagePresenter mHomePagePresenter;
    private ItemDecorationColumns mItemDecorationColumns;
    private DividerItemDecoration mDividerItemDecoration;
    private LinearLayout mLayoutRecycler;
    private List<Product> mProductList;
    private static int layout = 0;

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
        mImageLayout = (ImageView) view.findViewById(R.id.img_layout_in_store);
        mRecyclerAllProduct = (RecyclerView) view.findViewById(R.id.rcv_all_product_in_store);
        mLinearSort = (LinearLayout) view.findViewById(R.id.lnr_sort_in_store);
        mLayoutRecycler = (LinearLayout) view.findViewById(R.id.layout_recycler_in_store);

        // Custom
        customSpinnerFilter();
        customRecyclerProduct();
        mLinearSort.setElevation(10.f);

        //Presenter
        mHomePagePresenter.loadData();

        // Listener
        mLayoutRecycler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout++;
                if (layout % 2 == 1) {
                    mImageLayout.setImageResource(R.drawable.grid);
                    mRecyclerProductAdapter = new RecyclerProductAdapter(R.layout.row_product_item_vertical, mProductList);
                    mRecyclerAllProduct.setLayoutManager(new LinearLayoutManager(mRecyclerAllProduct.getContext(), LinearLayoutManager.VERTICAL, false));
                    mRecyclerAllProduct.removeItemDecoration(mItemDecorationColumns);
                    mRecyclerAllProduct.addItemDecoration(mDividerItemDecoration);
                    mRecyclerAllProduct.setAdapter(mRecyclerProductAdapter);

                } else {
                    mImageLayout.setImageResource(R.drawable.linear);
                    mRecyclerProductAdapter = new RecyclerProductAdapter(R.layout.row_product_grid_layout, mProductList);
                    mRecyclerAllProduct.setLayoutManager(new GridLayoutManager(mRecyclerAllProduct.getContext(), 2));
                    mRecyclerAllProduct.removeItemDecoration(mDividerItemDecoration);
                    mRecyclerAllProduct.addItemDecoration(mItemDecorationColumns);
                    mRecyclerAllProduct.setAdapter(mRecyclerProductAdapter);
                }
            }
        });

        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void setAdapterProduct(List<Product> products) {
        mProductList = products;
        mRecyclerProductAdapter = new RecyclerProductAdapter(R.layout.row_product_grid_layout, mProductList);
        mRecyclerAllProduct.setLayoutManager(new GridLayoutManager(mRecyclerAllProduct.getContext(), 2));
        mRecyclerAllProduct.addItemDecoration(mItemDecorationColumns);
        mRecyclerAllProduct.setAdapter(mRecyclerProductAdapter);
    }

    private void customSpinnerFilter(){
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(mSpinnerSort.getContext(),
                R.array.sort_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerSort.setAdapter(adapter);
    }

    private void customRecyclerProduct() {
        ViewCompat.setNestedScrollingEnabled(mRecyclerAllProduct, true);
        mItemDecorationColumns = new ItemDecorationColumns(10, 2);
        mDividerItemDecoration = new DividerItemDecoration(mRecyclerAllProduct.getContext(), DividerItemDecoration.VERTICAL);
        mDividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.divider_linear_layout));
    }
}
