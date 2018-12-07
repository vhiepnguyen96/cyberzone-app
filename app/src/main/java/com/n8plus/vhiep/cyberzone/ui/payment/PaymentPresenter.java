package com.n8plus.vhiep.cyberzone.ui.payment;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.n8plus.vhiep.cyberzone.data.model.Order;
import com.n8plus.vhiep.cyberzone.data.model.OrderState;
import com.n8plus.vhiep.cyberzone.data.model.Product;
import com.n8plus.vhiep.cyberzone.data.model.PurchaseItem;
import com.n8plus.vhiep.cyberzone.util.Constant;
import com.n8plus.vhiep.cyberzone.util.MySingleton;
import com.n8plus.vhiep.cyberzone.util.VolleyUtil;
import com.stripe.android.Stripe;
import com.stripe.android.model.Card;
import com.stripe.android.model.Customer;
import com.stripe.android.model.Source;
import com.stripe.android.model.Token;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PaymentPresenter implements PaymentContract.Presenter {
    private final String TAG = "PaymentPresenter";
    private Context context;
    private PaymentContract.View mPaymentView;
    private Order mOrder;
    private List<OrderState> mOrderStates;
    private int mTotalPriceUSD = 0;
    private float mSubtotalPrice, mShippingFee, mTotalPrice, currentRate;
    private DecimalFormat df = new DecimalFormat("#.000");
    private Gson gson;
    private int updateSuccess = 0;

    public PaymentPresenter(@NonNull final Context context, @NonNull final PaymentContract.View mPaymentView) {
        this.context = context;
        this.mPaymentView = mPaymentView;
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("M/d/yy hh:mm a");
        gson = gsonBuilder.create();
    }

    @Override
    public void loadDataPayment(Order order) {
        mOrder = order;
        mSubtotalPrice = Integer.valueOf(order.getTotalPrice()) - Integer.valueOf(order.getDeliveryPrice().getTransportFee());
        mShippingFee = Integer.valueOf(order.getDeliveryPrice().getTransportFee());
        mTotalPrice = Integer.valueOf(order.getTotalPrice());

        mPaymentView.setOrderId(order.getOrderId() != null ? order.getOrderId() : "orderId");
        mPaymentView.setCountProduct(String.valueOf(order.getTotalQuantity()));
        mPaymentView.setTempPrice(mSubtotalPrice >= 1000 ? df.format(Product.convertToPrice(String.valueOf(mSubtotalPrice))).replace(",", ".") : String.valueOf(Math.round(mSubtotalPrice)));
        mPaymentView.setDeliveryPrice(mShippingFee >= 1000 ? df.format(Product.convertToPrice(String.valueOf(mShippingFee))).replace(",", ".") : String.valueOf(Math.round(mShippingFee)));
        mPaymentView.setTotalPrice(mTotalPrice >= 1000 ? df.format(Product.convertToPrice(String.valueOf(mTotalPrice))).replace(",", ".") : String.valueOf(Math.round(mTotalPrice)));

        if (order.getPaymentMethod().getName().equals("Thanh toán khi nhận hàng")) {
            mPaymentView.setLayoutPayemnt(order.getPaymentMethod().getName());
            mPaymentView.setDeliveryAddress(order.getDeliveryAddress());
        } else {
            mPaymentView.setLayoutPayemnt(order.getPaymentMethod().getName());
            fetchCurrencyRate();
        }
    }

    @Override
    public void loadOrderState(List<OrderState> orderStates) {
        mOrderStates = orderStates;
    }

    @Override
    public void loadOrderState() {
        mOrderStates = new ArrayList<>();
        VolleyUtil.GET(context, Constant.URL_ORDER_STATE,
                response -> {
                    try {
                        mOrderStates = Arrays.asList(gson.fromJson(String.valueOf(response.getJSONArray("orderStates")), OrderState[].class));
                        Log.i(TAG, "GET: " + mOrderStates.size() + " orderStates");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> Log.e(TAG, error.toString()));
    }


    public void fetchCurrencyRate() {
        VolleyUtil.GET(context, Constant.URL_CURRENCY_RATE,
                response -> {
                    try {
                        Log.i(TAG, "USD_VND: " + String.format("%.3f", response.getDouble("USD_VND") / 1000));
                        currentRate = (float) (response.getDouble("USD_VND") / 1000);
                        Log.i(TAG, "CurrentRate: " + currentRate);
                        mTotalPriceUSD = (int) (mTotalPrice / currentRate);
                        mPaymentView.setTotalPriceUSD(String.valueOf(mTotalPriceUSD));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> Log.e(TAG, error.toString()));
    }

    @Override
    public void chectOut(final Token token) {
        JSONObject charge = new JSONObject();
        try {
            charge.put("email", Constant.customer.getEmail());
            charge.put("order", mOrder.getOrderId());
            charge.put("source", token.getId());
            charge.put("amount", String.valueOf(mTotalPriceUSD * 100));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i(TAG, charge.toString());

        JsonObjectRequest checkOutRequest = new JsonObjectRequest(Request.Method.POST, Constant.URL_CHARGE, charge,
                response -> {
                    Log.i(TAG, "Response: " + response.toString());
                    mPaymentView.hideProgressDialog();
                    mPaymentView.setPaymentResult(true);
                    changeOrderState(mOrder);
                },
                error -> {
                    Log.e(TAG, "Error: " + error.getMessage());
                    mPaymentView.hideProgressDialog();
                    mPaymentView.setPaymentResult(false);
                });
        checkOutRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(context).addToRequestQueue(checkOutRequest);
    }

    @Override
    public void confirmOrder() {
        mPaymentView.moveToHome();
    }

    @Override
    public void changeOrderState(final Order order) {
        if (getOrderStateId(mOrderStates, "Đang xử lý") != null) {
            JSONArray updateOrder = new JSONArray();
            try {
                JSONObject stateObj = new JSONObject();
                stateObj.put("propName", "orderState");
                stateObj.put("value", getOrderStateId(mOrderStates, "Đang xử lý"));

                updateOrder.put(stateObj);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            VolleyUtil.PATCH(context, Constant.URL_ORDER + "/" + order.getOrderId(), updateOrder,
                    response -> {
                        Log.i(TAG, "Response: " + response.toString());
                        // Update order item state
                        for (PurchaseItem orderItem : order.getPurchaseList()) {
                            Log.d(TAG, "orderItemId: " + orderItem.getId());
                            JSONArray updateOrderItem = new JSONArray();
                            try {
                                JSONObject stateObj = new JSONObject();
                                stateObj.put("propName", "orderItemState");
                                stateObj.put("value", getOrderStateId(mOrderStates, "Đang xử lý"));
                                updateOrderItem.put(stateObj);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            VolleyUtil.PATCH(context, Constant.URL_ORDER_ITEM + "/" + orderItem.getId(), updateOrderItem,
                                    response1 -> {
                                        updateSuccess++;
                                        if (updateSuccess == mOrder.getPurchaseList().size()) {
                                            mPaymentView.updateOrderStateResult(true);
                                        }
                                        Log.i(TAG, "updateSuccess: " + updateSuccess);
                                    },
                                    error -> {
                                        Log.e(TAG, "Error: " + error.toString());
                                        mPaymentView.updateOrderStateResult(false);
                                    });
                        }
                    },
                    error -> Log.e(TAG, error.toString()));
        } else {
            Toast.makeText(((Activity) mPaymentView).getApplicationContext(), "Get orderStateId fail!", Toast.LENGTH_SHORT).show();
        }
    }

    private String getOrderStateId(List<OrderState> orderStates, String orderStateName) {
        String orderStateId = null;
        for (OrderState state : orderStates) {
            if (state.getStateName().equals(orderStateName)) {
                orderStateId = state.getId();
            }
        }
        return orderStateId;
    }
}
