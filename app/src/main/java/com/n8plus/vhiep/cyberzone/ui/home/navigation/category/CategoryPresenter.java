package com.n8plus.vhiep.cyberzone.ui.home.navigation.category;

import android.content.Context;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CategoryPresenter implements CategoryContract.Presenter {
    private final CategoryContract.View mCategoryView;
    private List<Category> categories;
    private final String ENDPOINT = Constant.URL_HOST + "categories";
    private Gson gson;

    public CategoryPresenter(CategoryContract.View mCategoryView) {
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

    public void prepareData() {
        categories = new ArrayList<>();
        categories.add(new Category("5b974fb26153321ffc61b828", "Linh kiện máy tính"));
        categories.add(new Category("5b974fb26153321ffc61b828", "Màn hình máy tính"));
        categories.add(new Category("5b974fb26153321ffc61b828", "Ổ cứng HDD/SSD"));
        categories.add(new Category("5b974fb26153321ffc61b828", "Chuột, Bàn phím, Webcam"));
        categories.add(new Category("5b974fb26153321ffc61b828", "Tai nghe & Loa"));
    }

    private void fetchData() {
        JsonObjectRequest  request = new JsonObjectRequest (Request.Method.GET, ENDPOINT, null, onPostsLoaded, onPostsError);
        MySingleton.getInstance(((Fragment) mCategoryView).getActivity().getApplicationContext()).addToRequestQueue(request);
    }

    private final Response.Listener<JSONObject> onPostsLoaded = new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject response) {
            try {
                Log.d("CategoryPresenter", "Load nè");
                categories = Arrays.asList(gson.fromJson(String.valueOf(response.getJSONArray("categories")), Category[].class));
                mCategoryView.setAdapterCategory(categories);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    private final Response.ErrorListener onPostsError = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.e("CategoryPresenter", error.toString());
        }
    };

}
