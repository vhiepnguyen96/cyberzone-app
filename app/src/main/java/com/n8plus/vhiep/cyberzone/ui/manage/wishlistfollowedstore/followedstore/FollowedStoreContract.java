package com.n8plus.vhiep.cyberzone.ui.manage.wishlistfollowedstore.followedstore;

import com.n8plus.vhiep.cyberzone.base.BasePresenter;
import com.n8plus.vhiep.cyberzone.data.model.FollowStore;
import com.n8plus.vhiep.cyberzone.data.model.Store;

import java.util.List;

public interface FollowedStoreContract {
    interface View {
        void setLayoutNone(boolean b);
        void setAdapterFollowStore(List<FollowStore> followStores);
        void setNotifyDataSetChanged();
        void actionUnfollowStore(int position);
        void actionGoToStore(int position);
        void moveToStore(Store store);
        void unfollowStoreAlert(boolean b);
    }
    interface Presenter extends BasePresenter<View> {
        void loadData();
        void loadFollowStore(String customerId);
        void goToStore(int position);
        void unfollowStore(int position);
    }
}
