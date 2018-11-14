package com.n8plus.vhiep.cyberzone.ui.payment;

import android.app.Activity;
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
    private PaymentContract.View mPaymentView;
    private Order mOrder;
    private List<OrderState> mOrderStates;
    private int mTotalPriceUSD = 0;
    private float mSubtotalPrice, mShippingFee, mTotalPrice, currentRate;
    private DecimalFormat df = new DecimalFormat("#.000");
    private String URL_CHARGE = Constant.URL_HOST + "checkouts";
    private String URL_ORDER = Constant.URL_HOST + "orders";
    private String URL_ORDER_STATE = Constant.URL_HOST + "orderStates";
    private String URL_ORDER_ITEM = Constant.URL_HOST + "orderItems";
    private String URL_CURRENCY_RATE = "https://free.currencyconverterapi.com/api/v5/convert?q=USD_VND&compact=ultra";
    private Gson gson;
    private int updateSuccess = 0;

    public PaymentPresenter(PaymentContract.View mPaymentView) {
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
        JsonObjectRequest orderStateRequest = new JsonObjectRequest(Request.Method.GET, URL_ORDER_STATE, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            mOrderStates = Arrays.asList(gson.fromJson(String.valueOf(response.getJSONArray("orderStates")), OrderState[].class));
                            Log.i(TAG, "GET: " + mOrderStates.size() + " orderStates");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, error.toString());
                    }
                });
        MySingleton.getInstance(((Activity) mPaymentView).getApplicationContext()).addToRequestQueue(orderStateRequest);
    }


    public void fetchCurrencyRate() {
        JsonObjectRequest currencyRateRequest = new JsonObjectRequest(Request.Method.GET, URL_CURRENCY_RATE, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.i("PaymentPresenter", "USD_VND: " + String.format("%.3f", response.getDouble("USD_VND") / 1000));
                            currentRate = (float) (response.getDouble("USD_VND") / 1000);
                            Log.i("PaymentPresenter", "CurrentRate: " + currentRate);
                            mTotalPriceUSD = (int) (mTotalPrice / currentRate);
                            mPaymentView.setTotalPriceUSD(String.valueOf(mTotalPriceUSD));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("PaymentPresenter", error.toString());
                    }
                });
        MySingleton.getInstance(((Activity) mPaymentView).getApplicationContext()).addToRequestQueue(currencyRateRequest);
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
        Log.i("PaymentPresenter", charge.toString());
        JsonObjectRequest checkOutRequest = new JsonObjectRequest(Request.Method.POST, URL_CHARGE, charge,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i(TAG, "Response: " + response.toString());
                        mPaymentView.hideProgressDialog();
                        mPaymentView.setPaymentResult(true);
                        changeOrderState(mOrder);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "Error: " + error.getMessage());
                        mPaymentView.hideProgressDialog();
                        mPaymentView.setPaymentResult(false);
                    }
                });
        checkOutRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(((Activity) mPaymentView).getApplicationContext()).addToRequestQueue(checkOutRequest);
    }

    @Override
    public void confirmOrder() {
        mPaymentView.moveToHome();
    }

    @Override
    public void changeOrderState(final Order order) {
        if (getOrderStateId(mOrderStates, "Đang xử lý") != null) {
            JSONArray updateObj = new JSONArray();
            try {
                JSONObject stateObj = new JSONObject();
                stateObj.put("propName", "orderState");
                stateObj.put("value", getOrderStateId(mOrderStates, "Đang xử lý"));

                updateObj.put(stateObj);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            JsonArrayRequest orderStateRequest = new JsonArrayRequest(Request.Method.PATCH, URL_ORDER + "/" + order.getOrderId(), updateObj,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            Log.i(TAG, "Response: " + response.toString());
                            // Update order item state
                            for (PurchaseItem orderItem : order.getPurchaseList()) {
                                JSONArray updateObj = new JSONArray();
                                try {
                                    JSONObject stateObj = new JSONObject();
                                    stateObj.put("propName", "orderItemState");
                                    stateObj.put("value", getOrderStateId(mOrderStates, "Đang xử lý"));
                                    updateObj.put(stateObj);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                JsonArrayRequest orderItemStateRequest = new JsonArrayRequest(Request.Method.PATCH, URL_ORDER_ITEM + "/" + orderItem.getId(), updateObj,
                                        new Response.Listener<JSONArray>() {
                                            @Override
                                            public void onResponse(JSONArray response) {
                                                Log.i(TAG, "Response: " + response.toString());
                                                updateSuccess++;
                                                if (updateSuccess == mOrder.getPurchaseList().size()) {
                                                    mPaymentView.updateOrderStateResult(true);
                                                    mPaymentView.moveToHome();
                                                }
                                                Log.i(TAG, "updateSuccess: " + updateSuccess);
                                            }
                                        },
                                        new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                Log.e(TAG, "Error: " + error.toString());
                                                mPaymentView.updateOrderStateResult(false);
                                            }
                                        });
                                MySingleton.getInstance(((Activity) mPaymentView).getApplicationContext()).addToRequestQueue(orderItemStateRequest);
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e(TAG, "Error: " + error.toString());
                        }
                    });
            MySingleton.getInstance(((Activity) mPaymentView).getApplicationContext()).addToRequestQueue(orderStateRequest);
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
