package com.n8plus.vhiep.cyberzone.ui.manage.myreview.notwritereview.allpurchase;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.n8plus.vhiep.cyberzone.R;
import com.n8plus.vhiep.cyberzone.data.model.Order;
import com.n8plus.vhiep.cyberzone.data.model.Product;
import com.n8plus.vhiep.cyberzone.ui.manage.myreview.adapter.ProductPurchaseAdapter;
import com.n8plus.vhiep.cyberzone.ui.manage.myreview.notwritereview.writereview.WriteReviewFragment;

import java.util.ArrayList;
import java.util.List;

public class AllPurchaseFragment extends Fragment implements AllPurchaseContract.View {
    private ListView mListAllPurchase;
    private ProductPurchaseAdapter mProductPurchaseAdapter;
    private AllPurchasePresenter mAllPurchasePresenter;
    private List<Product> mAllProductPurchase;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.all_purchase_frag, container, false);
        mAllPurchasePresenter = new AllPurchasePresenter(this);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mListAllPurchase = (ListView) view.findViewById(R.id.lsv_all_purchase);

        // Presenter
        mAllPurchasePresenter.loadData();

        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void setAdapterAllPurchase(List<Order> orderList) {
        mAllProductPurchase = new ArrayList<>();
        for (int i = 0; i < orderList.size(); i++) {
            for (int j=0; j<orderList.get(i).getPurchaseList().size(); j++){
                mAllProductPurchase.add(orderList.get(i).getPurchaseList().get(j).getProduct());
            }
        }
        mProductPurchaseAdapter = new ProductPurchaseAdapter(mListAllPurchase.getContext(), R.layout.row_purchase, mAllProductPurchase);
        mListAllPurchase.setAdapter(mProductPurchaseAdapter);
    }
}
