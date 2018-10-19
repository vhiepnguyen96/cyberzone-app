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
    public void loadData() {
        prepareData();
        mListReviewView.setAdapterCustomerReview(reviewStoreList);
    }

    private void prepareData() {
        Store store = new Store("5b989eb9a6bce5234c9522ea", "Máy tính Phong Vũ");
        reviewStoreList = new ArrayList<>();
        reviewStoreList.add(new ReviewStore("rv1", "Huỳnh Khắc Duy", store, R.drawable.emoji_notgood, "Hàng dở quá anh êi!", Calendar.getInstance().getTime()));
        reviewStoreList.add(new ReviewStore("rv2", "Võ Hoài Phong", store, R.drawable.emoji_good, "Hàng tốt, đóng gói cẩn thận!", Calendar.getInstance().getTime()));
        reviewStoreList.add(new ReviewStore("rv5", "Đặng Minh Nhựt", store, R.drawable.emoji_notgood, "Hàng kém chất lượng, đóng gói ẩu, giao hàng lâu, đừng mua shop này nhé, qua shop mình mua nè! Ahihi", Calendar.getInstance().getTime()));
        reviewStoreList.add(new ReviewStore("rv3", "Nguyễn Văn Hiệp", store, R.drawable.emoji_normal, "Hàng chất lượng, vận chuyển hơi lâu!", Calendar.getInstance().getTime()));
        reviewStoreList.add(new ReviewStore("rv4", "Võ Nguyễn Đại Phúc", store, R.drawable.emoji_notgood, "Hàng dở quá anh êi!", Calendar.getInstance().getTime()));
        reviewStoreList.add(new ReviewStore("rv6", "Nguyễn Văn Tài", store, R.drawable.emoji_normal, "Hàng chất lượng, vận chuyển hơi lâu!", Calendar.getInstance().getTime()));
        reviewStoreList.add(new ReviewStore("rv3", "Nguyễn Văn Hiệp", store, R.drawable.emoji_normal, "Hàng chất lượng, vận chuyển hơi lâu!", Calendar.getInstance().getTime()));
        reviewStoreList.add(new ReviewStore("rv4", "Võ Nguyễn Đại Phúc", store, R.drawable.emoji_notgood, "Hàng dở quá anh êi!", Calendar.getInstance().getTime()));
        reviewStoreList.add(new ReviewStore("rv6", "Nguyễn Văn Tài", store, R.drawable.emoji_normal, "Hàng chất lượng, vận chuyển hơi lâu!", Calendar.getInstance().getTime()));
        reviewStoreList.add(new ReviewStore("rv3", "Nguyễn Văn Hiệp", store, R.drawable.emoji_normal, "Hàng chất lượng, vận chuyển hơi lâu!", Calendar.getInstance().getTime()));
        reviewStoreList.add(new ReviewStore("rv4", "Võ Nguyễn Đại Phúc", store, R.drawable.emoji_notgood, "Hàng dở quá anh êi!", Calendar.getInstance().getTime()));
        reviewStoreList.add(new ReviewStore("rv6", "Nguyễn Văn Tài", store, R.drawable.emoji_normal, "Hàng chất lượng, vận chuyển hơi lâu!", Calendar.getInstance().getTime()));
    }
}
