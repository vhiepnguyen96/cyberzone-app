package com.n8plus.vhiep.cyberzone.ui.manage.myorders.allorder;

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
import com.n8plus.vhiep.cyberzone.util.VolleyUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class AllOrderPresenter implements AllOrderContract.Presenter {
    private static final String TAG = "AllOrderPresenter";
    private List<Order> orderList;
    private Context context;
    private AllOrderContract.View mAllOrderView;
    private Gson gson;

    public AllOrderPresenter(@NonNull final Context context, @NonNull final AllOrderContract.View mAllOrderView) {
        this.context = context;
        this.mAllOrderView = mAllOrderView;
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("M/d/yy hh:mm a");
        gson = gsonBuilder.create();
    }

    @Override
    public void loadAllOrder(String customerId) {
        mAllOrderView.setLayoutLoading(true);
        VolleyUtil.GET(context, Constant.URL_ORDER + "/customer/" + customerId,
                response -> {
                    try {
                        orderList = Arrays.asList(gson.fromJson(String.valueOf(response.getJSONArray("orders")), Order[].class));
                        Log.i("AllOrderPresenter", "GET: " + orderList.size() + " order");
                        mAllOrderView.setLayoutLoading(false);

                        if (orderList.size() > 0) {
                            mAllOrderView.setLayoutNone(false);
                            mAllOrderView.setAdapterAllOrder(orderList);
                            for (int i = 0; i < orderList.size(); i++) {
                                final int position = i;
                                VolleyUtil.GET(context, Constant.URL_ORDER_ITEM + "/order/" + orderList.get(i).getOrderId(),
                                        response1 -> {
                                            try {
                                                orderList.get(position).setPurchaseList(Arrays.asList(gson.fromJson(String.valueOf(response1.getJSONArray("orderItems")), PurchaseItem[].class)));
                                                Log.i(TAG, "GET: " + orderList.get(position).getPurchaseList().size() + " orderItems");
                                                mAllOrderView.setNotifyDataSetChanged();
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        },
                                        error -> Log.e(TAG, error.toString()));
                            }
                        } else {
                            mAllOrderView.setLayoutNone(true);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    Log.e(TAG, error.toString());
                    mAllOrderView.setLayoutNone(true);
                    mAllOrderView.setLayoutLoading(false);
                });
    }
}
