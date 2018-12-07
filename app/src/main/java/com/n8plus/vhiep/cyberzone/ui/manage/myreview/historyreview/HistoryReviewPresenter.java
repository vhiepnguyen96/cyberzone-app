package com.n8plus.vhiep.cyberzone.ui.manage.myreview.historyreview;

import android.content.Context;
import android.support.annotation.NonNull;
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
import com.n8plus.vhiep.cyberzone.util.VolleyUtil;

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
    private static final String TAG = "HistoryReviewPresenter";
    private Context context;
    private HistoryReviewContract.View mHistoryReviewView;
    private List<ReviewProduct> mReviewProducts;
    private List<ReviewStore> mReviewStores;
    private List<HistoryReview> mHistoryReviews;
    private Gson gson;
    private int review = 0;

    public HistoryReviewPresenter(@NonNull final Context context, @NonNull final HistoryReviewContract.View mHistoryReviewView) {
        this.context = context;
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

        // Load review store
        VolleyUtil.GET(context, Constant.URL_REVIEW_STORE + "/customer/" + customerId,
                response -> {
                    try {
                        mReviewStores = Arrays.asList(gson.fromJson(String.valueOf(response.getJSONArray("reviewStores")), ReviewStore[].class));
                        Log.i(TAG, "GET: " + mReviewStores.size() + " reviewStores");
                        review++;
                        if (review == 2) {
                            setDataHistoryReview(mReviewProducts, mReviewStores);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error ->  Log.e(TAG, error.toString()));

        // Load review product
        VolleyUtil.GET(context,Constant.URL_REVIEW_PRODUCT + "/customer/" + customerId,
                response -> {
                    try {
                        mReviewProducts = Arrays.asList(gson.fromJson(String.valueOf(response.getJSONArray("reviewProducts")), ReviewProduct[].class));
                        Log.i(TAG, "GET: " + mReviewProducts.size() + " reviewProducts");
                        review++;
                        if (review == 2) {
                            setDataHistoryReview(mReviewProducts, mReviewStores);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> Log.e(TAG, error.toString()));
    }

    @Override
    public void loadImageProduct(final int position) {
        VolleyUtil.GET(context, Constant.URL_IMAGE + "/product/" + mReviewProducts.get(position).getProduct().getProductId(),
                response -> {
                    try {
                        List<ProductImage> imageList = Arrays.asList(gson.fromJson(String.valueOf(response.getJSONArray("imageList")), ProductImage[].class));
                        Log.d(TAG, "Product images: " + imageList.size());
                        if (imageList.size() > 0) {
                            mReviewProducts.get(position).getProduct().setImageList(imageList);
                            mHistoryReviewView.setNotifyDataSetChanged();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> Log.e(TAG, error.toString()));
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
        mHistoryReviewView.setLayoutLoading(false);
        mHistoryReviewView.setAdapterHistoryReview(mHistoryReviews);
    }
}
