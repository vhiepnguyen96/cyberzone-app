package com.n8plus.vhiep.cyberzone.ui.manage.myreview.notwritereview.writereview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.n8plus.vhiep.cyberzone.R;
import com.n8plus.vhiep.cyberzone.data.model.Address;
import com.n8plus.vhiep.cyberzone.data.model.Customer;
import com.n8plus.vhiep.cyberzone.data.model.DeliveryPrice;
import com.n8plus.vhiep.cyberzone.data.model.Order;
import com.n8plus.vhiep.cyberzone.data.model.OrderState;
import com.n8plus.vhiep.cyberzone.data.model.Overview;
import com.n8plus.vhiep.cyberzone.data.model.PaymentMethod;
import com.n8plus.vhiep.cyberzone.data.model.Product;
import com.n8plus.vhiep.cyberzone.data.model.ProductImage;
import com.n8plus.vhiep.cyberzone.data.model.ProductType;
import com.n8plus.vhiep.cyberzone.data.model.PurchaseItem;
import com.n8plus.vhiep.cyberzone.data.model.RatingLevel;
import com.n8plus.vhiep.cyberzone.data.model.RatingStar;
import com.n8plus.vhiep.cyberzone.data.model.ReviewStore;
import com.n8plus.vhiep.cyberzone.data.model.SaleOff;
import com.n8plus.vhiep.cyberzone.data.model.Specification;
import com.n8plus.vhiep.cyberzone.data.model.Store;
import com.n8plus.vhiep.cyberzone.ui.manage.myorders.allorder.AllOrderContract;
import com.n8plus.vhiep.cyberzone.util.Constant;
import com.n8plus.vhiep.cyberzone.util.MySingleton;
import com.n8plus.vhiep.cyberzone.util.VolleyUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class WriteReviewPresenter implements WriteReviewContract.Presenter {
    private final String TAG = "WriteReviewPresenter";
    private Context context;
    private WriteReviewContract.View mWriteReviewView;
    private PurchaseItem mOrderItem;
    private Gson gson;
    private List<RatingStar> mRatingStars;
    private List<RatingLevel> mRatingLevels;
    private int success = 0;

    public WriteReviewPresenter(@NonNull final Context context, @NonNull final WriteReviewContract.View mWriteReviewView) {
        this.context = context;
        this.mWriteReviewView = mWriteReviewView;
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("M/d/yy hh:mm a");
        gson = gsonBuilder.create();
    }

    @Override
    public void loadDataReview(PurchaseItem orderItem) {
        mOrderItem = orderItem;
        mWriteReviewView.setProductName(mOrderItem.getProduct().getProductName());
        mWriteReviewView.setCustomerName(Constant.customer.getName());
        if (mOrderItem.getProduct().getStore() != null)
            mWriteReviewView.setStoreName(mOrderItem.getProduct().getStore().getStoreName());
        loadRatingStar();
        loadRatingLevel();
    }

    @Override
    public void loadRatingStar() {
        mRatingStars = new ArrayList<>();
        VolleyUtil.GET(context, Constant.URL_RATING_STAR,
                response -> {
                    try {
                        mRatingStars = Arrays.asList(gson.fromJson(String.valueOf(response.getJSONArray("ratingStars")), RatingStar[].class));
                        Log.d(TAG, "GET: " + mRatingStars.size() + " ratingStars");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> Log.e(TAG, error.toString()));
//        JsonObjectRequest ratingStarRequest = new JsonObjectRequest(Request.Method.GET, Constant.URL_RATING_STAR, null,
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        try {
//                            mRatingStars = Arrays.asList(gson.fromJson(String.valueOf(response.getJSONArray("ratingStars")), RatingStar[].class));
//                            Log.d("WriteReviewPresenter", "GET: " + mRatingStars.size() + " ratingStars");
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Log.e("WriteReviewPresenter", error.toString());
//                    }
//                });
//        MySingleton.getInstance(((Fragment) mWriteReviewView).getContext().getApplicationContext()).addToRequestQueue(ratingStarRequest);
    }

    @Override
    public void loadRatingLevel() {
        mRatingLevels = new ArrayList<>();
        VolleyUtil.GET(context, Constant.URL_RATING_LEVEL,
                response -> {
                    try {
                        mRatingLevels = Arrays.asList(gson.fromJson(String.valueOf(response.getJSONArray("ratingLevels")), RatingLevel[].class));
                        Log.d(TAG, "GET: " + mRatingLevels.size() + " ratingLevels");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> Log.e(TAG, error.toString()));
//        JsonObjectRequest ratingLevelRequest = new JsonObjectRequest(Request.Method.GET, Constant.URL_RATING_LEVEL, null,
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        try {
//                            mRatingLevels = Arrays.asList(gson.fromJson(String.valueOf(response.getJSONArray("ratingLevels")), RatingLevel[].class));
//                            Log.d("WriteReviewPresenter", "GET: " + mRatingLevels.size() + " ratingLevels");
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Log.e("WriteReviewPresenter", error.toString());
//                    }
//                });
//        MySingleton.getInstance(((Fragment) mWriteReviewView).getContext().getApplicationContext()).addToRequestQueue(ratingLevelRequest);
    }

    @Override
    public void sendReview(String reviewProduct, int rateProductStar, String reviewStore, int rateStoreLevel) {
        // Send review product
        Log.d(TAG, "rateProductStar: " + rateProductStar);
        JSONObject reviewProductObj = new JSONObject();
        try {
            reviewProductObj.put("customerId", Constant.customer.getId());

            JSONObject productObj = new JSONObject();
            productObj.put("_id", mOrderItem.getProduct().getProductId());
            productObj.put("productName", mOrderItem.getProduct().getProductName());
            productObj.put("imageURL", mOrderItem.getProduct().getImageDefault());

            reviewProductObj.put("product", productObj);
            if (getIdRatingStar(rateProductStar) != null) {
                reviewProductObj.put("ratingStarId", getIdRatingStar(rateProductStar));
            }
            reviewProductObj.put("review", reviewProduct);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "reviewProduct: " + reviewProductObj.toString());

        VolleyUtil.POST(context, Constant.URL_REVIEW_PRODUCT, reviewProductObj,
                response -> {
                    success++;
                    if (success == 2) {
                        updateIsReviewOrderItem(mOrderItem.getId());
                    }
                },
                error -> {
                    mWriteReviewView.sendReviewResult(false);
                    Log.e(TAG, error.toString());
                });

        // Send review store
        Log.d(TAG, "rateStoreLevel: " + rateStoreLevel);
        JSONObject reviewStoreObj = new JSONObject();
        try {
            reviewStoreObj.put("customerId", Constant.customer.getId());
            reviewStoreObj.put("storeId", mOrderItem.getProduct().getStore().getStoreId());
            if (getIdRatingLevel(rateStoreLevel) != null) {
                reviewStoreObj.put("ratingLevelId", getIdRatingLevel(rateStoreLevel));
            }
            reviewStoreObj.put("review", reviewStore);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "reviewStore: " + reviewStoreObj.toString());

        VolleyUtil.POST(context, Constant.URL_REVIEW_STORE, reviewStoreObj,
                response -> {
                    success++;
                    if (success == 2) {
                        updateIsReviewOrderItem(mOrderItem.getId());
                    }
                },
                error -> {
                    mWriteReviewView.sendReviewResult(false);
                    Log.e(TAG, error.toString());
                });
    }

    @Override
    public String getIdRatingStar(int star) {
        if (mRatingStars != null && mRatingStars.size() > 0) {
            for (RatingStar ratingStar : mRatingStars) {
                if (ratingStar.getRatingStar() == star) {
                    return ratingStar.getId();
                }
            }
        }
        return null;
    }

    @Override
    public String getIdRatingLevel(int level) {
        if (mRatingLevels != null && mRatingLevels.size() > 0) {
            for (RatingLevel ratingLevel : mRatingLevels) {
                if (ratingLevel.getLevel() == level) {
                    return ratingLevel.getId();
                }
            }
        }
        return null;
    }

    @Override
    public void updateIsReviewOrderItem(String orderItemId) {
        JSONArray updateArr = new JSONArray();
        JSONObject isReviewObj = new JSONObject();
        try {
            isReviewObj.put("propName", "isReview");
            isReviewObj.put("value", true);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        updateArr.put(isReviewObj);

        VolleyUtil.PATCH(context, Constant.URL_ORDER_ITEM + "/" + orderItemId, updateArr,
                response -> {
                    mWriteReviewView.sendReviewResult(true);
                    mWriteReviewView.backAllPurchase();
                },
                error -> {
                    mWriteReviewView.sendReviewResult(false);
                });
    }
}
