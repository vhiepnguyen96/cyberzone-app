package com.n8plus.vhiep.cyberzone.ui.manage.myorders.waitforpayment;

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
import com.n8plus.vhiep.cyberzone.data.model.Order;
import com.n8plus.vhiep.cyberzone.data.model.Overview;
import com.n8plus.vhiep.cyberzone.data.model.Product;
import com.n8plus.vhiep.cyberzone.data.model.ProductImage;
import com.n8plus.vhiep.cyberzone.data.model.ProductType;
import com.n8plus.vhiep.cyberzone.data.model.PurchaseItem;
import com.n8plus.vhiep.cyberzone.data.model.SaleOff;
import com.n8plus.vhiep.cyberzone.data.model.Specification;
import com.n8plus.vhiep.cyberzone.data.model.Store;
import com.n8plus.vhiep.cyberzone.ui.manage.myorders.MyOrderContract;
import com.n8plus.vhiep.cyberzone.util.Constant;
import com.n8plus.vhiep.cyberzone.util.MySingleton;
import com.n8plus.vhiep.cyberzone.util.VolleyUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class WaitForPaymentPresenter implements WaitForPaymentContract.Presenter {
    private static final String TAG = "WaitForPaymentPresenter";
    private List<Order> waitForPaymentList;
    private Context context;
    private WaitForPaymentContract.View mWaitForPaymentView;
    private Gson gson;

    public WaitForPaymentPresenter(@NonNull final Context context, @NonNull final WaitForPaymentContract.View mWaitForPaymentView) {
        this.context = context;
        this.mWaitForPaymentView = mWaitForPaymentView;
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("M/d/yy hh:mm a");
        gson = gsonBuilder.create();
    }

    @Override
    public void loadOrderWaitToPayment(String customerId) {
        mWaitForPaymentView.setLayoutLoading(true);
        VolleyUtil.GET(context, Constant.URL_ORDER + "/customer/" + customerId,
                response -> {
                    try {
                        List<Order> orderList = Arrays.asList(gson.fromJson(String.valueOf(response.getJSONArray("orders")), Order[].class));
                        Log.i(TAG, "GET: " + orderList.size() + " order");
                        mWaitForPaymentView.setLayoutLoading(false);

                        if (orderList.size() > 0) {
                            mWaitForPaymentView.setLayoutOrderNone(false);
                            waitForPaymentList = getWaitForPaymentList(orderList);
                            mWaitForPaymentView.setAdapterWaitForPayment(waitForPaymentList);
                            for (int i = 0; i < waitForPaymentList.size(); i++) {
                                final int position = i;
                                VolleyUtil.GET(context, Constant.URL_ORDER_ITEM + "/order/" + waitForPaymentList.get(i).getOrderId(),
                                        response1 -> {
                                            try {
                                                waitForPaymentList.get(position).setPurchaseList(Arrays.asList(gson.fromJson(String.valueOf(response1.getJSONArray("orderItems")), PurchaseItem[].class)));
                                                Log.i(TAG, "GET: " + waitForPaymentList.get(position).getPurchaseList().size() + " orderItems");
                                                mWaitForPaymentView.setNotifyDataSetChanged();
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        },
                                        error -> Log.e(TAG, error.toString()));
                            }
                        } else {
                            mWaitForPaymentView.setLayoutOrderNone(true);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    Log.e(TAG, error.toString());
                    mWaitForPaymentView.setLayoutOrderNone(true);
                    mWaitForPaymentView.setLayoutLoading(false);
                });
    }

    public List<Order> getWaitForPaymentList(List<Order> orderList) {
        List<Order> waitForPayments = new ArrayList<>();
        for (Order order : orderList) {
            if (order.getOrderState().getStateName().equals("Đang chờ thanh toán")) {
                waitForPayments.add(order);
            }
        }
        return waitForPayments;
    }
}
