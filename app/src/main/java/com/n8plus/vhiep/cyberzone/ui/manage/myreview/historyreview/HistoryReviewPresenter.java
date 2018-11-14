package com.n8plus.vhiep.cyberzone.ui.manage.myreview.historyreview;

import android.support.v4.app.Fragment;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.n8plus.vhiep.cyberzone.R;
import com.n8plus.vhiep.cyberzone.data.model.Address;
import com.n8plus.vhiep.cyberzone.data.model.Customer;
import com.n8plus.vhiep.cyberzone.data.model.HistoryReview;
import com.n8plus.vhiep.cyberzone.data.model.Order;
import com.n8plus.vhiep.cyberzone.data.model.OrderState;
import com.n8plus.vhiep.cyberzone.data.model.Overview;
import com.n8plus.vhiep.cyberzone.data.model.Product;
import com.n8plus.vhiep.cyberzone.data.model.ProductImage;
import com.n8plus.vhiep.cyberzone.data.model.ProductType;
import com.n8plus.vhiep.cyberzone.data.model.PurchaseItem;
import com.n8plus.vhiep.cyberzone.data.model.RatingLevel;
import com.n8plus.vhiep.cyberzone.data.model.RatingStar;
import com.n8plus.vhiep.cyberzone.data.model.ReviewProduct;
import com.n8plus.vhiep.cyberzone.data.model.ReviewStore;
import com.n8plus.vhiep.cyberzone.data.model.Specification;
import com.n8plus.vhiep.cyberzone.data.model.Store;
import com.n8plus.vhiep.cyberzone.ui.manage.myorders.allorder.AllOrderContract;
import com.n8plus.vhiep.cyberzone.util.Constant;
import com.n8plus.vhiep.cyberzone.util.MySingleton;

import org.joda.time.DateTime;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class HistoryReviewPresenter implements HistoryReviewContract.Presenter {
    private HistoryReviewContract.View mHistoryReviewView;
    private List<ReviewProduct> mReviewProducts;
    private List<ReviewStore> mReviewStores;
    private List<HistoryReview> mHistoryReviews;
    private Gson gson;
    private final String URL_REVIEW_PRODUCT = Constant.URL_HOST + "reviewProducts";
    private final String URL_REVIEW_STORE = Constant.URL_HOST + "reviewStores";
    private final String URL_IMAGE = Constant.URL_HOST + "productImages";
    private int review = 0;

    public HistoryReviewPresenter(HistoryReviewContract.View mHistoryReviewView) {
        this.mHistoryReviewView = mHistoryReviewView;
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("M/d/yy hh:mm a");
        gson = gsonBuilder.create();
    }

    @Override
    public void loadData() {
        loadHistoryReview(Constant.customer.getId());
    }

    @Override
    public void loadHistoryReview(String customerId) {
        mReviewStores = new ArrayList<>();
        mReviewProducts = new ArrayList<>();

        JsonObjectRequest reviewStoreRequest = new JsonObjectRequest(Request.Method.GET, URL_REVIEW_STORE + "/customer/" + customerId, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            mReviewStores = Arrays.asList(gson.fromJson(String.valueOf(response.getJSONArray("reviewStores")), ReviewStore[].class));
                            Log.i("HistoryReviewPresenter", "GET: " + mReviewStores.size() + " reviewStores");
                            review++;
                            if (review == 2) {
                                setDataHistoryReview(mReviewProducts, mReviewStores);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("HistoryReviewPresenter", error.toString());
                    }
                });
        MySingleton.getInstance(((Fragment) mHistoryReviewView).getContext().getApplicationContext()).addToRequestQueue(reviewStoreRequest);

        JsonObjectRequest reviewProductRequest = new JsonObjectRequest(Request.Method.GET, URL_REVIEW_PRODUCT + "/customer/" + customerId, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            mReviewProducts = Arrays.asList(gson.fromJson(String.valueOf(response.getJSONArray("reviewProducts")), ReviewProduct[].class));
                            Log.i("HistoryReviewPresenter", "GET: " + mReviewProducts.size() + " reviewProducts");
                            review++;
                            if (review == 2) {
                                setDataHistoryReview(mReviewProducts, mReviewStores);
                            }
//                            for (int i = 0; i < mReviewProducts.size(); i++) {
//                                if (mReviewProducts.get(i).getProduct() != null) {
//                                    loadImageProduct(i);
//                                }
//                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("HistoryReviewPresenter", error.toString());
                    }
                });
        MySingleton.getInstance(((Fragment) mHistoryReviewView).getContext().getApplicationContext()).addToRequestQueue(reviewProductRequest);
    }

    @Override
    public void loadImageProduct(final int position) {
        JsonObjectRequest requestImage = new JsonObjectRequest(Request.Method.GET, URL_IMAGE + "/product/" + mReviewProducts.get(position).getProduct().getProductId(), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    List<ProductImage> imageList = Arrays.asList(gson.fromJson(String.valueOf(response.getJSONArray("imageList")), ProductImage[].class));
                    Log.d("ProductPresenter", "Product images: " + imageList.size());
                    if (imageList.size() > 0) {
                        mReviewProducts.get(position).getProduct().setImageList(imageList);
                        mHistoryReviewView.setNotifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("ProductPresenter", error.toString());
            }
        });
        MySingleton.getInstance(((Fragment) mHistoryReviewView).getContext().getApplicationContext()).addToRequestQueue(requestImage);
    }


    @Override
    public void setDataHistoryReview(List<ReviewProduct> reviewProducts, List<ReviewStore> reviewStores) {
        mHistoryReviews = new ArrayList<>();
        for (int i = 0; i < reviewStores.size(); i++) {
            for (int j = 0; j < reviewProducts.size(); j++) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                try {
                    String dateReviewStoreStr = sdf.format(reviewStores.get(i).getDateReview());
                    String dateReviewProductStr = sdf.format(reviewProducts.get(j).getDateReview());

                    Date dateReviewStore = sdf.parse(dateReviewStoreStr);
                    Date dateReviewProduct = sdf.parse(dateReviewProductStr);

                    if (dateReviewStore.compareTo(dateReviewProduct) == 0) {
                        mHistoryReviews.add(new HistoryReview(dateReviewStore, reviewStores.get(i), reviewProducts.get(j)));
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
        mHistoryReviewView.setLayoutReviewNone(mHistoryReviews.size() > 0 ? false : true);
        mHistoryReviewView.setAdapterHistoryReview(mHistoryReviews);
    }

//    private void prepareData() {
//        Store store = new Store("5b989eb9a6bce5234c9522ea", "Máy tính Phong Vũ");
////        List<ProductImage> imageList_1603653 = new ArrayList<>();
////        imageList_1603653.add(new ProductImage("5b98a6a6fe67871b2068add0", R.drawable.img_1603653_1));
//        Product product = new Product("5b974fbf6153321ffc61b829", "Bo mạch chính/ Mainboard Asrock H110M-DVS R2.0");
//
//        ReviewStore reviewStore = new ReviewStore("5b98a6a6fe67871b2068add0", new Customer("dâf", "Nguyễn Văn Hiệp"), store, new RatingLevel("sfafa", 1, "Tốt"), "Hàng tốt, đóng gói cẩn thận!", Calendar.getInstance().getTime());
//        ReviewProduct reviewProduct = new ReviewProduct("5b98a6a6fe67871b2068add1", new Customer("dâf", "Nguyễn Văn Hiệp"), product, new RatingStar("sfafsfa", 5, "5 SAO"), "Sản phẩm ổn trong tầm giá", new Date());
//
//        mHistoryReviewList = new ArrayList<>();
//        mHistoryReviewList.add(new HistoryReview(reviewStore, reviewProduct));
//        mHistoryReviewList.add(new HistoryReview(reviewStore, reviewProduct));
//        mHistoryReviewList.add(new HistoryReview(reviewStore, reviewProduct));
//    }
}
