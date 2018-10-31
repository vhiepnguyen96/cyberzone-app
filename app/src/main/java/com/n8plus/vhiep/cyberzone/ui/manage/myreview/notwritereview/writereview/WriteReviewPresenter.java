package com.n8plus.vhiep.cyberzone.ui.manage.myreview.notwritereview.writereview;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class WriteReviewPresenter implements WriteReviewContract.Presenter {
    private WriteReviewContract.View mWriteReviewView;
    private PurchaseItem mOrderItem;
    private Gson gson;
    private List<RatingStar> mRatingStars;
    private List<RatingLevel> mRatingLevels;
    private final String URL_RATING_STAR = Constant.URL_HOST + "ratingStars";
    private final String URL_RATING_LEVEL = Constant.URL_HOST + "ratingLevels";
    private final String URL_REVIEW_PRODUCT = Constant.URL_HOST + "reviewProducts";
    private final String URL_REVIEW_STORE = Constant.URL_HOST + "reviewStores";
    private final String URL_ORDER_ITEM = Constant.URL_HOST + "orderItems";
    private int success = 0;

    public WriteReviewPresenter(WriteReviewContract.View mWriteReviewView) {
        this.mWriteReviewView = mWriteReviewView;
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("M/d/yy hh:mm a");
        gson = gsonBuilder.create();
    }

    @Override
    public void loadDataReview(PurchaseItem orderItem) {
        mOrderItem = orderItem;
        mWriteReviewView.setProductName(mOrderItem.getProduct().getProductName());
        mWriteReviewView.setStoreName(mOrderItem.getProduct().getStore().getStoreName());
        mWriteReviewView.setCustomerName(Constant.customer.getName());
        Log.d("WriteReviewPresenter", "orderId: " + mOrderItem.getOrderId());
        loadRatingStar();
        loadRatingLevel();
    }

    @Override
    public void loadRatingStar() {
        mRatingStars = new ArrayList<>();
        JsonObjectRequest ratingStarRequest = new JsonObjectRequest(Request.Method.GET, URL_RATING_STAR, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            mRatingStars = Arrays.asList(gson.fromJson(String.valueOf(response.getJSONArray("ratingStars")), RatingStar[].class));
                            Log.d("WriteReviewPresenter", "GET: " + mRatingStars.size() + " ratingStars");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("WriteReviewPresenter", error.toString());
                    }
                });
        MySingleton.getInstance(((Fragment) mWriteReviewView).getContext().getApplicationContext()).addToRequestQueue(ratingStarRequest);
    }

    @Override
    public void loadRatingLevel() {
        mRatingLevels = new ArrayList<>();
        JsonObjectRequest ratingLevelRequest = new JsonObjectRequest(Request.Method.GET, URL_RATING_LEVEL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            mRatingLevels = Arrays.asList(gson.fromJson(String.valueOf(response.getJSONArray("ratingLevels")), RatingLevel[].class));
                            Log.d("WriteReviewPresenter", "GET: " + mRatingLevels.size() + " ratingLevels");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("WriteReviewPresenter", error.toString());
                    }
                });
        MySingleton.getInstance(((Fragment) mWriteReviewView).getContext().getApplicationContext()).addToRequestQueue(ratingLevelRequest);
    }

    @Override
    public void sendReview(String reviewProduct, int rateProductStar, String reviewStore, int rateStoreLevel) {
        Log.d("WriteReviewPresenter", "rateProductStar: " + rateProductStar);
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
        Log.d("WriteReviewPresenter", "reviewProduct: " + reviewProductObj.toString());
        JsonObjectRequest reviewProductRequest = new JsonObjectRequest(Request.Method.POST, URL_REVIEW_PRODUCT, reviewProductObj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        success++;
                        if (success == 2) {
                            updateIsReviewOrderItem(mOrderItem.getId());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mWriteReviewView.sendReviewResult(false);
                        Log.e("WriteReviewPresenter", error.toString());
                    }
                });
        MySingleton.getInstance(((Fragment) mWriteReviewView).getContext().getApplicationContext()).addToRequestQueue(reviewProductRequest);

        Log.d("WriteReviewPresenter", "rateStoreLevel: " + rateStoreLevel);
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
        Log.d("WriteReviewPresenter", "reviewStore: " + reviewStoreObj.toString());
        JsonObjectRequest reviewStoreRequest = new JsonObjectRequest(Request.Method.POST, URL_REVIEW_STORE, reviewStoreObj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        success++;
                        if (success == 2) {
                            updateIsReviewOrderItem(mOrderItem.getId());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mWriteReviewView.sendReviewResult(false);
                        Log.e("WriteReviewPresenter", error.toString());
                    }
                });
        MySingleton.getInstance(((Fragment) mWriteReviewView).getContext().getApplicationContext()).addToRequestQueue(reviewStoreRequest);
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
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.PATCH, URL_ORDER_ITEM + "/" + orderItemId, updateArr,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        mWriteReviewView.sendReviewResult(true);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mWriteReviewView.sendReviewResult(false);
                    }
                });
        MySingleton.getInstance(((Fragment) mWriteReviewView).getContext().getApplicationContext()).addToRequestQueue(request);
    }
}
