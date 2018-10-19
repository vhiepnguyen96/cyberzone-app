package com.n8plus.vhiep.cyberzone.ui.store.homepage;

import com.n8plus.vhiep.cyberzone.R;
import com.n8plus.vhiep.cyberzone.data.model.Filter;
import com.n8plus.vhiep.cyberzone.data.model.FilterChild;
import com.n8plus.vhiep.cyberzone.data.model.Overview;
import com.n8plus.vhiep.cyberzone.data.model.Product;
import com.n8plus.vhiep.cyberzone.data.model.ProductImage;
import com.n8plus.vhiep.cyberzone.data.model.ProductType;
import com.n8plus.vhiep.cyberzone.data.model.SaleOff;
import com.n8plus.vhiep.cyberzone.data.model.Specification;
import com.n8plus.vhiep.cyberzone.data.model.Store;

import java.util.ArrayList;
import java.util.List;

public class HomePagePresenter implements HomePageContract.Presenter {
    private HomePageContract.View mHomePageView;
    private List<Product> products;

    public HomePagePresenter(HomePageContract.View mHomePageView) {
        this.mHomePageView = mHomePageView;
    }

    @Override
    public void loadData() {
        prepareData();
        mHomePageView.setAdapterProduct(products);
    }

    public void prepareData() {
        products = new ArrayList<>();
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

        List<Overview> overviews = new ArrayList<>();
        overviews.add(new Overview("", "ASRock trang bị cho H110M-DVS R2.0 chuẩn linh kiện Super Alloy bền bỉ - trước đây vốn chỉ xuất hiện trên các bo mạch chủ trung cấp và cao cấp thể hiện trong thông điệp Stable and Reliable - Ổn định và tin cậy."));

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

        Product product_1603653 = new Product("1603653", productType, store, "Bo mạch chính/ Mainboard Asrock H110M-DVS R2.0", "1320", specifications, overviews, "New", new SaleOff("1", 5));
        Product product_1600666 = new Product("1600666", productType, store, "Bo mạch chính/ Mainboard Gigabyte H110M-DS2 DDR4", "1465", specifications, overviews, "New", new SaleOff("1", 6));
        Product product_1701299 = new Product("1701299", productType, store, "Bo mạch chính/ Mainboard Gigabyte B250M-Gaming 3", "1899", specifications, overviews, "New", new SaleOff("1", 0));
        Product product_1704264 = new Product("1704264", productType, store, "Bo mạch chính/ Mainboard Msi A320M Bazooka", "2180", specifications, overviews, "New", new SaleOff("1", 0));
        Product product_1501266 = new Product("1501266", productType, store, "Bo mạch chính/ Mainboard Asus H81M-K", "1280", specifications, overviews, "New", new SaleOff("1", 0));

        products.add(product_1603653);
        products.add(product_1600666);
        products.add(product_1701299);
        products.add(product_1704264);
        products.add(product_1501266);

        products.add(product_1603653);
        products.add(product_1600666);
        products.add(product_1701299);
        products.add(product_1704264);
        products.add(product_1501266);

    }
}
