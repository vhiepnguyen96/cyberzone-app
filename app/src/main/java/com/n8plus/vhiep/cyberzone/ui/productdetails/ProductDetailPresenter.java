package com.n8plus.vhiep.cyberzone.ui.productdetails;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;

import com.n8plus.vhiep.cyberzone.R;
import com.n8plus.vhiep.cyberzone.data.model.Overview;
import com.n8plus.vhiep.cyberzone.data.model.Policy;
import com.n8plus.vhiep.cyberzone.data.model.Product;
import com.n8plus.vhiep.cyberzone.data.model.Specification;
import com.n8plus.vhiep.cyberzone.data.model.Store;

import java.util.ArrayList;
import java.util.List;

public class ProductDetailPresenter implements ProductDetailContract.Presenter {
    private ProductDetailContract.View mProductDetailView;
    private Product mProduct;
    private Store mStore;
    private Integer[] mProductImages;
    private ArrayList<Specification> mArraySpecifications;
    private List<Policy> mArrayPolicies;
    private List<Overview> mArrayOverviews;

    public ProductDetailPresenter(ProductDetailContract.View mProductDetailView) {
        this.mProductDetailView = mProductDetailView;
    }

    @Override
    public void loadData() {
        fakeData();
        loadImageProduct();
        mProductDetailView.initSlideImage(mProductImages);
        mProductDetailView.setProductName(mProduct.getName());
        mProductDetailView.setProductPrice(mProduct.getPrice(), mProduct.getDiscount());
        mProductDetailView.setProductDiscount(mProduct.getDiscount());
        mProductDetailView.setProductSpecification(mProduct.getSpecifications());
        mProductDetailView.setProductOverviews(mProduct.getOverviews());
        mProductDetailView.setProductPolicies(mArrayPolicies);
        mProductDetailView.setStoreName(mStore.getStoreName());
        mProductDetailView.setStoreLocation(mStore.getLocation());
    }

    public void loadImageProduct() {
        mProductImages = new Integer[]{R.drawable.img_1603653_1, R.drawable.img_1603653_2, R.drawable.img_1603653_3};
    }

    private void fakeData() {
        mArraySpecifications = new ArrayList<>();
        mArraySpecifications.add(new Specification("Bảo hành (tháng)", "36"));
        mArraySpecifications.add(new Specification("Thương hiệu", "Asrock"));
        mArraySpecifications.add(new Specification("Model", "H110M-DVS R2.0"));
        mArraySpecifications.add(new Specification("Loại", "Micro-ATX"));
        mArraySpecifications.add(new Specification("Loại Socket", "LGA 1151"));
        mArraySpecifications.add(new Specification("Chipset", "Intel H110"));
        mArraySpecifications.add(new Specification("Số khe Ram", "2"));
        mArraySpecifications.add(new Specification("Dung lượng Ram tối đa", "32GB"));
        mArraySpecifications.add(new Specification("Loại Ram", "DDR4 2133"));
        mArraySpecifications.add(new Specification("Audio", "7.1 Channels"));
        mArraySpecifications.add(new Specification("Số cổng HDMI", "Không có"));
        mArraySpecifications.add(new Specification("Số cổng DVI", "Không có"));

        mArrayOverviews = new ArrayList<>();
        mArrayOverviews.add(new Overview("", "ASRock trang bị cho H110M-DVS R2.0 chuẩn linh kiện Super Alloy bền bỉ - trước đây vốn chỉ xuất hiện trên các bo mạch chủ trung cấp và cao cấp thể hiện trong thông điệp Stable and Reliable - Ổn định và tin cậy."));
        mArrayOverviews.add(new Overview("", "Phụ kiện đi kèm cơ bản nhưng đầy đủ: gồm sách hướng dẫn, đĩa cài driver, I/O ( miếng chặn main) và 2 cáp SATA3 6Gb/s."));
        mArrayOverviews.add(new Overview("", "H110M-DVS R2.0 có size M-ATX thường thấy của phân khúc mainboard giá thấp. Với cái nhìn đầu tiên, chiếc mainboard khá đẹp mắt với tone mạch màu đen bóng, có thêm sắc cam từ miếng tản nhiệt chipset."));
        mArrayOverviews.add(new Overview("", "ASRock sử dụng công nghệ Super Alloy cho các sản phẩm mainboard của mình, PCB của bo mạch chủ được xây dựng từ sợi thủy tinh mật độ cao (High Density Glass Fabric PCB) giúp cho sản phẩm giảm thiểu hư hỏng hoặc rò rỉ điện khi gặp môi trường có độ ẩm cao - đặc biệt thật sự cần thiết cho thị trường Việt Nam, một quốc gia với khí hậu nóng ẩm."));
        mProduct = new Product("1603653", "Bo mạch chính/ Mainboard Asrock H110M-DVS R2.0", R.drawable.img_1603653_1, mArraySpecifications, "10.320", mArrayOverviews, "New", 5);

        mArrayPolicies = new ArrayList<>();
        mArrayPolicies.add(new Policy(R.drawable.delivery, "Miễn phí vận chuyển với đơn hàng từ 500.000 ₫ trở lên"));
        mArrayPolicies.add(new Policy(R.drawable.telephone, "Hỗ trợ online 24/7"));
        mArrayPolicies.add(new Policy(R.drawable.payment_method, "Thanh toán bảo mật"));
        mArrayPolicies.add(new Policy(R.drawable.dollar_symbol, "Chính sách đổi trả"));

        mStore = new Store("st1", "Máy tính Phong Vũ", "Nguyễn Văn Vũ", "Hà Nội");
    }
}
