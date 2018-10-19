package com.n8plus.vhiep.cyberzone.ui.manage.wishlistfollowedstore.wishlist;

import com.n8plus.vhiep.cyberzone.R;
import com.n8plus.vhiep.cyberzone.data.model.Overview;
import com.n8plus.vhiep.cyberzone.data.model.Product;
import com.n8plus.vhiep.cyberzone.data.model.ProductImage;
import com.n8plus.vhiep.cyberzone.data.model.ProductType;
import com.n8plus.vhiep.cyberzone.data.model.SaleOff;
import com.n8plus.vhiep.cyberzone.data.model.Specification;
import com.n8plus.vhiep.cyberzone.data.model.Store;
import com.n8plus.vhiep.cyberzone.data.model.WishList;
import com.n8plus.vhiep.cyberzone.ui.manage.myorders.MyOrderContract;

import java.util.ArrayList;
import java.util.List;

public class WishListPresenter implements WishListContract.Presenter {
    private WishListContract.View mWishListView;
    private WishList mWishList;

    public WishListPresenter(WishListContract.View mWishListView) {
        this.mWishListView = mWishListView;
    }

    @Override
    public void loadData() {
        prepareData();
        mWishListView.setAdapterWishList(mWishList);
    }

    private void prepareData(){
        List<Specification> specifications = new ArrayList<>();
        specifications.add(new Specification("Bảo hành", "36"));
        specifications.add(new Specification("Thương hiệu", "Asrock"));
        specifications.add(new Specification("Model", "H110M-DVS R2.0"));
        specifications.add(new Specification("Loại", "Micro-ATX"));
        specifications.add(new Specification("Loại Socket", "LGA 1151"));
        specifications.add(new Specification("Chipset", "Intel H110"));
        specifications.add(new Specification("Số khe Ram", "2"));
        specifications.add(new Specification("Dung lượng Ram tối đa", "32GB"));
        specifications.add(new Specification("Loại Ram", "DDR4 2133"));
        specifications.add(new Specification("VGA Onboard", "Intel HD Graphics"));

        ProductType productType = new ProductType("5b98a6a6fe67871b2068add0", "Bo mạch chủ");
        Store store = new Store("5b989eb9a6bce5234c9522ea", "Máy tính Phong Vũ");

//        List<ProductImage> imageList_1603653 = new ArrayList<>();
//        imageList_1603653.add(new ProductImage("5b98a6a6fe67871b2068add0", R.drawable.img_1603653_1));
//
//        List<ProductImage> imageList_1600666 = new ArrayList<>();
//        imageList_1600666.add(new ProductImage("5b98a6a6fe67871b2068add0", R.drawable.img_1600666));
//
//        List<ProductImage> imageList_1701299 = new ArrayList<>();
//        imageList_1701299.add(new ProductImage("5b98a6a6fe67871b2068add0", R.drawable.img_1701299));
//
//        List<ProductImage> imageList_1704264 = new ArrayList<>();
//        imageList_1704264.add(new ProductImage("5b98a6a6fe67871b2068add0", R.drawable.img_1704264));
//
//        List<ProductImage> imageList_1501266 = new ArrayList<>();
//        imageList_1501266.add(new ProductImage("5b98a6a6fe67871b2068add0", R.drawable.img_1501266));

        List<Overview> overviews = new ArrayList<>();
        overviews.add(new Overview("", "ASRock trang bị cho H110M-DVS R2.0 chuẩn linh kiện Super Alloy bền bỉ - trước đây vốn chỉ xuất hiện trên các bo mạch chủ trung cấp và cao cấp thể hiện trong thông điệp Stable and Reliable - Ổn định và tin cậy."));

        Product product_1603653 = new Product("1603653", productType, store, "Bo mạch chính/ Mainboard Asrock H110M-DVS R2.0", "1.320", specifications, overviews, "New", new SaleOff("1", 5));
        Product product_1600666 = new Product("1600666", productType, store, "Bo mạch chính/ Mainboard Gigabyte H110M-DS2 DDR4", "1.465", specifications, overviews, "New", new SaleOff("1", 6));
        Product product_1701299 = new Product("1701299", productType, store, "Bo mạch chính/ Mainboard Gigabyte B250M-Gaming 3", "1.899", specifications, overviews, "New", new SaleOff("1", 0));
        Product product_1704264 = new Product("1704264", productType, store, "Bo mạch chính/ Mainboard Msi A320M Bazooka", "2.180", specifications, overviews, "New", new SaleOff("1", 0));
        Product product_1501266 = new Product("1501266", productType, store, "Bo mạch chính/ Mainboard Asus H81M-K", "1.280", specifications, overviews, "New", new SaleOff("1", 0));

        List<Product> productList = new ArrayList<>();
        productList.add(product_1603653);
        productList.add(product_1600666);
        productList.add(product_1701299);
        productList.add(product_1704264);
        productList.add(product_1501266);

        mWishList = new WishList("5b962cd9738558095492b986", productList);
    }
}
