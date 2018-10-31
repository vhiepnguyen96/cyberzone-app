package com.n8plus.vhiep.cyberzone.ui.payment;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.n8plus.vhiep.cyberzone.data.model.Order;
import com.n8plus.vhiep.cyberzone.data.model.Product;
import com.n8plus.vhiep.cyberzone.util.Constant;
import com.n8plus.vhiep.cyberzone.util.MySingleton;
import com.stripe.android.model.Token;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

public class PaymentPresenter implements PaymentContract.Presenter {
    private PaymentContract.View mPaymentView;
    private Order mOrder;
    private int mTotalPriceUSD = 0;
    private float mSubtotalPrice, mShippingFee, mTotalPrice, currentRate;
    private DecimalFormat df = new DecimalFormat("#.000");
    private String URL_CHARGE = Constant.URL_HOST + "checkouts/charge";
    private String URL_CURRENCY_RATE = "https://free.currencyconverterapi.com/api/v5/convert?q=USD_VND&compact=ultra";

    public PaymentPresenter(PaymentContract.View mPaymentView) {
        this.mPaymentView = mPaymentView;
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

        if (order.getPaymentMethod().getName().equals("Thanh toán khi nhận hàng")){
            mPaymentView.setLayoutPayemnt(order.getPaymentMethod().getName());
            mPaymentView.setDeliveryAddress(order.getDeliveryAddress());
        } else {
            mPaymentView.setLayoutPayemnt(order.getPaymentMethod().getName());
            fetchCurrencyRate();
        }
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
        JSONObject chargeObj = new JSONObject();
        try {
            chargeObj.put("email", Constant.customer.getEmail());
            chargeObj.put("order", mOrder.getOrderId());
            chargeObj.put("source", token.getId());
            chargeObj.put("amount", mTotalPriceUSD * 100);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("PaymentPresenter", chargeObj.toString());

        JsonObjectRequest checkOutRequest = new JsonObjectRequest(Request.Method.POST, URL_CHARGE, chargeObj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("PaymentPresenter", "Response: " + response.toString());
                        Toast.makeText(((Activity) mPaymentView).getApplicationContext(), "Đã thanh toán", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("PaymentPresenter", "Error: " + error.toString());
                        Toast.makeText(((Activity) mPaymentView).getApplicationContext(), "Đã thanh toán", Toast.LENGTH_SHORT).show();
                    }
                });
        MySingleton.getInstance(((Activity) mPaymentView).getApplicationContext()).addToRequestQueue(checkOutRequest);
    }

    @Override
    public void confirmOrder() {

    }
}
