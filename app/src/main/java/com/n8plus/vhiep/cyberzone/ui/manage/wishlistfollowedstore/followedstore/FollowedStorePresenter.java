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
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

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
                        loadFollowStore(Constant.customer.getId());
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

//    private void prepareData(){
//        List<Category> categories = new ArrayList<>();
//        categories.add(new Category("5b974fb26153321ffc61b828", "Linh kiện máy tính"));
//        categories.add(new Category("5b974fbf6153321ffc61b829", "Màn hình máy tính"));
//        categories.add(new Category("5b974fc86153321ffc61b82a", "Ổ cứng HDD/SSD"));
//
//        mStoreList = new ArrayList<>();
//        mStoreList.add(new Store("5b989eb9a6bce5234c9522ea", new Account("admin", "admin"), "Máy tính Phong Vũ", "Hồ Chí Minh", "0909159753", new Date(), categories));
//        mStoreList.add(new Store("5b989eb9a6bce5234c9522ea", new Account("admin", "admin"), "Phi Long Gaming", "Hồ Chí Minh", "0909159753", new Date(), categories));
//        mStoreList.add(new Store("5b989eb9a6bce5234c9522ea", new Account("admin", "admin"), "An Phát PC", "Đà Nẵng", "0909159753", new Date(), categories));
//        mStoreList.add(new Store("5b989eb9a6bce5234c9522ea", new Account("admin", "admin"), "Hà Nội Computer", "Hà Nội", "0909159753", new Date(), categories));
//        mStoreList.add(new Store("5b989eb9a6bce5234c9522ea", new Account("admin", "admin"), "Máy tính Duy Huỳnh", "Kiên Giang", "0909159753", new Date(), categories));
//
//    }
}
