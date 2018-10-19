package com.n8plus.vhiep.cyberzone.ui.payment;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
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
    private int mTotalProduct;
    private float mSubtotalPrice, mShippingFee, mTotalPrice;
    private String URL_CHARGE = Constant.URL_HOST + "checkouts/charge";

    public PaymentPresenter(PaymentContract.View mPaymentView) {
        this.mPaymentView = mPaymentView;
    }

    @Override
    public void loadDataPayment(String countProduct, int tempPrice, int deliveryPrice) {
        mSubtotalPrice = tempPrice;
        mShippingFee = deliveryPrice;
        mTotalPrice = tempPrice + deliveryPrice;

        DecimalFormat df = new DecimalFormat("#.000");
        mPaymentView.setCountProduct(String.valueOf(countProduct));
        mPaymentView.setTempPrice(mSubtotalPrice >= 1000 ? df.format(Product.convertToPrice(String.valueOf(mSubtotalPrice))) : String.valueOf(Math.round(mSubtotalPrice)));
        mPaymentView.setDeliveryPrice(mShippingFee >= 1000 ? df.format(Product.convertToPrice(String.valueOf(mShippingFee))) : String.valueOf(Math.round(mShippingFee)));
        mPaymentView.setTotalPrice(mTotalPrice >= 1000 ? df.format(Product.convertToPrice(String.valueOf(mTotalPrice))) : String.valueOf(Math.round(mTotalPrice)));

    }

    @Override
    public void chectOut(final Token token) {
        final int amount = (int) (mTotalPrice / 23);

//        final JSONObject charge = new JSONObject();
//        try {
//            charge.put("email", "vhiepnguyen96@gmail.com");
//            charge.put("order", "5b9b7430b18f6d1178239040");
//            charge.put("source", token.getId());
//            charge.put("amount", amount * 100);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        Log.i("PaymentPresenter", charge.toString());

        JsonObjectRequest checkOutRequest = new JsonObjectRequest(Request.Method.POST, URL_CHARGE, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("PaymentPresenter", "Response: " + response.toString());
                        Toast.makeText(((Activity) mPaymentView).getApplicationContext(), "Thanh toán thành công", Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("PaymentPresenter", "Error: " + error.toString());
                Toast.makeText(((Activity) mPaymentView).getApplicationContext(), "Thanh toán thất bại", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");
                params.put("email", "vhiepnguyen96@gmail.com");
                params.put("order", "5b9b7430b18f6d1178239040");
                params.put("source", token.getId());
                params.put("amount", String.valueOf(amount * 100));
                return params;
            }
        };
        MySingleton.getInstance(((Activity) mPaymentView).getApplicationContext()).addToRequestQueue(checkOutRequest);
    }

}
