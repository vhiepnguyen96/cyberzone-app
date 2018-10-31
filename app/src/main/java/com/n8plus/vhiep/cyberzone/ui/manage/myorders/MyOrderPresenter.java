package com.n8plus.vhiep.cyberzone.ui.manage.myorders;

import android.support.v4.app.Fragment;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.n8plus.vhiep.cyberzone.data.model.Order;
import com.n8plus.vhiep.cyberzone.data.model.PurchaseItem;
import com.n8plus.vhiep.cyberzone.ui.manage.myorders.allorder.AllOrderContract;
import com.n8plus.vhiep.cyberzone.ui.manage.myprofile.MyProfileContract;
import com.n8plus.vhiep.cyberzone.util.Constant;
import com.n8plus.vhiep.cyberzone.util.MySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MyOrderPresenter implements MyOrderContract.Presenter {
    private MyOrderContract.View mMyOrderView;
    private List<Order> orderList;
    private final String URL_ORDER = Constant.URL_HOST + "orders";
    private final String URL_ORDER_ITEM = Constant.URL_HOST + "orderItems";
    private Gson gson;

    public MyOrderPresenter(MyOrderContract.View mMyOrderView) {
        this.mMyOrderView = mMyOrderView;
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("M/d/yy hh:mm a");
        gson = gsonBuilder.create();
    }

    @Override
    public void loadData() {

    }

}
