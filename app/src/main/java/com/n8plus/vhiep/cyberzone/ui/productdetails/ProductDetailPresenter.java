package com.n8plus.vhiep.cyberzone.ui.productdetails;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.Toast;

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
import com.n8plus.vhiep.cyberzone.util.VolleyUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class ProductDetailPresenter implements ProductDetailContract.Presenter {
    private static final String TAG = "ProductDetailPresenter";
    private Context context;
    private ProductDetailContract.View mProductDetailView;
    private List<Policy> mPolicyList;
    private Product mProduct;
    private Gson gson;
    private int mCurrentQuantity = 0;

    public ProductDetailPresenter(@NonNull final Context context, @NonNull final ProductDetailContract.View mProductDetailView) {
        this.context = context;
        this.mProductDetailView = mProductDetailView;
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("M/d/yy hh:mm a");
        gson = gsonBuilder.create();
    }

    @Override
    public void loadProductDetail(Product product) {
        mProduct = product;
        // Set current quantity in cart
        mCurrentQuantity = Constant.countProductQuantity(product);
        Log.d(TAG, "mCurrentQuantity: "+mCurrentQuantity);

        DecimalFormat df = new DecimalFormat("#.000");

        fakeDataPolicies();
        if (Constant.customer != null) {
            loadWishList();
        }

        setReviewProduct(product.getReviewProducts());

        mProductDetailView.setImageList(product.getImageList() != null ? product.getImageList() : new ArrayList<ProductImage>());
        mProductDetailView.setProductName(product.getProductName());

        mProductDetailView.setLayoutAddToCart(product.getQuantity());

        if (product.getSaleOff() != null) {
            if (product.getSaleOff().getDiscount() > 0) {
                int basicPrice = Integer.valueOf(product.getPrice());
                int discount = product.getSaleOff().getDiscount();
                int salePrice = basicPrice - (basicPrice * discount / 100);

                mProductDetailView.setProductPrice(basicPrice > 1000 ? df.format(Product.convertToPrice(String.valueOf(basicPrice))).replace(",", ".") : String.valueOf(basicPrice));
                mProductDetailView.setLayoutPriceSale(true);
                mProductDetailView.setProductPriceSale(salePrice > 1000 ? df.format(Product.convertToPrice(String.valueOf(salePrice))).replace(",", ".") : String.valueOf(salePrice));
                mProductDetailView.setProductDiscount(product.getSaleOff().getDiscount());
            } else {
                mProductDetailView.setProductPriceSale(Integer.valueOf(product.getPrice()) > 1000 ? df.format(Product.convertToPrice(product.getPrice())).replace(",", ".") : product.getPrice());
                mProductDetailView.setLayoutPriceSale(false);
            }
        } else {
            mProductDetailView.setProductPriceSale(Integer.valueOf(product.getPrice()) > 1000 ? df.format(Product.convertToPrice(product.getPrice())).replace(",", ".") : product.getPrice());
            mProductDetailView.setLayoutPriceSale(false);
        }

        mProductDetailView.setProductSpecification(product.getSpecifications() != null ? product.getSpecifications() : new ArrayList<Specification>());
        mProductDetailView.setProductOverviews(product.getOverviews() != null ? product.getOverviews() : new ArrayList<Overview>());
        mProductDetailView.setProductPolicies(mPolicyList);
        mProductDetailView.setStoreName(product.getStore().getStoreName());
        mProductDetailView.setStoreLocation(product.getStore().getLocation());

    }

    @Override
    public void loadWishList() {
        VolleyUtil.GET(context, Constant.URL_WISHLIST + "/check/" + Constant.customer.getId() + "/" + mProduct.getProductId(),
                response -> {
                    Log.i(TAG, response.toString());
                    mProductDetailView.setWishListResult(true);
                },
                error -> {
                    Log.e(TAG, error.toString());
                    mProductDetailView.setWishListResult(false);
                });
//        JsonObjectRequest wishListRequest = new JsonObjectRequest(Request.Method.GET, Constant.URL_WISHLIST + "/check/" + Constant.customer.getId() + "/" + mProduct.getProductId(), null,
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        Log.i("ProductDetailPresenter", response.toString());
//                        mProductDetailView.setWishListResult(true);
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Log.e("ProductDetailPresenter", error.toString());
//                        mProductDetailView.setWishListResult(false);
//                    }
//                });
//        MySingleton.getInstance(((Activity) mProductDetailView).getApplicationContext()).addToRequestQueue(wishListRequest);
    }

    @Override
    public int getCurrentQuantity() {
        return mProduct.getQuantity();
    }

    @Override
    public void addToWishList() {
        JSONObject wishList = new JSONObject();
        try {
            wishList.put("customerId", Constant.customer.getId());
            wishList.put("productId", mProduct.getProductId());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        VolleyUtil.POST(context,  Constant.URL_WISHLIST, wishList,
                response -> {
                    Log.i(TAG, response.toString());
                    mProductDetailView.addToWishListResult(true);
                },
                error -> {
                    Log.e(TAG, error.toString());
                    mProductDetailView.addToWishListResult(false);
                });
//        JsonObjectRequest wishListRequest = new JsonObjectRequest(Request.Method.POST, Constant.URL_WISHLIST, wishList,
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        Log.i("ProductDetailPresenter", response.toString());
//                        mProductDetailView.addToWishListResult(true);
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Log.e("ProductDetailPresenter", error.toString());
//                        mProductDetailView.addToWishListResult(false);
//                    }
//                });
//        MySingleton.getInstance(((Activity) mProductDetailView).getApplicationContext()).addToRequestQueue(wishListRequest);
    }

    @Override
    public void removeFromWishList() {
        VolleyUtil.DELETE(context,Constant.URL_WISHLIST + "/" + Constant.customer.getId() + "/" + mProduct.getProductId(),
                response -> {
                    Log.i(TAG, response.toString());
                    mProductDetailView.removeFromWishListResult(true);
                },
                error -> {
                    Log.e(TAG, error.toString());
                    mProductDetailView.removeFromWishListResult(false);
                });
//        JsonObjectRequest wishListRequest = new JsonObjectRequest(Request.Method.DELETE, Constant.URL_WISHLIST + "/" + Constant.customer.getId() + "/" + mProduct.getProductId(), null,
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        Log.i("ProductDetailPresenter", response.toString());
//                        mProductDetailView.removeFromWishListResult(true);
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Log.e("ProductDetailPresenter", error.toString());
//                        mProductDetailView.removeFromWishListResult(false);
//                    }
//                });
//        MySingleton.getInstance(((Activity) mProductDetailView).getApplicationContext()).addToRequestQueue(wishListRequest);
    }

    @Override
    public void buyNow() {
        Constant.purchaseList.clear();
        Constant.purchaseList.add(new PurchaseItem(mProduct, 1));
        mCurrentQuantity = 1;
        mProductDetailView.moveToCart();
    }

    @Override
    public void addToCart() {
        // Kiểm tra số lượng còn lại của sản phẩm
        if (mCurrentQuantity >= mProduct.getQuantity()) {
            mProductDetailView.setAlert("Bạn đã thêm tối đa sản phẩm!");
        } else {
            int sizeOld = Constant.countProductInCart();
            int sizePurchaseList = Constant.purchaseList.size();
            if (sizePurchaseList > 0) {
                int unduplicated = 0;
                for (int i = 0; i < sizePurchaseList; i++) {
                    if (mProduct.getProductId().equals(Constant.purchaseList.get(i).getProduct().getProductId())) {
                        Constant.purchaseList.get(i).setQuantity(Constant.purchaseList.get(i).getQuantity() + 1);
                        mCurrentQuantity++;
                    } else {
                        unduplicated++;
                    }
                }
                if (unduplicated == sizePurchaseList) {
                    Constant.purchaseList.add(new PurchaseItem(mProduct, 1));
                    mCurrentQuantity++;
                }
            } else {
                Constant.purchaseList.add(new PurchaseItem(mProduct, 1));
                mCurrentQuantity++;
            }
            mProductDetailView.addToCartAlert(Constant.countProductInCart() > sizeOld ? true : false);

            mProductDetailView.setCartMenuItem();
        }
    }

    @Override
    public void prepareDataStore() {
        mProductDetailView.moveToStore(mProduct.getStore());
    }

    private void fakeDataPolicies() {
        mPolicyList = new ArrayList<>();
        mPolicyList.add(new Policy(R.drawable.delivery, "Miễn phí vận chuyển với đơn hàng từ 1.000.000 đ trở lên"));
        mPolicyList.add(new Policy(R.drawable.telephone, "Hỗ trợ online 24/7"));
        mPolicyList.add(new Policy(R.drawable.payment_method, "Thanh toán bảo mật"));
        mPolicyList.add(new Policy(R.drawable.dollar_symbol, "Chính sách đổi trả"));
    }

    private void setReviewProduct(List<ReviewProduct> reviewProducts) {
        if (reviewProducts != null && reviewProducts.size() > 0) {
            mProductDetailView.setReviewNone(false);
            mProductDetailView.setLayoutRatingProduct(true);
            mProductDetailView.setAdapterRatingProduct(reviewProducts);

            mProductDetailView.setRatingBar(reviewProducts.size(), mProduct.getAverageReview());
            Log.i("ProductDetailPresenter", "AverageReview: " + mProduct.getAverageReview());
            mProductDetailView.setRating5star(reviewProducts.size(), mProduct.getCountStar(5));
            mProductDetailView.setRating4star(reviewProducts.size(), mProduct.getCountStar(4));
            mProductDetailView.setRating3star(reviewProducts.size(), mProduct.getCountStar(3));
            mProductDetailView.setRating2star(reviewProducts.size(), mProduct.getCountStar(2));
            mProductDetailView.setRating1star(reviewProducts.size(), mProduct.getCountStar(1));
        } else {
            mProductDetailView.setReviewNone(true);
            mProductDetailView.setLayoutRatingProduct(false);
        }
    }
}
