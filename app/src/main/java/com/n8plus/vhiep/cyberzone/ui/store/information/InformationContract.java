package com.n8plus.vhiep.cyberzone.ui.store.information;

import android.widget.ListView;

import com.n8plus.vhiep.cyberzone.base.BasePresenter;
import com.n8plus.vhiep.cyberzone.data.model.Review;

import java.util.List;

public interface InformationContract {
    interface View{
        void setAdapterCustomerReview(List<Review> reviewList);
    }
    interface Presenter extends BasePresenter<View> {

    }
}
