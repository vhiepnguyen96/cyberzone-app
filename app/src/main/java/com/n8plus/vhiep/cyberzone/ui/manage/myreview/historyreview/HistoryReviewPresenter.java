package com.n8plus.vhiep.cyberzone.ui.manage.myreview.historyreview;

import com.n8plus.vhiep.cyberzone.R;
import com.n8plus.vhiep.cyberzone.data.model.Address;
import com.n8plus.vhiep.cyberzone.data.model.Customer;
import com.n8plus.vhiep.cyberzone.data.model.HistoryReview;
import com.n8plus.vhiep.cyberzone.data.model.Order;
import com.n8plus.vhiep.cyberzone.data.model.Overview;
import com.n8plus.vhiep.cyberzone.data.model.Product;
import com.n8plus.vhiep.cyberzone.data.model.ProductImage;
import com.n8plus.vhiep.cyberzone.data.model.ProductType;
import com.n8plus.vhiep.cyberzone.data.model.PurchaseItem;
import com.n8plus.vhiep.cyberzone.data.model.RatingStar;
import com.n8plus.vhiep.cyberzone.data.model.ReviewProduct;
import com.n8plus.vhiep.cyberzone.data.model.ReviewStore;
import com.n8plus.vhiep.cyberzone.data.model.Specification;
import com.n8plus.vhiep.cyberzone.data.model.Store;
import com.n8plus.vhiep.cyberzone.ui.manage.myorders.allorder.AllOrderContract;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class HistoryReviewPresenter implements HistoryReviewContract.Presenter {
    private List<HistoryReview> mHistoryReviewList;
    private HistoryReviewContract.View mHistoryReviewView;

    public HistoryReviewPresenter(HistoryReviewContract.View mHistoryReviewView) {
        this.mHistoryReviewView = mHistoryReviewView;
    }

    @Override
    public void loadData() {
        prepareData();
        mHistoryReviewView.setAdapterHistoryReview(mHistoryReviewList);
    }

    private void prepareData() {
        Store store = new Store("5b989eb9a6bce5234c9522ea", "Máy tính Phong Vũ");
//        List<ProductImage> imageList_1603653 = new ArrayList<>();
//        imageList_1603653.add(new ProductImage("5b98a6a6fe67871b2068add0", R.drawable.img_1603653_1));
        Product product = new Product("5b974fbf6153321ffc61b829", "Bo mạch chính/ Mainboard Asrock H110M-DVS R2.0");

        ReviewStore reviewStore = new ReviewStore("5b98a6a6fe67871b2068add0", "Nguyễn Văn Hiệp", store, R.drawable.emoji_good, "Hàng tốt, đóng gói cẩn thận!", Calendar.getInstance().getTime());
        ReviewProduct reviewProduct = new ReviewProduct("5b98a6a6fe67871b2068add1",new Customer("dâf", "Nguyễn Văn Hiệp"), product, new RatingStar("sfafsfa", 5, "5 SAO"), "Sản phẩm ổn trong tầm giá", new Date());

        mHistoryReviewList = new ArrayList<>();
        mHistoryReviewList.add(new HistoryReview(reviewStore, reviewProduct));
        mHistoryReviewList.add(new HistoryReview(reviewStore, reviewProduct));
        mHistoryReviewList.add(new HistoryReview(reviewStore, reviewProduct));
    }
}
