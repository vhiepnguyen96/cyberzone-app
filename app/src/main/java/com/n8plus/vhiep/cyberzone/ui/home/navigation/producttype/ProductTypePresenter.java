package com.n8plus.vhiep.cyberzone.ui.home.navigation.producttype;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.n8plus.vhiep.cyberzone.R;
import com.n8plus.vhiep.cyberzone.data.model.Category;
import com.n8plus.vhiep.cyberzone.data.model.ProductType;
import com.n8plus.vhiep.cyberzone.ui.home.adapter.ProductTypeAdapter;
import com.n8plus.vhiep.cyberzone.util.Constant;
import com.n8plus.vhiep.cyberzone.util.MySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.support.v4.util.Preconditions.checkNotNull;

public class ProductTypePresenter implements ProductTypeContract.Presenter {
    private ProductTypeContract.View mProductTypeView;
    private List<ProductType> productTypes;
    private final String URL = Constant.URL_HOST + "productTypes";
    private Gson gson;

    public ProductTypePresenter(ProductTypeContract.View mProductTypeView) {
        this.mProductTypeView = mProductTypeView;
        productTypes = new ArrayList<>();
    }

    public void prepareData() {
        productTypes = new ArrayList<>();
        productTypes.add(new ProductType("5b98a6a6fe67871b2068add0", "CPU - Bộ xử lý"));
        productTypes.add(new ProductType("5b98a6a6fe67871b2068add0", "Bo mạch chủ"));
        productTypes.add(new ProductType("5b98a6a6fe67871b2068add0", "Card màn hình - VGA"));
        productTypes.add(new ProductType("5b98a6a6fe67871b2068add0", "Nguồn - PSU"));
        productTypes.add(new ProductType("5b98a6a6fe67871b2068add0", "RAM"));
        productTypes.add(new ProductType("5b98a6a6fe67871b2068add0", "Case"));
        productTypes.add(new ProductType("5b98a6a6fe67871b2068add0", "Tản nhiệt"));
    }

    @Override
    public void loadData(String categoryId) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("M/d/yy hh:mm a");
        gson = gsonBuilder.create();
        fetchProductType(categoryId);
    }

    @Override
    public void prepareDataProductType(int position) {
        Bundle data = new Bundle();
        data.putString("productType", productTypes.get(position).getId());
        mProductTypeView.moveToProductActivity(data);
        Log.d("ProductTypePresenter", "productTypeID: "+productTypes.get(position).getId());
    }

    private void fetchProductType(String categoryId) {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URL + "/category/" + categoryId, null, onPostsLoaded, onPostsError);
        MySingleton.getInstance(((Fragment) mProductTypeView).getActivity().getApplicationContext()).addToRequestQueue(request);
    }

    private final Response.Listener<JSONObject> onPostsLoaded = new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject response) {
            try {
                Log.e("ProductTypePresenter", "Load nè");
                productTypes = Arrays.asList(gson.fromJson(String.valueOf(response.getJSONArray("productTypes")), ProductType[].class));
                mProductTypeView.setAdapterProductType(productTypes);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    private final Response.ErrorListener onPostsError = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.e("ProductTypePresenter", error.toString());
        }
    };

}
