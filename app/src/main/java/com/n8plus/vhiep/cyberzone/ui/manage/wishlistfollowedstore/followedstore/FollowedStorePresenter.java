package com.n8plus.vhiep.cyberzone.ui.manage.wishlistfollowedstore.followedstore;

import android.support.v4.app.Fragment;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.n8plus.vhiep.cyberzone.data.model.Account;
import com.n8plus.vhiep.cyberzone.data.model.Category;
import com.n8plus.vhiep.cyberzone.data.model.Customer;
import com.n8plus.vhiep.cyberzone.data.model.FollowStore;
import com.n8plus.vhiep.cyberzone.data.model.Product;
import com.n8plus.vhiep.cyberzone.data.model.Store;
import com.n8plus.vhiep.cyberzone.data.model.WishList;
import com.n8plus.vhiep.cyberzone.ui.manage.wishlistfollowedstore.wishlist.WishListContract;
import com.n8plus.vhiep.cyberzone.util.Constant;
import com.n8plus.vhiep.cyberzone.util.MySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class FollowedStorePresenter implements FollowedStoreContract.Presenter {
    private FollowedStoreContract.View mFollowedStoreView;
    private List<FollowStore> mFollowStores;
    private Gson gson;
    private final String URL_FOLLOW_STORE = Constant.URL_HOST + "followStores";
    private final String URL_CATEGORY = Constant.URL_HOST + "categories";

    public FollowedStorePresenter(FollowedStoreContract.View mFollowedStoreView) {
        this.mFollowedStoreView = mFollowedStoreView;
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("M/d/yy hh:mm a");
        gson = gsonBuilder.create();
    }

    @Override
    public void loadData() {
        loadFollowStore(Constant.customer.getId());
    }

    @Override
    public void loadFollowStore(String customerId) {
        mFollowStores = new ArrayList<>();
        JsonObjectRequest wishListRequest = new JsonObjectRequest(Request.Method.GET, URL_FOLLOW_STORE + "/customer/" + customerId, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            mFollowStores = Arrays.asList(gson.fromJson(String.valueOf(response.getJSONArray("followStores")), FollowStore[].class));
                            Log.d("FollowedStorePresenter", "GET followStores: " + mFollowStores.size());
                            if (mFollowStores.size() > 0) {
                                mFollowedStoreView.setLayoutNone(false);
                                mFollowedStoreView.setAdapterFollowStore(mFollowStores);
                                for (int i = 0; i < mFollowStores.size(); i++) {
                                    JSONArray categories = response.getJSONArray("followStores").getJSONObject(i).getJSONObject("store").getJSONArray("categories");
                                    final List<Category> categoryList = new ArrayList<>();
                                    for (int j = 0; j < categories.length(); j++) {
                                        JsonObjectRequest categoryRequest = new JsonObjectRequest(Request.Method.GET, URL_CATEGORY + "/" + categories.getJSONObject(j).getString("category"), null, new Response.Listener<JSONObject>() {
                                            @Override
                                            public void onResponse(JSONObject response) {
                                                try {
                                                    Category category = gson.fromJson(String.valueOf(response.getJSONObject("category")), Category.class);
                                                    categoryList.add(category);
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }, new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                Log.e("FollowedStorePresenter", error.toString());
                                            }
                                        });
                                        MySingleton.getInstance(((Fragment) mFollowedStoreView).getContext().getApplicationContext()).addToRequestQueue(categoryRequest);
                                    }
                                    mFollowStores.get(i).getStore().setCategories(categoryList);
                                    mFollowedStoreView.setNotifyDataSetChanged();
                                }
                            } else {
                                mFollowedStoreView.setLayoutNone(true);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mFollowedStoreView.setLayoutNone(true);
                    }
                });
        MySingleton.getInstance(((Fragment) mFollowedStoreView).getContext().getApplicationContext()).addToRequestQueue(wishListRequest);
    }

    @Override
    public void goToStore(int position) {
        mFollowedStoreView.moveToStore(mFollowStores.get(position).getStore());
    }

    @Override
    public void unfollowStore(int position) {
        JsonObjectRequest followRequest = new JsonObjectRequest(Request.Method.DELETE, URL_FOLLOW_STORE + "/" + Constant.customer.getId() + "/" + mFollowStores.get(position).getStore().getStoreId(), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("ProductDetailPresenter", response.toString());
                        mFollowedStoreView.unfollowStoreAlert(true);
                        mFollowedStoreView.setNotifyDataSetChanged();
                        loadData();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("ProductDetailPresenter", error.toString());
                        mFollowedStoreView.unfollowStoreAlert(false);
                    }
                });
        MySingleton.getInstance(((Fragment) mFollowedStoreView).getContext().getApplicationContext()).addToRequestQueue(followRequest);
    }
}
