package com.n8plus.vhiep.cyberzone.ui.product;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.bignerdranch.expandablerecyclerview.Model.ParentObject;
import com.n8plus.vhiep.cyberzone.R;
import com.n8plus.vhiep.cyberzone.data.model.Filter;
import com.n8plus.vhiep.cyberzone.data.model.FilterChild;
import com.n8plus.vhiep.cyberzone.data.model.Overview;
import com.n8plus.vhiep.cyberzone.data.model.Product;
import com.n8plus.vhiep.cyberzone.data.model.Specification;
import com.n8plus.vhiep.cyberzone.ui.product.adapter.FilterExpandableAdapter;
import com.n8plus.vhiep.cyberzone.ui.product.adapter.ProductVerticalAdapter;

import java.util.ArrayList;
import java.util.List;

public class ProductPresenter implements ProductContract.Presenter {
    private ProductContract.View mProductView;
    private ArrayList<Product> products;
    private ArrayList<ParentObject> parentObjects;

    public ProductPresenter(ProductContract.View mProductView) {
        this.mProductView = mProductView;
    }

    @Override
    public void loadData() {
        prepareData();
        mProductView.setAdapterProduct(products);
        mProductView.generateFilters(parentObjects);
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

        Product product_1603653 = new Product("1603653", "Bo mạch chính/ Mainboard Asrock H110M-DVS R2.0", R.drawable.img_1603653_1, specifications, "1.320", overviews, "New", 5);
        Product product_1600666 = new Product("1600666", "Bo mạch chính/ Mainboard Gigabyte H110M-DS2 DDR4", R.drawable.img_1600666, specifications, "1.465", overviews, "New", 6);
        Product product_1701299 = new Product("1701299", "Bo mạch chính/ Mainboard Gigabyte B250M-Gaming 3", R.drawable.img_1701299, specifications, "1.899", overviews, "New", 0);
        Product product_1704264 = new Product("1704264", "Bo mạch chính/ Mainboard Msi A320M Bazooka", R.drawable.img_1704264, specifications, "2.180", overviews, "New", 0);
        Product product_1501266 = new Product("1501266", "Bo mạch chính/ Mainboard Asus H81M-K", R.drawable.img_1501266, specifications, "1.280", overviews, "New", 0);

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

        List<Object> categoryItem = new ArrayList<>();
        categoryItem.add(new FilterChild("CPU Intel", true));
        categoryItem.add(new FilterChild("CPU Intel i3", false));
        categoryItem.add(new FilterChild("CPU Intel i5", false));
        categoryItem.add(new FilterChild("CPU Intel i7", false));
        categoryItem.add(new FilterChild("CPU Intel i9", false));

        List<Object> brandItem = new ArrayList<>();
        brandItem.add(new FilterChild("AMD", false));
        brandItem.add(new FilterChild("Intel", true));

        List<Object> priceItem = new ArrayList<>();
        priceItem.add(new FilterChild("0đ -> 1.000.000đ", true));
        priceItem.add(new FilterChild("0đ -> 1.000.000đ", true));
        priceItem.add(new FilterChild("0đ -> 1.000.000đ", true));
        priceItem.add(new FilterChild("0đ -> 1.000.000đ", true));
        priceItem.add(new FilterChild("0đ -> 1.000.000đ", true));

        parentObjects = new ArrayList<>();
        parentObjects.add(new Filter("Danh mục", categoryItem));
        parentObjects.add(new Filter("Thương hiệu", brandItem));
        parentObjects.add(new Filter("Mức giá", priceItem));
    }
}
