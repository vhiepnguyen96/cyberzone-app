package com.n8plus.vhiep.cyberzone.ui.manage.myorders.allorder;

import com.n8plus.vhiep.cyberzone.R;
import com.n8plus.vhiep.cyberzone.data.model.Address;
import com.n8plus.vhiep.cyberzone.data.model.Order;
import com.n8plus.vhiep.cyberzone.data.model.Overview;
import com.n8plus.vhiep.cyberzone.data.model.Product;
import com.n8plus.vhiep.cyberzone.data.model.ProductImage;
import com.n8plus.vhiep.cyberzone.data.model.ProductType;
import com.n8plus.vhiep.cyberzone.data.model.PurchaseItem;
import com.n8plus.vhiep.cyberzone.data.model.SaleOff;
import com.n8plus.vhiep.cyberzone.data.model.Specification;
import com.n8plus.vhiep.cyberzone.data.model.Store;
import com.n8plus.vhiep.cyberzone.ui.manage.myorders.waitforpayment.WaitForPaymentContract;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AllOrderPresenter implements AllOrderContract.Presenter {
    private List<Order> orderList;
    private AllOrderContract.View mAllOrderView;

    public AllOrderPresenter(AllOrderContract.View mAllOrderView) {
        this.mAllOrderView = mAllOrderView;
    }

    @Override
    public void loadData() {
        prepareData();
        mAllOrderView.setAdapterAllOrder(orderList);
    }

    private void prepareData() {
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

        Product product_1603653 = new Product("1603653", productType, store, "Bo mạch chính/ Mainboard Asrock H110M-DVS R2.0", "1.320", specifications, overviews, "New", new SaleOff("1", 5));
        Product product_1600666 = new Product("1600666", productType, store, "Bo mạch chính/ Mainboard Gigabyte H110M-DS2 DDR4", "1.465", specifications, overviews, "New", new SaleOff("1", 6));

        List<PurchaseItem> purchaseItems = new ArrayList<>();
        purchaseItems.add(new PurchaseItem(product_1603653, 1));
        purchaseItems.add(new PurchaseItem(product_1600666, 2));

        Address address = new Address("a1", "Nguyễn Văn Hiệp", "01646158456", "Số nhà 100, Hẻm 138, Đường Trần Hưng Đạo, Phường An Nghiệp, Quận Ninh Kiều, TP Cần Thơ");

        orderList = new ArrayList<>();
        orderList.add(new Order("5b9b7430b18f6d1178239040", "5b962cd9738558095492b986", purchaseItems, 3, address, "0.020", "5.550", new Date(), "Đang chờ thanh toán"));
        orderList.add(new Order("5b9b7430b18f6d1178239041", "5b962cd9738558095492b986", purchaseItems, 3, address, "0.020", "5.550", new Date(), "Đã hủy đơn"));
        orderList.add(new Order("5b9b7430b18f6d1178239042", "5b962cd9738558095492b986", purchaseItems, 3, address, "0.020", "5.550", new Date(), "Đã giao hàng"));
    }
}
