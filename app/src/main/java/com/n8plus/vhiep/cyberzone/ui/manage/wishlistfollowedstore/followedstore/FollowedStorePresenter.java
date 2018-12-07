package com.n8plus.vhiep.cyberzone.ui.manage.wishlistfollowedstore.followedstore;

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
import com.n8plus.vhiep.cyberzone.util.VolleyUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class FollowedStorePresenter implements FollowedStoreContract.Presenter {
    private static final String TAG = "FollowedStorePresenter";
    private Context context;
    private FollowedStoreContract.View mFollowedStoreView;
    private List<FollowStore> mFollowStores;
    private Gson gson;

    public FollowedStorePresenter(@NonNull final Context context, @NonNull final FollowedStoreContract.View mFollowedStoreView) {
        this.context = context;
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
        mFollowedStoreView.setLayoutLoading(true);
        VolleyUtil.GET(context, Constant.URL_FOLLOW_STORE + "/customer/" + customerId,
                response -> {
                    try {
                        mFollowStores = Arrays.asList(gson.fromJson(String.valueOf(response.getJSONArray("followStores")), FollowStore[].class));
                        Log.d("FollowedStorePresenter", "GET followStores: " + mFollowStores.size());
                        mFollowedStoreView.setLayoutLoading(false);

                        if (mFollowStores.size() > 0) {
                            mFollowedStoreView.setLayoutNone(false);
                            mFollowedStoreView.setAdapterFollowStore(mFollowStores);
                            for (int i = 0; i < mFollowStores.size(); i++) {
                                JSONArray categories = response.getJSONArray("followStores").getJSONObject(i).getJSONObject("store").getJSONArray("categories");
                                final List<Category> categoryList = new ArrayList<>();
                                for (int j = 0; j < categories.length(); j++) {
                                    VolleyUtil.GET(context, Constant.URL_CATEGORY + "/" + categories.getJSONObject(j).getString("category"),
                                            response1 -> {
                                                try {
                                                    Category category = gson.fromJson(String.valueOf(response1.getJSONObject("category")), Category.class);
                                                    categoryList.add(category);
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            },
                                            error -> Log.e(TAG, error.toString()));
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
                },
                error -> {
                    mFollowedStoreView.setLayoutNone(true);
                    mFollowedStoreView.setLayoutLoading(false);
                });
    }

    @Override
    public void goToStore(int position) {
        mFollowedStoreView.moveToStore(mFollowStores.get(position).getStore());
    }

    @Override
    public void unfollowStore(int position) {
        VolleyUtil.DELETE(context, Constant.URL_FOLLOW_STORE + "/" + Constant.customer.getId() + "/" + mFollowStores.get(position).getStore().getStoreId(),
                response -> {
                    Log.i(TAG, response.toString());
                    mFollowedStoreView.unfollowStoreAlert(true);
                    mFollowedStoreView.setNotifyDataSetChanged();
                    loadData();
                },
                error -> {
                    Log.e(TAG, error.toString());
                    mFollowedStoreView.unfollowStoreAlert(false);
                });
    }
}
