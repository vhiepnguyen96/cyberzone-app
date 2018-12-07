package com.n8plus.vhiep.cyberzone.ui.manage.registersale.loadregistersale;

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
import com.n8plus.vhiep.cyberzone.data.model.RegisterSale;
import com.n8plus.vhiep.cyberzone.data.model.WishList;
import com.n8plus.vhiep.cyberzone.ui.manage.registersale.RegisterSaleContract;
import com.n8plus.vhiep.cyberzone.util.Constant;
import com.n8plus.vhiep.cyberzone.util.MySingleton;
import com.n8plus.vhiep.cyberzone.util.VolleyUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LoadRegisterSalePresenter implements LoadRegisterSaleContract.Presenter {
    private static final String TAG = "RegisterSalePresenter";
    private Context context;
    private LoadRegisterSaleContract.View mLoadRegisterSaleView;
    private List<RegisterSale> mRegisterSales;
    private Gson gson;

    public LoadRegisterSalePresenter(@NonNull final Context context, @NonNull final LoadRegisterSaleContract.View mLoadRegisterSaleView) {
        this.context = context;
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
        mLoadRegisterSaleView.setLayoutLoading(true);
        VolleyUtil.GET(context, Constant.URL_REGISTER_SALE + "/customer/" + customerId,
                response -> {
                    try {
                        mRegisterSales = Arrays.asList(gson.fromJson(String.valueOf(response.getJSONArray("registeredSales")), RegisterSale[].class));
                        Log.d(TAG, "GET mRegisterSales: " + mRegisterSales.size());
                        mLoadRegisterSaleView.setLayoutLoading(false);

                        if (mRegisterSales.size() > 0) {
                            mLoadRegisterSaleView.setLayoutNone(false);
                            mLoadRegisterSaleView.setAdapterRegisterSale(mRegisterSales);
                        } else {
                            mLoadRegisterSaleView.setLayoutNone(true);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    Log.d(TAG, error.toString());
                    mLoadRegisterSaleView.setLayoutNone(true);
                    mLoadRegisterSaleView.setLayoutLoading(false);
                });
    }
}
