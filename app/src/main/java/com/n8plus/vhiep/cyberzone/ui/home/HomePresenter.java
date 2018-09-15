package com.n8plus.vhiep.cyberzone.ui.home;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.n8plus.vhiep.cyberzone.R;
import com.n8plus.vhiep.cyberzone.data.model.Category;
import com.n8plus.vhiep.cyberzone.data.model.Overview;
import com.n8plus.vhiep.cyberzone.data.model.Product;
import com.n8plus.vhiep.cyberzone.data.model.ProductType;
import com.n8plus.vhiep.cyberzone.data.model.Specification;
import com.n8plus.vhiep.cyberzone.ui.home.adapter.PopularCategoryAdapter;
import com.n8plus.vhiep.cyberzone.ui.home.adapter.ProductHorizontalAdapter;

import java.util.ArrayList;
import java.util.List;

public class HomePresenter implements HomeContract.Presenter {

    private final HomeContract.View mHomeView;
    private ArrayList<Product> products;
    private ArrayList<ProductType> productTypes;

    public HomePresenter(HomeContract.View mHomeView) {
        this.mHomeView = mHomeView;
    }

    @Override
    public void loadData() {
        fakeData();
        mHomeView.setAdapterNewArrival(products);
        mHomeView.setAdapterOnSale(products);
        mHomeView.setAdapterBestSeller(products);
        mHomeView.setAdapterPopularCategory(productTypes);
    }

    private void fakeData(){
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

        Product product_1603653 = new Product("1603653", "Bo mạch chính/ Mainboard Asrock H110M-DVS R2.0", R.drawable.img_1603653_1, specifications, "1.320", overviews, "New", 0);
        Product product_1600666 = new Product("1600666", "Bo mạch chính/ Mainboard Gigabyte H110M-DS2 DDR4", R.drawable.img_1600666, specifications, "1.465", overviews, "New", 5);
        Product product_1701299 = new Product("1701299", "Bo mạch chính/ Mainboard Gigabyte B250M-Gaming 3", R.drawable.img_1701299, specifications, "1.899", overviews, "New", 5);
        Product product_1704264 = new Product("1704264", "Bo mạch chính/ Mainboard Msi A320M Bazooka", R.drawable.img_1704264, specifications, "2.180", overviews, "New", 5);
        Product product_1501266 = new Product("1501266", "Bo mạch chính/ Mainboard Asus H81M-K", R.drawable.img_1501266, specifications, "1.280", overviews, "New", 5);

        products.add(product_1603653);
        products.add(product_1600666);
        products.add(product_1701299);
        products.add(product_1704264);
        products.add(product_1501266);

        productTypes = new ArrayList<>();
        productTypes.add(new ProductType("Bộ xử lý - CPU", R.drawable.processor));
        productTypes.add(new ProductType("Bo mạch chủ", R.drawable.motherboard));
        productTypes.add(new ProductType("Card màn hình", R.drawable.vga_card));
        productTypes.add(new ProductType("Nguồn - PSU", R.drawable.psu));
        productTypes.add(new ProductType("Thùng máy - Case", R.drawable.case_pc));
        productTypes.add(new ProductType("Tản nhiệt", R.drawable.radial_fan));
        productTypes.add(new ProductType("Màn hình", R.drawable.monitor));
        productTypes.add(new ProductType("RAM", R.drawable.memory));
        productTypes.add(new ProductType("SSD & HDD", R.drawable.ssd_hdd));
        productTypes.add(new ProductType("Chuột & Bàn phím", R.drawable.keyboard));
        productTypes.add(new ProductType("Webcam", R.drawable.webcam));
    }
}
