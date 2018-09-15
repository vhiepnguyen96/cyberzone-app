package com.n8plus.vhiep.cyberzone.ui.manage.wishlistfollowedstore;

import com.n8plus.vhiep.cyberzone.base.BasePresenter;

public interface WishlistFollowedstoreContract {
    interface View {

    }
    interface Presenter extends BasePresenter<View> {
        void loadData();
    }
}
