package com.n8plus.vhiep.cyberzone.ui.manage.wishlistfollowedstore.followedstore;

import com.n8plus.vhiep.cyberzone.data.model.Account;
import com.n8plus.vhiep.cyberzone.data.model.Category;
import com.n8plus.vhiep.cyberzone.data.model.Customer;
import com.n8plus.vhiep.cyberzone.data.model.Product;
import com.n8plus.vhiep.cyberzone.data.model.Store;
import com.n8plus.vhiep.cyberzone.ui.manage.wishlistfollowedstore.wishlist.WishListContract;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FollowedStorePresenter implements FollowedStoreContract.Presenter {
    private FollowedStoreContract.View mFollowedStoreView;
    private List<Store> mStoreList;

    public FollowedStorePresenter(FollowedStoreContract.View mFollowedStoreView) {
        this.mFollowedStoreView = mFollowedStoreView;
    }

    @Override
    public void loadData() {
        prepareData();
        mFollowedStoreView.setAdapterFollowStore(mStoreList);
    }

    private void prepareData(){
        List<Category> categories = new ArrayList<>();
        categories.add(new Category("5b974fb26153321ffc61b828", "Linh kiện máy tính"));
        categories.add(new Category("5b974fbf6153321ffc61b829", "Màn hình máy tính"));
        categories.add(new Category("5b974fc86153321ffc61b82a", "Ổ cứng HDD/SSD"));

        mStoreList = new ArrayList<>();
        mStoreList.add(new Store("5b989eb9a6bce5234c9522ea", new Account("admin", "admin"), "Máy tính Phong Vũ", "Hồ Chí Minh", "0909159753", new Date(), categories));
        mStoreList.add(new Store("5b989eb9a6bce5234c9522ea", new Account("admin", "admin"), "Phi Long Gaming", "Hồ Chí Minh", "0909159753", new Date(), categories));
        mStoreList.add(new Store("5b989eb9a6bce5234c9522ea", new Account("admin", "admin"), "An Phát PC", "Đà Nẵng", "0909159753", new Date(), categories));
        mStoreList.add(new Store("5b989eb9a6bce5234c9522ea", new Account("admin", "admin"), "Hà Nội Computer", "Hà Nội", "0909159753", new Date(), categories));
        mStoreList.add(new Store("5b989eb9a6bce5234c9522ea", new Account("admin", "admin"), "Máy tính Duy Huỳnh", "Kiên Giang", "0909159753", new Date(), categories));

    }
}
