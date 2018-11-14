package com.n8plus.vhiep.cyberzone.ui.manage.myreview.notwritereview.allpurchase;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.n8plus.vhiep.cyberzone.R;
import com.n8plus.vhiep.cyberzone.data.model.Order;
import com.n8plus.vhiep.cyberzone.data.model.Product;
import com.n8plus.vhiep.cyberzone.data.model.PurchaseItem;
import com.n8plus.vhiep.cyberzone.ui.manage.myreview.adapter.ProductPurchaseAdapter;
import com.n8plus.vhiep.cyberzone.ui.manage.myreview.notwritereview.writereview.WriteReviewFragment;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AllPurchaseFragment extends Fragment implements AllPurchaseContract.View {
    private ListView mListAllPurchase;
    private LinearLayout mLinearReviewNone;
    private ProductPurchaseAdapter mProductPurchaseAdapter;
    private AllPurchasePresenter mAllPurchasePresenter;

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
        mLinearReviewNone = (LinearLayout) view.findViewById(R.id.lnr_review_none);

        // Presenter
        mAllPurchasePresenter.loadData();

        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void setLayoutReviewNone(boolean b) {
        mLinearReviewNone.setVisibility(b ? View.VISIBLE : View.GONE);
    }

    @Override
    public void setAdapterAllPurchase(List<PurchaseItem> purchaseItemList, List<Date> datePurchaseList) {
        mProductPurchaseAdapter = new ProductPurchaseAdapter(this, R.layout.row_purchase, purchaseItemList, datePurchaseList);
        mListAllPurchase.setAdapter(mProductPurchaseAdapter);
    }

    @Override
    public void setNotifyDataSetChanged() {
        mProductPurchaseAdapter.notifyDataSetChanged();
    }

    @Override
    public void writeReview(int position) {
        mAllPurchasePresenter.prepareDataWriteReview(position);
    }

    @Override
    public void moveToWriteReview(PurchaseItem orderItem) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("orderItem", orderItem);

        WriteReviewFragment writeReviewFragment = new WriteReviewFragment();
        writeReviewFragment.setArguments(bundle);
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frm_not_write_review, writeReviewFragment);
        fragmentTransaction.commit();
    }
}
