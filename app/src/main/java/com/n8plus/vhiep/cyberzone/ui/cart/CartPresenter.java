package com.n8plus.vhiep.cyberzone.ui.cart;

import com.n8plus.vhiep.cyberzone.R;
import com.n8plus.vhiep.cyberzone.data.model.Overview;
import com.n8plus.vhiep.cyberzone.data.model.Product;
import com.n8plus.vhiep.cyberzone.data.model.Specification;

import java.util.ArrayList;
import java.util.List;

public class CartPresenter implements CartContract.Presenter {
    private CartContract.View mCartView;
    private List<Product> productList;

    public CartPresenter(CartContract.View mCartView) {
        this.mCartView = mCartView;
    }

    @Override
    public void loadData() {
        prepareProductData();
        mCartView.setAdapterCart(productList);
    }

    private void prepareProductData() {
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

        Product product_1603653 = new Product("1603653", "Bo mạch chính/ Mainboard Asrock H110M-DVS R2.0", R.drawable.img_1603653_1, specifications, "1.320", overviews, "New", 5);
        Product product_1600666 = new Product("1600666", "Bo mạch chính/ Mainboard Gigabyte H110M-DS2 DDR4", R.drawable.img_1600666, specifications, "1.465", overviews, "New", 6);
        Product product_1701299 = new Product("1701299", "Bo mạch chính/ Mainboard Gigabyte B250M-Gaming 3", R.drawable.img_1701299, specifications, "1.899", overviews, "New", 0);
        Product product_1704264 = new Product("1704264", "Bo mạch chính/ Mainboard Msi A320M Bazooka", R.drawable.img_1704264, specifications, "2.180", overviews, "New", 0);
        Product product_1501266 = new Product("1501266", "Bo mạch chính/ Mainboard Asus H81M-K", R.drawable.img_1501266, specifications, "1.280", overviews, "New", 0);

        productList = new ArrayList<>();
        productList.add(product_1603653);
        productList.add(product_1600666);
        productList.add(product_1701299);
        productList.add(product_1704264);
        productList.add(product_1501266);

    }
}
