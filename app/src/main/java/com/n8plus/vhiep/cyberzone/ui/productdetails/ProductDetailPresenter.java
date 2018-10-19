package com.n8plus.vhiep.cyberzone.ui.productdetails;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.n8plus.vhiep.cyberzone.R;
import com.n8plus.vhiep.cyberzone.data.model.Category;
import com.n8plus.vhiep.cyberzone.data.model.Customer;
import com.n8plus.vhiep.cyberzone.data.model.Overview;
import com.n8plus.vhiep.cyberzone.data.model.Policy;
import com.n8plus.vhiep.cyberzone.data.model.Product;
import com.n8plus.vhiep.cyberzone.data.model.ProductImage;
import com.n8plus.vhiep.cyberzone.data.model.ProductType;
import com.n8plus.vhiep.cyberzone.data.model.PurchaseItem;
import com.n8plus.vhiep.cyberzone.data.model.ReviewProduct;
import com.n8plus.vhiep.cyberzone.data.model.Specification;
import com.n8plus.vhiep.cyberzone.data.model.Store;
import com.n8plus.vhiep.cyberzone.util.Constant;
import com.n8plus.vhiep.cyberzone.util.MySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class ProductDetailPresenter implements ProductDetailContract.Presenter {
    private ProductDetailContract.View mProductDetailView;
    private List<Policy> mPolicyList;
    private Product mProduct;
    private final String URL_PRODUCT = Constant.URL_HOST + "products";
    private final String URL_IMAGE = Constant.URL_HOST + "productImages";
    private final String URL_REVIEW = Constant.URL_HOST + "reviewProducts";
    private Gson gson;

    public ProductDetailPresenter(ProductDetailContract.View mProductDetailView) {
        this.mProductDetailView = mProductDetailView;
    }

    @Override
    public void loadProductDetail(Product product) {
        mProduct = product;
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("M/d/yy hh:mm a");
        gson = gsonBuilder.create();

        fakeDataPolicies();
        setReviewProduct(product.getReviewProducts());

        mProductDetailView.setImageList(product.getImageList() != null ? product.getImageList() : new ArrayList<ProductImage>());
        mProductDetailView.setProductName(product.getProductName());
        mProductDetailView.setProductPrice(product.getPrice(), product.getSaleOff() != null ? product.getSaleOff().getDiscount() : 0);
        mProductDetailView.setProductDiscount(product.getSaleOff() != null ? product.getSaleOff().getDiscount() : 0);
        mProductDetailView.setProductSpecification(product.getSpecifications() != null ? product.getSpecifications() : new ArrayList<Specification>());
        mProductDetailView.setProductOverviews(product.getOverviews() != null ? product.getOverviews() : new ArrayList<Overview>());
        mProductDetailView.setProductPolicies(mPolicyList);
        mProductDetailView.setStoreName(product.getStore().getStoreName());
        mProductDetailView.setStoreLocation(product.getStore().getLocation());

    }

    @Override
    public void buyNow() {
        Constant.purchaseList.clear();
        Constant.purchaseList.add(new PurchaseItem(mProduct, 1));
        mProductDetailView.moveToCart();
    }

    @Override
    public void addToCart() {
        int sizeOld = Constant.countProductInCart();
        int sizePurchaseList = Constant.purchaseList.size();
        if (sizePurchaseList > 0) {
            int unduplicated = 0;
            for (int i = 0; i < sizePurchaseList; i++) {
                if (mProduct.getProductId().equals(Constant.purchaseList.get(i).getProduct().getProductId())) {
                    Constant.purchaseList.get(i).setQuantity(Constant.purchaseList.get(i).getQuantity() + 1);
                } else {
                    unduplicated++;
                }
            }
            if (unduplicated == sizePurchaseList) {
                Constant.purchaseList.add(new PurchaseItem(mProduct, 1));
            }
        } else {
            Constant.purchaseList.add(new PurchaseItem(mProduct, 1));
        }

        mProductDetailView.addToCartAlert(Constant.countProductInCart() > sizeOld ? true : false);
        mProductDetailView.setCartMenuItem();
    }

    private void fakeDataPolicies() {
        mPolicyList = new ArrayList<>();
        mPolicyList.add(new Policy(R.drawable.delivery, "Miễn phí vận chuyển với đơn hàng từ 3 sản phẩm trở lên"));
        mPolicyList.add(new Policy(R.drawable.telephone, "Hỗ trợ online 24/7"));
        mPolicyList.add(new Policy(R.drawable.payment_method, "Thanh toán bảo mật"));
        mPolicyList.add(new Policy(R.drawable.dollar_symbol, "Chính sách đổi trả"));
    }

    private void setReviewProduct(List<ReviewProduct> reviewProducts) {
        if (reviewProducts != null && reviewProducts.size() > 0) {
            mProductDetailView.setReviewNone(false);
            mProductDetailView.setLayoutRatingProduct(true);
            mProductDetailView.setAdapterRatingProduct(reviewProducts);
            int count5 = 0, count4 = 0, count3 = 0, count2 = 0, count1 = 0;
            for (int i = 0; i < reviewProducts.size(); i++) {
                switch ((int) reviewProducts.get(i).getRatingStar().getRatingStar()) {
                    case 5:
                        count5++;
                        break;
                    case 4:
                        count4++;
                        break;
                    case 3:
                        count3++;
                        break;
                    case 2:
                        count2++;
                        break;
                    case 1:
                        count1++;
                        break;
                }
            }
            float rating = (5 * count5 + 4 * count4 + 3 * count3 + 2 * count2 + 1 * count1) / (count5 + count4 + count3 + count2 + count1);
            Log.i("RATING", count5 + " | " + count4 + " | " + count3 + " | " + count2 + " | " + count1 + " | " + String.valueOf(rating));
            mProductDetailView.setRatingBar(reviewProducts.size(), rating);
            mProductDetailView.setRating5star(reviewProducts.size(), count5);
            mProductDetailView.setRating4star(reviewProducts.size(), count4);
            mProductDetailView.setRating3star(reviewProducts.size(), count3);
            mProductDetailView.setRating2star(reviewProducts.size(), count2);
            mProductDetailView.setRating1star(reviewProducts.size(), count1);
        } else {
            mProductDetailView.setReviewNone(true);
            mProductDetailView.setLayoutRatingProduct(false);
        }
    }
}
