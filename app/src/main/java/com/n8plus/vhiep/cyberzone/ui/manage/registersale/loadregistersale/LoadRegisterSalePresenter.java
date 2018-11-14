package com.n8plus.vhiep.cyberzone.ui.manage.registersale.loadregistersale;

import android.support.v4.app.Fragment;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.n8plus.vhiep.cyberzone.data.model.RegisterSale;
import com.n8plus.vhiep.cyberzone.data.model.WishList;
import com.n8plus.vhiep.cyberzone.ui.manage.registersale.RegisterSaleContract;
import com.n8plus.vhiep.cyberzone.util.Constant;
import com.n8plus.vhiep.cyberzone.util.MySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LoadRegisterSalePresenter implements LoadRegisterSaleContract.Presenter {
    private LoadRegisterSaleContract.View mLoadRegisterSaleView;
    private List<RegisterSale> mRegisterSales;
    private Gson gson;
    private final String URL_REGISTER_SALE = Constant.URL_HOST + "registeredSales";

    public LoadRegisterSalePresenter(LoadRegisterSaleContract.View mLoadRegisterSaleView) {
        this.mLoadRegisterSaleView = mLoadRegisterSaleView;
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("M/d/yy hh:mm a");
        gson = gsonBuilder.create();
    }

    @Override
    public void loadData() {
        loadAllRegisterSale(Constant.customer.getId());
    }

    @Override
    public void loadAllRegisterSale(String customerId) {
        mRegisterSales = new ArrayList<>();
        JsonObjectRequest registerSaleRequest = new JsonObjectRequest(Request.Method.GET, URL_REGISTER_SALE + "/customer/" + customerId, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            mRegisterSales = Arrays.asList(gson.fromJson(String.valueOf(response.getJSONArray("registeredSales")), RegisterSale[].class));
                            Log.d("RegisterSalePresenter", "GET mRegisterSales: " + mRegisterSales.size());
                            if (mRegisterSales.size() > 0) {
                                mLoadRegisterSaleView.setLayoutNone(false);
                                mLoadRegisterSaleView.setAdapterRegisterSale(mRegisterSales);
                            } else {
                                mLoadRegisterSaleView.setLayoutNone(true);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("RegisterSalePresenter", error.toString());
                        mLoadRegisterSaleView.setLayoutNone(true);
                    }
                });
        MySingleton.getInstance(((Fragment) mLoadRegisterSaleView).getContext().getApplicationContext()).addToRequestQueue(registerSaleRequest);
    }
}
