package com.n8plus.vhiep.cyberzone.ui.store.information;

import android.app.Activity;
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
import com.n8plus.vhiep.cyberzone.data.model.Product;
import com.n8plus.vhiep.cyberzone.data.model.ProductImage;
import com.n8plus.vhiep.cyberzone.data.model.ReviewStore;
import com.n8plus.vhiep.cyberzone.data.model.Store;
import com.n8plus.vhiep.cyberzone.util.Constant;
import com.n8plus.vhiep.cyberzone.util.MySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class InformationPresenter implements InformationContract.Presenter {

    private InformationContract.View mInformationStoreView;
    private Store mStore;
    private Gson gson;
    private List<Category> mCategoryList;
    private final String URL_STORE = Constant.URL_HOST + "stores";
    private final String URL_REVIEW_STORE = Constant.URL_HOST + "reviewStores";
    private int mLimit = 10;

    public InformationPresenter(InformationContract.View mInformationStoreView) {
        this.mInformationStoreView = mInformationStoreView;
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("M/d/yy hh:mm a");
        gson = gsonBuilder.create();
    }

    @Override
    public void loadData(Store store) {
        mStore = store;
        loadStoreInfomation(mStore.getStoreId());
        loadStoreReviews(mStore.getStoreId());
    }

    @Override
    public void loadStoreInfomation(String storeId) {
        JsonObjectRequest storeRequest = new JsonObjectRequest(Request.Method.GET, URL_STORE + "/" + storeId, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    mStore = gson.fromJson(String.valueOf(response.getJSONObject("store")), Store.class);
                    Log.i("StorePresenter", "storeResult: " + mStore.getStoreName() + " | " + mStore.getCategories().size());
                    mInformationStoreView.setStoreLocation(mStore.getLocation());
                    mInformationStoreView.setTimeInSystem(mStore.getTimeInSystem());

                    // Load popular category
                    JSONArray categories = response.getJSONObject("store").getJSONArray("categories");
                    mCategoryList = new ArrayList<>();
                    for (int i = 0; i < categories.length(); i++) {
                        mCategoryList.add(gson.fromJson(String.valueOf(categories.getJSONObject(i).getJSONObject("category")), Category.class));
                    }
                    if (mCategoryList.size() > 0) {
                        mInformationStoreView.setPopularCategory(getPopularCategory(mCategoryList));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("StorePresenter", error.toString());
            }
        });
        MySingleton.getInstance(((Fragment) mInformationStoreView).getContext().getApplicationContext()).addToRequestQueue(storeRequest);
    }

    @Override
    public void loadStoreReviews(String storeId) {
        JsonObjectRequest storeReviewRequest = new JsonObjectRequest(Request.Method.GET, URL_REVIEW_STORE + "/store/" + storeId, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            List<ReviewStore> reviewStores = Arrays.asList(gson.fromJson(String.valueOf(response.getJSONArray("reviewStores")), ReviewStore[].class));
                            Log.i("StorePresenter", "GET: " + reviewStores.size() + " reviewStores");
                            if (reviewStores.size() > 0) {
                                mStore.setReviewStores(reviewStores);
                                loadStoreReviews(mLimit);
                                mInformationStoreView.setPositiveReview(String.valueOf(mStore.getPositiveReview()));
                                mInformationStoreView.setTotalReview(String.valueOf(mStore.getReviewStores().size()));
                                mInformationStoreView.setGoodReview(mStore.getReviewStores().size(), mStore.getCountLevel(1));
                                mInformationStoreView.setNormalReview(mStore.getReviewStores().size(), mStore.getCountLevel(2));
                                mInformationStoreView.setNotGoodReview(mStore.getReviewStores().size(), mStore.getCountLevel(3));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("StorePresenter", error.toString());
                    }
                });
        MySingleton.getInstance(((Fragment) mInformationStoreView).getContext().getApplicationContext()).addToRequestQueue(storeReviewRequest);
    }

    @Override
    public void loadStoreReviews(int limit) {
        if (mStore.getReviewStores().size() > limit) {
            mInformationStoreView.setAdapterCustomerReview(getLimitStoreReviews(limit));
            mInformationStoreView.setCountMoreReview(String.valueOf(mStore.getReviewStores().size() - limit));
        } else {
            mInformationStoreView.setAdapterCustomerReview(mStore.getReviewStores());
            mInformationStoreView.setCountMoreReview("0");
        }
    }

    @Override
    public List<ReviewStore> getLimitStoreReviews(int limit) {
        List<ReviewStore> reviewStores = new ArrayList<>();
        for (int i = 0; i < limit; i++) {
            reviewStores.add(mStore.getReviewStores().get(i));
        }
        return reviewStores;
    }

    @Override
    public void prepareDataMoreReview() {
        if (mStore.getReviewStores().size() < mLimit){
            mInformationStoreView.alertNoMoreReview();
        } else {
            mInformationStoreView.showMoreReview(mStore.getReviewStores());
        }
    }

    @Override
    public String getPopularCategory(List<Category> categories) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < categories.size(); i++) {
            if (i < categories.size() - 1) {
                builder.append(categories.get(i).getName() + ", ");
            } else {
                builder.append(categories.get(i).getName());
            }
        }
        return builder.toString();
    }
}
