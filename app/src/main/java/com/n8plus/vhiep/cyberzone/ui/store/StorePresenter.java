package com.n8plus.vhiep.cyberzone.ui.store;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.n8plus.vhiep.cyberzone.data.model.FollowStore;
import com.n8plus.vhiep.cyberzone.data.model.Product;
import com.n8plus.vhiep.cyberzone.data.model.ReviewStore;
import com.n8plus.vhiep.cyberzone.data.model.Store;
import com.n8plus.vhiep.cyberzone.util.Constant;
import com.n8plus.vhiep.cyberzone.util.MySingleton;
import com.n8plus.vhiep.cyberzone.util.VolleyUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

public class StorePresenter implements StoreContract.Presenter {
    private static final String TAG = "StorePresenter";
    private Context context;
    private StoreContract.View mStoreView;
    private Store mStore;
    private Gson gson;

    public StorePresenter(@NonNull final Context context, @NonNull final StoreContract.View mStoreView) {
        this.context = context;
        this.mStoreView = mStoreView;
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("M/d/yy hh:mm a");
        gson = gsonBuilder.create();
    }

    @Override
    public void loadData(Store store) {
        mStore = store;
        mStoreView.setStoreName(mStore.getStoreName());
        loadFollowStore(mStore.getStoreId());
        if (Constant.customer != null) {
            checkFollowStore(mStore.getStoreId(), Constant.customer.getId());
        }
        loadStoreReviews(mStore.getStoreId());
    }

    @Override
    public void loadFollowStore(String storeId) {
        VolleyUtil.GET(context, Constant.URL_FOLLOW_STORE + "/store/" + storeId,
                response -> {
                    try {
                        List<FollowStore> followStores = Arrays.asList(gson.fromJson(String.valueOf(response.getJSONArray("followStores")), FollowStore[].class));
                        Log.i(TAG, "GET: " + followStores.size() + " followStores");
                        if (followStores.size() > 0) {
                            mStoreView.setFollowCounter(mStore.getStringCountFollow(followStores.size()));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> Log.e(TAG, error.toString()));
//        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, Constant.URL_FOLLOW_STORE + "/store/" + storeId, null,
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        try {
//                            List<FollowStore> followStores = Arrays.asList(gson.fromJson(String.valueOf(response.getJSONArray("followStores")), FollowStore[].class));
//                            Log.i("StorePresenter", "GET: " + followStores.size() + " followStores");
//                            if (followStores.size() > 0) {
//                                mStoreView.setFollowCounter(mStore.getStringCountFollow(followStores.size()));
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Log.e("StorePresenter", error.toString());
//                    }
//                });
//        MySingleton.getInstance(((Activity) mStoreView).getApplicationContext()).addToRequestQueue(request);
    }

    @Override
    public void checkFollowStore(String storeId, String customerId) {
        VolleyUtil.GET(context, Constant.URL_FOLLOW_STORE + "/check/" + Constant.customer.getId() + "/" + mStore.getStoreId(),
                response -> {
                    Log.i(TAG, response.toString());
                    mStoreView.setFollowStoreResult(true);
                },
                error -> {
                    Log.e(TAG, error.toString());
                    mStoreView.setFollowStoreResult(false);
                });
//        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, Constant.URL_FOLLOW_STORE + "/check/" + Constant.customer.getId() + "/" + mStore.getStoreId(),
//                null,
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        Log.i("ProductDetailPresenter", response.toString());
//                        mStoreView.setFollowStoreResult(true);
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Log.e("ProductDetailPresenter", error.toString());
//                        mStoreView.setFollowStoreResult(false);
//                    }
//                });
//        MySingleton.getInstance(((Activity) mStoreView).getApplicationContext()).addToRequestQueue(request);
    }

    @Override
    public void loadStoreReviews(String storeId) {
        VolleyUtil.GET(context, Constant.URL_REVIEW_STORE + "/store/" + storeId,
                response -> {
                    try {
                        List<ReviewStore> reviewStores = Arrays.asList(gson.fromJson(String.valueOf(response.getJSONArray("reviewStores")), ReviewStore[].class));
                        Log.i(TAG, "GET: " + reviewStores.size() + " reviewStores");
                        if (reviewStores.size() > 0) {
                            mStore.setReviewStores(reviewStores);
                            mStoreView.setPositiveReview(String.valueOf(mStore.getPositiveReview()));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error ->  Log.e(TAG, error.toString()));
//        JsonObjectRequest storeReviewRequest = new JsonObjectRequest(Request.Method.GET, Constant.URL_REVIEW_STORE + "/store/" + storeId, null,
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        try {
//                            List<ReviewStore> reviewStores = Arrays.asList(gson.fromJson(String.valueOf(response.getJSONArray("reviewStores")), ReviewStore[].class));
//                            Log.i("StorePresenter", "GET: " + reviewStores.size() + " reviewStores");
//                            if (reviewStores.size() > 0) {
//                                mStore.setReviewStores(reviewStores);
//                                mStoreView.setPositiveReview(String.valueOf(mStore.getPositiveReview()));
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Log.e("StorePresenter", error.toString());
//                    }
//                });
//        MySingleton.getInstance(((Activity) mStoreView).getApplicationContext()).addToRequestQueue(storeReviewRequest);
    }

    @Override
    public void followStore() {
        JSONObject follow = new JSONObject();
        try {
            follow.put("customerId", Constant.customer.getId());
            follow.put("storeId", mStore.getStoreId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        VolleyUtil.POST(context, Constant.URL_FOLLOW_STORE, follow,
                response -> {
                    Log.i(TAG, response.toString());
                    mStoreView.followStoreResult(true);
                    mStoreView.setFollowStoreResult(true);
                    loadFollowStore(mStore.getStoreId());
                },
                error -> {
                    Log.e(TAG, error.toString());
                    mStoreView.followStoreResult(false);
                });
//        JsonObjectRequest followRequest = new JsonObjectRequest(Request.Method.POST, Constant.URL_FOLLOW_STORE, follow,
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        Log.i("ProductDetailPresenter", response.toString());
//                        mStoreView.followStoreResult(true);
//                        mStoreView.setFollowStoreResult(true);
//                        loadFollowStore(mStore.getStoreId());
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Log.e("ProductDetailPresenter", error.toString());
//                        mStoreView.followStoreResult(false);
//                    }
//                });
//        MySingleton.getInstance(((Activity) mStoreView).getApplicationContext()).addToRequestQueue(followRequest);
    }

    @Override
    public void unfollowStore() {
        VolleyUtil.DELETE(context, Constant.URL_FOLLOW_STORE + "/" + Constant.customer.getId() + "/" + mStore.getStoreId(),
                response -> {
                    Log.i(TAG, response.toString());
                    mStoreView.unfollowStoreResult(true);
                    mStoreView.setFollowStoreResult(false);
                    loadFollowStore(mStore.getStoreId());
                },
                error -> {
                    Log.e(TAG, error.toString());
                    mStoreView.unfollowStoreResult(false);
                });
//        JsonObjectRequest followRequest = new JsonObjectRequest(Request.Method.DELETE, Constant.URL_FOLLOW_STORE + "/" + Constant.customer.getId() + "/" + mStore.getStoreId(), null,
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        Log.i("ProductDetailPresenter", response.toString());
//                        mStoreView.unfollowStoreResult(true);
//                        mStoreView.setFollowStoreResult(false);
//                        loadFollowStore(mStore.getStoreId());
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Log.e("ProductDetailPresenter", error.toString());
//                        mStoreView.unfollowStoreResult(false);
//                    }
//                });
//        MySingleton.getInstance(((Activity) mStoreView).getApplicationContext()).addToRequestQueue(followRequest);
    }
}
