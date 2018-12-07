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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.n8plus.vhiep.cyberzone.R;
import com.n8plus.vhiep.cyberzone.data.model.ReviewStore;
import com.n8plus.vhiep.cyberzone.data.model.Store;
import com.n8plus.vhiep.cyberzone.ui.store.adapter.CustomerReviewAdapter;
import com.n8plus.vhiep.cyberzone.ui.store.information.reviews.listreview.ListReviewFragment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class InformationFragment extends Fragment implements InformationContract.View {
    private RecyclerView mRecyclerCustomerReview;
    private CustomerReviewAdapter mCustomerReviewAdapter;
    private RecyclerView.LayoutManager mLayoutCustomerReview;
    private TextView mWriteReview, mCountMoreReview, mStoreLocation, mTimeInSystem, mPopularCategory,
            mPositiveReview, mCountReview, mCountGoodReview, mCountNormalReview, mCountNotGoodReview;
    private ProgressBar mProgressGoodReview, mProgressNormalReview, mProgressNotGoodReview;
    private LinearLayout mMoreReview;
    private InformationPresenter mInformationPresenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.information_frag, container, false);
        mInformationPresenter = new InformationPresenter(getContext(),this);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mStoreLocation = (TextView) view.findViewById(R.id.tv_store_location);
        mTimeInSystem = (TextView) view.findViewById(R.id.tv_time_in_system);
        mPopularCategory = (TextView) view.findViewById(R.id.tv_popular_category);
        mRecyclerCustomerReview = (RecyclerView) view.findViewById(R.id.rcv_customer_reviews);
        mCountMoreReview = (TextView) view.findViewById(R.id.tv_count_more_review);
        mMoreReview = (LinearLayout) view.findViewById(R.id.lnr_more_review);
        mPositiveReview = (TextView) view.findViewById(R.id.tv_review_percent);
        mCountReview = (TextView) view.findViewById(R.id.tv_total_reviews);
        mCountGoodReview = (TextView) view.findViewById(R.id.tv_count_good_reviews);
        mCountNormalReview = (TextView) view.findViewById(R.id.tv_count_normal_reviews);
        mCountNotGoodReview = (TextView) view.findViewById(R.id.tv_count_notgood_reviews);
        mProgressGoodReview = (ProgressBar) view.findViewById(R.id.pb_good_reviews);
        mProgressNormalReview = (ProgressBar) view.findViewById(R.id.pb_normal_reviews);
        mProgressNotGoodReview = (ProgressBar) view.findViewById(R.id.pb_notgood_reviews);

        Bundle bundle = getArguments();
        if (bundle != null && bundle.getSerializable("store") != null){
            mInformationPresenter.loadData((Store) bundle.getSerializable("store"));
        }

        mMoreReview.setOnClickListener(v -> mInformationPresenter.prepareDataMoreReview());

        super.onViewCreated(view, savedInstanceState);
    }

    private List<ReviewStore> getRandomReview(List<ReviewStore> reviewStoreList) {
        List<ReviewStore> arrayList = new ArrayList<>();
        if (reviewStoreList.size() > 10) {
            for (int i = 0; i < 10; i++) {
                arrayList.add(reviewStoreList.get(i));
            }
        } else {
            arrayList.addAll(reviewStoreList);
        }
        return arrayList;
    }

    @Override
    public void setStoreLocation(String storeLocation) {
        mStoreLocation.setText(storeLocation);
    }

    @Override
    public void setTimeInSystem(String timeInSystem) {
        mTimeInSystem.setText(timeInSystem);
    }

    @Override
    public void setPopularCategory(String popularCategory) {
        mPopularCategory.setText(popularCategory);
    }

    @Override
    public void setAdapterCustomerReview(List<ReviewStore> reviewStoreList) {
        mLayoutCustomerReview =  new LinearLayoutManager(mRecyclerCustomerReview.getContext(), LinearLayoutManager.VERTICAL, false);
        mCustomerReviewAdapter = new CustomerReviewAdapter(getRandomReview(reviewStoreList));
//        DividerItemDecoration itemDecorator = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
//        mRecyclerCustomerReview.addItemDecoration(itemDecorator);
        mRecyclerCustomerReview.setLayoutManager(mLayoutCustomerReview);
        mRecyclerCustomerReview.setAdapter(mCustomerReviewAdapter);
    }

    @Override
    public void setPositiveReview(String positiveReview) {
        mPositiveReview.setText(positiveReview);
    }

    @Override
    public void setTotalReview(String totalReview) {
        mCountReview.setText(totalReview);
    }

    @Override
    public void setGoodReview(int totalReview, int goodReview) {
        mProgressGoodReview.setMax(totalReview);
        mProgressGoodReview.setProgress(goodReview);
        mCountGoodReview.setText(String.valueOf(goodReview));
    }

    @Override
    public void setNormalReview(int totalReview, int normalReview) {
        mProgressNormalReview.setMax(totalReview);
        mProgressNormalReview.setProgress(normalReview);
        mCountNormalReview.setText(String.valueOf(normalReview));
    }

    @Override
    public void setNotGoodReview(int totalReview, int notGoodReview) {
        mProgressNotGoodReview.setMax(totalReview);
        mProgressNotGoodReview.setProgress(notGoodReview);
        mCountNotGoodReview.setText(String.valueOf(notGoodReview));
    }

    @Override
    public void setCountMoreReview(String count) {
        mCountMoreReview.setText(count);
    }

    @Override
    public void alertNoMoreReview() {
        Toast.makeText(this.getContext(), "Đã hiển thị tất cả nhận xét!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showMoreReview(List<ReviewStore> reviewStores) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("reviewStores", (Serializable) reviewStores);
        ListReviewFragment specificationFragment = new ListReviewFragment();
        specificationFragment.setArguments(bundle);
        specificationFragment.show(getFragmentManager(), specificationFragment.getTag());
    }
}
