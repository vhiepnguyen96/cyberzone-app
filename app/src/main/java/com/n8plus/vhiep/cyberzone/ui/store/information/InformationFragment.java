package com.n8plus.vhiep.cyberzone.ui.store.information;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.n8plus.vhiep.cyberzone.R;
import com.n8plus.vhiep.cyberzone.data.model.Review;
import com.n8plus.vhiep.cyberzone.ui.store.adapter.CustomerReviewAdapter;
import com.n8plus.vhiep.cyberzone.ui.store.information.reviews.listreview.ListReviewFragment;
import com.n8plus.vhiep.cyberzone.ui.store.information.reviews.statistics.StatisticsFragment;

import java.util.ArrayList;
import java.util.List;

public class InformationFragment extends Fragment implements InformationContract.View {
    private RecyclerView mRecyclerCustomerReview;
    private CustomerReviewAdapter mCustomerReviewAdapter;
    private RecyclerView.LayoutManager mLayoutCustomerReview;
    private TextView mWriteReview, mCountMoreReview;
    private LinearLayout mMoreReview;
    private InformationPresenter mInformationPresenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.information_frag, container, false);
        mInformationPresenter = new InformationPresenter(this);
        loadStoreReview();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mRecyclerCustomerReview = (RecyclerView) view.findViewById(R.id.rcv_customer_reviews);
        mCountMoreReview = (TextView) view.findViewById(R.id.tv_count_more_review);
        mMoreReview = (LinearLayout) view.findViewById(R.id.lnr_more_review);

        mInformationPresenter.loadData();

        mMoreReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListReviewFragment specificationFragment = new ListReviewFragment();
                specificationFragment.show(getFragmentManager(), specificationFragment.getTag());
            }
        });

        super.onViewCreated(view, savedInstanceState);
    }

    private void loadStoreReview(){
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frm_store_reviews, new StatisticsFragment());
        fragmentTransaction.commit();
    }

    private List<Review> getRandomReview(List<Review> reviewList) {
        List<Review> arrayList = new ArrayList<>();
        if (reviewList.size() > 5) {
            for (int i = 0; i < 5; i++) {
                arrayList.add(reviewList.get(i));
            }
        } else {
            arrayList.addAll(reviewList);
        }
        return arrayList;
    }

    @Override
    public void setAdapterCustomerReview(List<Review> reviewList) {
        mLayoutCustomerReview =  new LinearLayoutManager(mRecyclerCustomerReview.getContext(), LinearLayoutManager.VERTICAL, false);
        mCustomerReviewAdapter = new CustomerReviewAdapter(getRandomReview(reviewList));
        DividerItemDecoration itemDecorator = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        mRecyclerCustomerReview.addItemDecoration(itemDecorator);
        mRecyclerCustomerReview.setLayoutManager(mLayoutCustomerReview);
        mRecyclerCustomerReview.setAdapter(mCustomerReviewAdapter);
    }
}
