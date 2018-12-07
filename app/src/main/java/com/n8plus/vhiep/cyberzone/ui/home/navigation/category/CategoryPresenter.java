package com.n8plus.vhiep.cyberzone.ui.home.navigation.category;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.n8plus.vhiep.cyberzone.R;
import com.n8plus.vhiep.cyberzone.data.model.Category;
import com.n8plus.vhiep.cyberzone.ui.home.adapter.CategoryAdapter;
import com.n8plus.vhiep.cyberzone.ui.manage.mydeliveryaddress.adddeliveryaddress.AddDeliveryAddressContract;
import com.n8plus.vhiep.cyberzone.util.Constant;
import com.n8plus.vhiep.cyberzone.util.MySingleton;
import com.n8plus.vhiep.cyberzone.util.VolleyUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CategoryPresenter implements CategoryContract.Presenter {
    private static final String TAG = "CategoryPresenter";
    private Context context;
    private final CategoryContract.View mCategoryView;
    private List<Category> categories;
    private Gson gson;

    public CategoryPresenter(@NonNull final Context context, @NonNull final CategoryContract.View mCategoryView) {
        this.context = context;
        this.mCategoryView = mCategoryView;
        categories = new ArrayList<>();
    }

    @Override
    public void loadData() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("M/d/yy hh:mm a");
        gson = gsonBuilder.create();
        fetchData();
    }

    private void fetchData() {
        VolleyUtil.GET(context, Constant.URL_CATEGORY,
                response -> {
                    try {
                        categories = Arrays.asList(gson.fromJson(String.valueOf(response.getJSONArray("categories")), Category[].class));
                        mCategoryView.setAdapterCategory(categories);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> Log.e(TAG, error.toString()));
    }
}
