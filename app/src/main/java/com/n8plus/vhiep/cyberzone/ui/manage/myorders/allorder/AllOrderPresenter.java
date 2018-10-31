package com.n8plus.vhiep.cyberzone.ui.manage.myorders.allorder;

import android.support.v4.app.Fragment;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.n8plus.vhiep.cyberzone.R;
import com.n8plus.vhiep.cyberzone.data.model.Address;
import com.n8plus.vhiep.cyberzone.data.model.Customer;
import com.n8plus.vhiep.cyberzone.data.model.Order;
import com.n8plus.vhiep.cyberzone.data.model.Overview;
import com.n8plus.vhiep.cyberzone.data.model.Product;
import com.n8plus.vhiep.cyberzone.data.model.ProductImage;
import com.n8plus.vhiep.cyberzone.data.model.ProductType;
import com.n8plus.vhiep.cyberzone.data.model.PurchaseItem;
import com.n8plus.vhiep.cyberzone.data.model.SaleOff;
import com.n8plus.vhiep.cyberzone.data.model.Specification;
import com.n8plus.vhiep.cyberzone.data.model.Store;
import com.n8plus.vhiep.cyberzone.ui.manage.myorders.waitforpayment.WaitForPaymentContract;
import com.n8plus.vhiep.cyberzone.util.Constant;
import com.n8plus.vhiep.cyberzone.util.MySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class AllOrderPresenter implements AllOrderContract.Presenter {
    private List<Order> orderList;
    private AllOrderContract.View mAllOrderView;
    private final String URL_ORDER = Constant.URL_HOST + "orders";
    private final String URL_ORDER_ITEM = Constant.URL_HOST + "orderItems";
    private Gson gson;

    public AllOrderPresenter(AllOrderContract.View mAllOrderView) {
        this.mAllOrderView = mAllOrderView;
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("M/d/yy hh:mm a");
        gson = gsonBuilder.create();
    }

    @Override
    public void loadAllOrder(String customerId) {
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URL_ORDER + "/customer/" + customerId, null,
            new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        orderList = Arrays.asList(gson.fromJson(String.valueOf(response.getJSONArray("orders")), Order[].class));
                        Log.i("AllOrderPresenter", "GET: " + orderList.size() + " order");
                        if (orderList.size() > 0) {
                            mAllOrderView.setAdapterAllOrder(orderList);
                            for (int i = 0; i < orderList.size(); i++) {
                                final int position = i;
                                JsonObjectRequest orderItemRequest = new JsonObjectRequest(Request.Method.GET, URL_ORDER_ITEM + "/order/" + orderList.get(i).getOrderId(), null,
                                        new Response.Listener<JSONObject>() {
                                            @Override
                                            public void onResponse(JSONObject response) {
                                                try {
                                                    orderList.get(position).setPurchaseList(Arrays.asList(gson.fromJson(String.valueOf(response.getJSONArray("orderItems")), PurchaseItem[].class)));
                                                    Log.i("AllOrderPresenter", "GET: " + orderList.get(position).getPurchaseList().size() + " orderItems");
                                                    mAllOrderView.setNotifyDataSetChanged();
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        },
                                        new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                Log.e("AllOrderPresenter", error.toString());
                                            }
                                        });
                                MySingleton.getInstance(((Fragment) mAllOrderView).getContext().getApplicationContext()).addToRequestQueue(orderItemRequest);
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
                    Log.e("AllOrderPresenter", error.toString());
                }
            });
        MySingleton.getInstance(((Fragment) mAllOrderView).getContext().getApplicationContext()).addToRequestQueue(request);
    }
}
