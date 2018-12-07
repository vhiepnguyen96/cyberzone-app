package com.n8plus.vhiep.cyberzone.ui.cart;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.n8plus.vhiep.cyberzone.R;
import com.n8plus.vhiep.cyberzone.data.model.DeliveryPrice;
import com.n8plus.vhiep.cyberzone.data.model.Overview;
import com.n8plus.vhiep.cyberzone.data.model.Product;
import com.n8plus.vhiep.cyberzone.data.model.ProductImage;
import com.n8plus.vhiep.cyberzone.data.model.ProductType;
import com.n8plus.vhiep.cyberzone.data.model.PurchaseItem;
import com.n8plus.vhiep.cyberzone.data.model.SaleOff;
import com.n8plus.vhiep.cyberzone.data.model.Specification;
import com.n8plus.vhiep.cyberzone.data.model.Store;
import com.n8plus.vhiep.cyberzone.ui.login.signup.SignupContract;
import com.n8plus.vhiep.cyberzone.util.Constant;
import com.n8plus.vhiep.cyberzone.util.MySingleton;
import com.n8plus.vhiep.cyberzone.util.VolleyUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CartPresenter implements CartContract.Presenter {
    private static final String TAG = "CartPresenter";
    private Context context;
    private CartContract.View mCartView;
    private List<DeliveryPrice> deliveryPrices;
    private DeliveryPrice mDeliveryPrice;
    private Gson gson;
    DecimalFormat df;

    public CartPresenter(@NonNull final Context context, @NonNull final CartContract.View mCartView) {
        this.context = context;
        this.mCartView = mCartView;
    }

    @Override
    public void loadData() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("M/d/yy hh:mm a");
        gson = gsonBuilder.create();
        df = new DecimalFormat("#.000");

        mCartView.setAdapterCart(Constant.purchaseList);
        mCartView.setTempPrice(getTempPrice() >= 1000 ? df.format(Product.convertToPrice(String.valueOf(getTempPrice()))) : String.valueOf(Math.round(getTempPrice())));
        mCartView.setProductCount(String.valueOf(Constant.countProductInCart()));
        fetchDeliveryPrice();
        mCartView.setCartNone(Constant.purchaseList.size() == 0 ? true : false);
    }

    @Override
    public void orderNow() {
        mCartView.moveToCheckOrder(Constant.purchaseList, Math.round(getTempPrice()), mDeliveryPrice);
    }

    private float getTempPrice() {
        float tempPrice = 0;
        for (PurchaseItem item : Constant.purchaseList) {
            if (item.getProduct().getSaleOff() != null && item.getProduct().getSaleOff().getDiscount() > 0) {
                int basicPrice = Integer.valueOf(item.getProduct().getPrice());
                int discount = item.getProduct().getSaleOff().getDiscount();
                int salePrice = basicPrice - (basicPrice * discount / 100);
                tempPrice = tempPrice + (salePrice * item.getQuantity());
            } else {
                tempPrice = tempPrice + (Integer.valueOf(item.getProduct().getPrice()) * item.getQuantity());
            }
        }
        return tempPrice;
    }


    public void fetchDeliveryPrice() {
        deliveryPrices = new ArrayList<>();
        VolleyUtil.GET(context,  Constant.URL_DELIVERY_PRICE,
                response -> {
                    try {
                        deliveryPrices = Arrays.asList(gson.fromJson(String.valueOf(response.getJSONArray("deliveryPrices")), DeliveryPrice[].class));
                        Log.i("CartPresenter", "Delivery price: " + deliveryPrices.size());
                        for (int i = 0; i < deliveryPrices.size(); i++) {
                            Log.i("CartPresenter", "Delivery price " + i + ":" + deliveryPrices.get(i).getTotalPriceMin() + " | " + deliveryPrices.get(i).getTotalPriceMax() + " | " + deliveryPrices.get(i).getTransportFee());
                            if (getTempPrice() == 0) {
                                mCartView.setDeliveryPrice("0");
                            } else if (getTempPrice() > deliveryPrices.get(i).getTotalPriceMin() && deliveryPrices.get(i).getTotalPriceMax() == 0) {
                                if (Integer.valueOf(deliveryPrices.get(i).getTransportFee()) >= 1000) {
                                    mCartView.setDeliveryPrice(df.format(Product.convertToPrice(deliveryPrices.get(i).getTransportFee())));
                                } else {
                                    mCartView.setDeliveryPrice(deliveryPrices.get(i).getTransportFee());
                                }
                                mDeliveryPrice = deliveryPrices.get(i);

                            } else if (getTempPrice() > deliveryPrices.get(i).getTotalPriceMin() && getTempPrice() < deliveryPrices.get(i).getTotalPriceMax()) {
                                if (Integer.valueOf(deliveryPrices.get(i).getTransportFee()) >= 1000) {
                                    mCartView.setDeliveryPrice(df.format(Product.convertToPrice(deliveryPrices.get(i).getTransportFee())));
                                } else {
                                    mCartView.setDeliveryPrice(deliveryPrices.get(i).getTransportFee());
                                }
                                mDeliveryPrice = deliveryPrices.get(i);
                            }
                        }
                        if (mDeliveryPrice != null){
                            float totalPrice = getTempPrice() + Integer.valueOf(mDeliveryPrice.getTransportFee());
                            mCartView.setTotalPrice(totalPrice >= 1000 ? df.format(Product.convertToPrice(String.valueOf(totalPrice))).replace(",", ".") : String.valueOf(Math.round(totalPrice)));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> Log.e(TAG, error.toString()));
//        JsonObjectRequest deliveryPriceRequest = new JsonObjectRequest(Request.Method.GET, Constant.URL_DELIVERY_PRICE, null, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//                try {
//                    deliveryPrices = Arrays.asList(gson.fromJson(String.valueOf(response.getJSONArray("deliveryPrices")), DeliveryPrice[].class));
//                    Log.i("CartPresenter", "Delivery price: " + deliveryPrices.size());
//                    for (int i = 0; i < deliveryPrices.size(); i++) {
//                        Log.i("CartPresenter", "Delivery price " + i + ":" + deliveryPrices.get(i).getTotalPriceMin() + " | " + deliveryPrices.get(i).getTotalPriceMax() + " | " + deliveryPrices.get(i).getTransportFee());
//                        if (getTempPrice() == 0) {
//                            mCartView.setDeliveryPrice("0");
//                        } else if (getTempPrice() > deliveryPrices.get(i).getTotalPriceMin() && deliveryPrices.get(i).getTotalPriceMax() == 0) {
//                            if (Integer.valueOf(deliveryPrices.get(i).getTransportFee()) >= 1000) {
//                                mCartView.setDeliveryPrice(df.format(Product.convertToPrice(deliveryPrices.get(i).getTransportFee())));
//                            } else {
//                                mCartView.setDeliveryPrice(deliveryPrices.get(i).getTransportFee());
//                            }
//                            mDeliveryPrice = deliveryPrices.get(i);
//
//                        } else if (getTempPrice() > deliveryPrices.get(i).getTotalPriceMin() && getTempPrice() < deliveryPrices.get(i).getTotalPriceMax()) {
//                            if (Integer.valueOf(deliveryPrices.get(i).getTransportFee()) >= 1000) {
//                                mCartView.setDeliveryPrice(df.format(Product.convertToPrice(deliveryPrices.get(i).getTransportFee())));
//                            } else {
//                                mCartView.setDeliveryPrice(deliveryPrices.get(i).getTransportFee());
//                            }
//                            mDeliveryPrice = deliveryPrices.get(i);
//                        }
//                    }
//                    if (mDeliveryPrice != null){
//                        float totalPrice = getTempPrice() + Integer.valueOf(mDeliveryPrice.getTransportFee());
//                        mCartView.setTotalPrice(totalPrice >= 1000 ? df.format(Product.convertToPrice(String.valueOf(totalPrice))).replace(",", ".") : String.valueOf(Math.round(totalPrice)));
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.e("ProductPresenter", error.toString());
//            }
//        });
//        MySingleton.getInstance(((Activity) mCartView).getApplicationContext()).addToRequestQueue(deliveryPriceRequest);
    }
}
