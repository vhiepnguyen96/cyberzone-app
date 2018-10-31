package com.n8plus.vhiep.cyberzone.ui.manage.wishlistfollowedstore.wishlist;

import com.n8plus.vhiep.cyberzone.base.BasePresenter;
import com.n8plus.vhiep.cyberzone.data.model.Product;
import com.n8plus.vhiep.cyberzone.data.model.WishList;

import java.util.List;

public interface WishListContract {
    interface View {
        void setAdapterWishList(List<WishList> wishList);
        void setNotifyDataSetChanged();
        void actionMoveToProductDetail(int position);
        void actionRemoveFromWishList(int position);
        void actionAddToCart(int position);
        void addToCartAlert(boolean b);
        void removeFromWishListAlert(boolean b);
        void moveToProductDetail(Product product);
    }
    interface Presenter extends BasePresenter<View> {
        void loadData();
        void loadWishList(String customerId);
        void loadProduct(int position);
        void loadImageProduct(int position);
        void prepareDataProductDetail(int position);
        void addToCart(int position);
        void removeFromWishList(int position);
    }
}
