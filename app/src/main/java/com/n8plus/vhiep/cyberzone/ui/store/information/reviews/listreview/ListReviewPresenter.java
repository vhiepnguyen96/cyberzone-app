package com.n8plus.vhiep.cyberzone.ui.store.information.reviews.listreview;

import com.n8plus.vhiep.cyberzone.R;
import com.n8plus.vhiep.cyberzone.data.model.ReviewStore;
import com.n8plus.vhiep.cyberzone.data.model.Store;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ListReviewPresenter implements ListReviewContract.Presenter {
    private ListReviewContract.View mListReviewView;
    private List<ReviewStore> reviewStoreList;

    public ListReviewPresenter(ListReviewContract.View mListReviewView) {
        this.mListReviewView = mListReviewView;
    }

    @Override
    public void loadStoreReviews(List<ReviewStore> reviewStores) {
        reviewStoreList = reviewStores;
        mListReviewView.setAdapterCustomerReview(reviewStores);
    }

}
