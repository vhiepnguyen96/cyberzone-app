package com.n8plus.vhiep.cyberzone.ui.manage.wishlistfollowedstore.followedstore;

import com.n8plus.vhiep.cyberzone.base.BasePresenter;
import com.n8plus.vhiep.cyberzone.data.model.Store;

import java.util.List;

public interface FollowedStoreContract {
    interface View {
        void setAdapterFollowStore(List<Store> storeList);
    }
    interface Presenter extends BasePresenter<View> {
        void loadData();
    }
}
