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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;

import com.n8plus.vhiep.cyberzone.R;
import com.n8plus.vhiep.cyberzone.data.model.Product;
import com.n8plus.vhiep.cyberzone.data.model.Store;
import com.n8plus.vhiep.cyberzone.ui.home.HomeContract;
import com.n8plus.vhiep.cyberzone.ui.product.adapter.ProductVerticalAdapter;
import com.n8plus.vhiep.cyberzone.ui.product.adapter.RecyclerProductAdapter;
import com.n8plus.vhiep.cyberzone.util.ItemDecorationColumns;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class HomePageFragment extends Fragment implements HomePageContract.View {
    private int GRID_LAYOUT = 1, LINEAR_LAYOUT = 2;
    private Spinner mSpinnerSort;
    private ImageView mImageLayout;
    private RecyclerView mRecyclerAllProduct;
    private LinearLayout mLinearSort;
    private RecyclerProductAdapter mRecyclerProductAdapter;
    private HomePagePresenter mHomePagePresenter;
    private ItemDecorationColumns mItemDecorationColumns;
    private DividerItemDecoration mDividerItemDecoration;
    private LinearLayout mLayoutRecycler;
    private static int layout = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_page_frag, container, false);
        mHomePagePresenter = new HomePagePresenter(getContext(), this);
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
        Bundle bundle = getArguments();
        if (bundle != null && bundle.getSerializable("store") != null) {
            mHomePagePresenter.loadData((Store) bundle.getSerializable("store"));
        }

        // Listener
        mLayoutRecycler.setOnClickListener(v -> {
            layout++;
            if (layout % 2 == 1) {
                mImageLayout.setImageResource(R.drawable.grid);
                mHomePagePresenter.changeProductLinearLayout();

            } else {
                mImageLayout.setImageResource(R.drawable.linear);
                mHomePagePresenter.changeProductGridLayout();
            }
        });

        mSpinnerSort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    if (mRecyclerProductAdapter != null) {
                        Collections.sort(mRecyclerProductAdapter.getProductList(), (o1, o2) -> Float.valueOf(o2.getAverageReview()).compareTo(Float.valueOf(o1.getAverageReview())));
                        mRecyclerAllProduct.setAdapter(mRecyclerProductAdapter);
                    }
                } else if (position == 1) {
                    Collections.sort(mRecyclerProductAdapter.getProductList(), (o1, o2) -> Float.valueOf(o1.getPrice()).compareTo(Float.valueOf(o2.getPrice())));
                    mRecyclerAllProduct.setAdapter(mRecyclerProductAdapter);
                } else if (position == 2) {
                    Collections.sort(mRecyclerProductAdapter.getProductList(), (o1, o2) -> Float.valueOf(o2.getPrice()).compareTo(Float.valueOf(o1.getPrice())));
                    mRecyclerAllProduct.setAdapter(mRecyclerProductAdapter);
                }
                mRecyclerAllProduct.setAdapter(mRecyclerProductAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void setAdapterProduct(List<Product> products, int layout) {
        if (layout == GRID_LAYOUT) {
            mRecyclerProductAdapter = new RecyclerProductAdapter(R.layout.row_product_grid_layout, products, layout);
            mRecyclerAllProduct.setLayoutManager(new GridLayoutManager(mRecyclerAllProduct.getContext(), 2));
            mRecyclerAllProduct.removeItemDecoration(mDividerItemDecoration);
            if (mRecyclerAllProduct.getItemDecorationCount() == 0) {
                mRecyclerAllProduct.addItemDecoration(mItemDecorationColumns);
            }
            mRecyclerAllProduct.setAdapter(mRecyclerProductAdapter);
        } else {
            mRecyclerProductAdapter = new RecyclerProductAdapter(R.layout.row_product_item_vertical, products, layout);
            mRecyclerAllProduct.setLayoutManager(new LinearLayoutManager(mRecyclerAllProduct.getContext(), LinearLayoutManager.VERTICAL, false));
            mRecyclerAllProduct.removeItemDecoration(mItemDecorationColumns);
            mRecyclerAllProduct.addItemDecoration(mDividerItemDecoration);
            mRecyclerAllProduct.setAdapter(mRecyclerProductAdapter);
        }
    }

    @Override
    public void setNotifyDataSetChanged() {
        mRecyclerProductAdapter.notifyDataSetChanged();
    }

    private void customSpinnerFilter() {
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
