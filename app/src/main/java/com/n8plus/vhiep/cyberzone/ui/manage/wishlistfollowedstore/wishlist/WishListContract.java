package com.n8plus.vhiep.cyberzone.ui.manage.wishlistfollowedstore.wishlist;

import com.n8plus.vhiep.cyberzone.base.BasePresenter;
import com.n8plus.vhiep.cyberzone.data.model.Product;
import com.n8plus.vhiep.cyberzone.data.model.WishList;

import java.util.List;

public interface WishListContract {
    interface View {
        void setAdapterWishList(WishList wishList);
    }
    interface Presenter extends BasePresenter<View> {
        void loadData();
    }
}
