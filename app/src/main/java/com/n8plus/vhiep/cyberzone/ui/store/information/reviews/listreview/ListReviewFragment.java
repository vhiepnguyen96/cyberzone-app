package com.n8plus.vhiep.cyberzone.ui.store.information.reviews.listreview;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.n8plus.vhiep.cyberzone.R;
import com.n8plus.vhiep.cyberzone.data.model.ReviewStore;
import com.n8plus.vhiep.cyberzone.ui.store.adapter.CustomerReviewAdapter;

import java.util.List;

public class ListReviewFragment extends BottomSheetDialogFragment implements ListReviewContract.View {
    private ImageView mCloseListReview;
    private RecyclerView mRecyclerCustomerReview;
    private CustomerReviewAdapter mCustomerReviewAdapter;
    private RecyclerView.LayoutManager mLayoutCustomerReview;
    private ListReviewPresenter mListReviewPresenter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_reviews_frag, container, false);
        mListReviewPresenter = new ListReviewPresenter(this);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mRecyclerCustomerReview = (RecyclerView) view.findViewById(R.id.rcv_all_customer_reviews);
        mCloseListReview = (ImageView) view.findViewById(R.id.img_close_list_review);

        Bundle bundle = getArguments();
        if (bundle != null && bundle.getSerializable("reviewStores") != null) {
            mListReviewPresenter.loadStoreReviews((List<ReviewStore>) bundle.getSerializable("reviewStores"));
        }

        mCloseListReview.setOnClickListener(v -> getDialog().dismiss());
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void setAdapterCustomerReview(List<ReviewStore> reviewStoreList) {
        mLayoutCustomerReview = new LinearLayoutManager(mRecyclerCustomerReview.getContext(), LinearLayoutManager.VERTICAL, false);
        mCustomerReviewAdapter = new CustomerReviewAdapter(reviewStoreList);
        mRecyclerCustomerReview.setLayoutManager(mLayoutCustomerReview);
        DividerItemDecoration itemDecorator = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        mRecyclerCustomerReview.addItemDecoration(itemDecorator);
        mRecyclerCustomerReview.setAdapter(mCustomerReviewAdapter);
    }
}
