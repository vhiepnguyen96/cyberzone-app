package com.n8plus.vhiep.cyberzone.ui.store.homepage;

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
import com.n8plus.vhiep.cyberzone.data.model.Filter;
import com.n8plus.vhiep.cyberzone.data.model.FilterChild;
import com.n8plus.vhiep.cyberzone.data.model.Overview;
import com.n8plus.vhiep.cyberzone.data.model.Product;
import com.n8plus.vhiep.cyberzone.data.model.ProductImage;
import com.n8plus.vhiep.cyberzone.data.model.ProductType;
import com.n8plus.vhiep.cyberzone.data.model.ReviewProduct;
import com.n8plus.vhiep.cyberzone.data.model.SaleOff;
import com.n8plus.vhiep.cyberzone.data.model.Specification;
import com.n8plus.vhiep.cyberzone.data.model.Store;
import com.n8plus.vhiep.cyberzone.util.Constant;
import com.n8plus.vhiep.cyberzone.util.MySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class HomePagePresenter implements HomePageContract.Presenter {
    private int GRID_LAYOUT = 1, LINEAR_LAYOUT = 2;
    private HomePageContract.View mHomePageView;
    private Store mStore;
    private List<Product> mProductList;
    private Date mCurentTime;
    private final String URL_PRODUCT = Constant.URL_HOST + "products";
    private final String URL_IMAGE = Constant.URL_HOST + "productImages";
    private final String URL_REVIEW = Constant.URL_HOST + "reviewProducts";
    private final String URL_TIME = "http://api.geonames.org/timezoneJSON?formatted=true&lat=10.041791&lng=105.747099&username=cyberzone&style=full";
    private Gson gson;

    public HomePagePresenter(HomePageContract.View mHomePageView) {
        this.mHomePageView = mHomePageView;
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("M/d/yy hh:mm a");
        gson = gsonBuilder.create();
    }

    @Override
    public void loadData(Store store) {
        mStore = store;
        loadStoreProduct(mStore.getStoreId());
    }

    @Override
    public void loadStoreProduct(String storeId) {
        mProductList = new ArrayList<>();
        JsonObjectRequest productRequestTypeId = new JsonObjectRequest(Request.Method.GET, URL_PRODUCT + "/store/" + storeId, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            mProductList = Arrays.asList(gson.fromJson(String.valueOf(response.getJSONArray("products")), Product[].class));
                            Log.i("HomePagePresenter", "GET: " + mProductList.size() + " products");
                            if (mProductList.size() > 0) {
                                mHomePageView.setAdapterProduct(mProductList, GRID_LAYOUT);
                                fetchCurrentTime();
                                for (int i = 0; i < mProductList.size(); i++) {
                                    fetchImageProduct(i);
                                    fetchReviewProduct(i);
                                    mHomePageView.setNotifyDataSetChanged();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("HomePagePresenter", error.toString());
                    }
                });
        MySingleton.getInstance(((Fragment) mHomePageView).getContext().getApplicationContext()).addToRequestQueue(productRequestTypeId);
    }

    @Override
    public void changeProductGridLayout() {
        mHomePageView.setAdapterProduct(mProductList, GRID_LAYOUT);
    }

    @Override
    public void changeProductLinearLayout() {
        mHomePageView.setAdapterProduct(mProductList, LINEAR_LAYOUT);
    }

    public void fetchImageProduct(final int position) {
        JsonObjectRequest requestImage = new JsonObjectRequest(Request.Method.GET, URL_IMAGE + "/product/" + mProductList.get(position).getProductId(), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            List<ProductImage> imageList = Arrays.asList(gson.fromJson(String.valueOf(response.getJSONArray("imageList")), ProductImage[].class));
                            Log.d("HomePagePresenter", "Product images: " + imageList.size());
                            if (imageList.size() > 0) {
                                mProductList.get(position).setImageList(imageList);
                                mHomePageView.setNotifyDataSetChanged();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("HomePagePresenter", error.toString());
                    }
                });
        MySingleton.getInstance(((Fragment) mHomePageView).getContext().getApplicationContext()).addToRequestQueue(requestImage);
    }

    public void fetchReviewProduct(final int position) {
        JsonObjectRequest requestRating = new JsonObjectRequest(Request.Method.GET, URL_REVIEW + "/product/" + mProductList.get(position).getProductId(), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            List<ReviewProduct> reviewProducts = Arrays.asList(gson.fromJson(String.valueOf(response.getJSONArray("reviewProducts")), ReviewProduct[].class));
                            Log.d("HomePagePresenter", "Review product: " + reviewProducts.size());
                            if (reviewProducts.size() > 0) {
                                mProductList.get(position).setReviewProducts(reviewProducts);
                                mHomePageView.setNotifyDataSetChanged();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("HomePagePresenter", error.toString());
                    }
                });
        MySingleton.getInstance(((Fragment) mHomePageView).getContext().getApplicationContext()).addToRequestQueue(requestRating);
    }

    public void fetchCurrentTime() {
        JsonObjectRequest timeRequest = new JsonObjectRequest(Request.Method.GET, URL_TIME, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                            sdf.setTimeZone(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
                            mCurentTime = sdf.parse(response.getString("time"));
                            Log.d("HomePagePresenter", "Current time: " + sdf.format(mCurentTime).toString());
                            for (int i = 0; i < mProductList.size(); i++) {
                                if (mProductList.get(i).getSaleOff() != null) {
                                    if (mProductList.get(i).getSaleOff().getDateStart().getTime() > mCurentTime.getTime() || mProductList.get(i).getSaleOff().getDateEnd().getTime() < mCurentTime.getTime()) {
                                        mProductList.get(i).getSaleOff().setDiscount(0);
                                    }
                                }
                            }
                            mHomePageView.setNotifyDataSetChanged();
                        } catch (ParseException | JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.w("HomePagePresenter", "onErrorResponse: " + error.getMessage());
            }
        });
        MySingleton.getInstance(((Fragment) mHomePageView).getContext().getApplicationContext()).addToRequestQueue(timeRequest);
    }
}
