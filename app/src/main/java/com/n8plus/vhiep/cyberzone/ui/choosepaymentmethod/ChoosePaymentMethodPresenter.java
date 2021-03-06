package com.n8plus.vhiep.cyberzone.ui.choosepaymentmethod;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.n8plus.vhiep.cyberzone.data.model.Address;
import com.n8plus.vhiep.cyberzone.data.model.Order;
import com.n8plus.vhiep.cyberzone.data.model.OrderState;
import com.n8plus.vhiep.cyberzone.data.model.PaymentMethod;
import com.n8plus.vhiep.cyberzone.data.model.Product;
import com.n8plus.vhiep.cyberzone.data.model.ProductImage;
import com.n8plus.vhiep.cyberzone.data.model.PurchaseItem;
import com.n8plus.vhiep.cyberzone.util.Constant;
import com.n8plus.vhiep.cyberzone.util.MySingleton;
import com.n8plus.vhiep.cyberzone.util.VolleyUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ChoosePaymentMethodPresenter implements ChoosePaymentMethodContract.Presenter {
    private static final String TAG = "PaymentMethodPresenter";
    private Context context;
    private ChoosePaymentMethodContract.View mChoosePayemntMethodView;
    private List<PaymentMethod> paymentMethods;
    private List<OrderState> orderStates;
    private OrderState stateProcessing;
    private OrderState stateWaitForPayment;
    private Order mOrder;
    private Gson gson;
    private int countSuccess = 0, countValid = 0;
    private boolean isValid;

    public ChoosePaymentMethodPresenter(@NonNull final Context context, @NonNull final ChoosePaymentMethodContract.View choosePayemntMethodView) {
        this.context = context;
        this.mChoosePayemntMethodView = choosePayemntMethodView;
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("M/d/yy hh:mm a");
        gson = gsonBuilder.create();
    }

    @Override
    public void loadPaymentMethod() {
        paymentMethods = new ArrayList<>();
        VolleyUtil.GET(context, Constant.URL_PAYMENT_METHOD,
                response -> {
                    try {
                        paymentMethods = Arrays.asList(gson.fromJson(String.valueOf(response.getJSONArray("paymentMethods")), PaymentMethod[].class));
                        Log.i(TAG, "GET: " + paymentMethods.size() + " paymentMethods");
                        if (paymentMethods.size() > 0) {
                            mChoosePayemntMethodView.setAdapterPaymentMethod(paymentMethods);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> Log.e(TAG, error.toString()));
    }

    @Override
    public void loadOrderState() {
        orderStates = new ArrayList<>();
        VolleyUtil.GET(context, Constant.URL_ORDER_STATE,
                response -> {
                    try {
                        orderStates = Arrays.asList(gson.fromJson(String.valueOf(response.getJSONArray("orderStates")), OrderState[].class));
                        Log.i(TAG, "GET: " + orderStates.size() + " orderStates");
                        for (OrderState state : orderStates) {
                            if (state.getStateName().equals("Đang xử lý")) {
                                stateProcessing = state;
                            } else if (state.getStateName().equals("Đang chờ thanh toán")) {
                                stateWaitForPayment = state;
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> Log.e(TAG, error.toString()));
    }

    @Override
    public void loadOrder(Order order) {
        mOrder = order;
    }

    @Override
    public void choosePaymentMethod(int position) {
        mOrder.setPaymentMethod(paymentMethods.get(position));
        if (paymentMethods.get(position).getName().equals("Thanh toán khi nhận hàng")) {
            mOrder.setOrderState(stateProcessing);
        } else {
            mOrder.setOrderState(stateWaitForPayment);
        }
        checkCurrentQuantity();
    }

    @Override
    public void checkCurrentQuantity() {
        for (int i = 0; i < mOrder.getPurchaseList().size(); i++) {
            int finalI = i;
            VolleyUtil.GET(context, Constant.URL_PRODUCT + "/" + mOrder.getPurchaseList().get(i).getProduct().getProductId(),
                    response -> {
                        try {
                            Product product = gson.fromJson(String.valueOf(response.getJSONObject("product")), Product.class);
                            if (product != null) {
                                Log.d(TAG, "quantityBuy: " + mOrder.getPurchaseList().get(finalI).getQuantity() + " | quantityProduct: " + product.getQuantity());
                                if (mOrder.getPurchaseList().get(finalI).getQuantity() <= product.getQuantity()) {
                                    countValid++;
                                    Log.d(TAG, "countValid: " + countValid + " | PurchaseList: " + mOrder.getPurchaseList().size());
                                }
                                // Last i
                                if (finalI == mOrder.getPurchaseList().size() - 1) {
                                    // Check is valid quantity
                                    isValid = (countValid == mOrder.getPurchaseList().size()) ? true : false;
                                    Log.d(TAG, " isValid: " + isValid);

                                    if (isValid) {
                                        saveOrder();
                                    } else {
                                        mChoosePayemntMethodView.showQuantityNonValid();
                                    }
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    },
                    error -> Log.e(TAG, error.toString()));
        }
    }

    @Override
    public void saveOrder() {
        JSONObject order = new JSONObject();
        try {
            JSONObject deliveryAddress = new JSONObject();
            deliveryAddress.put("presentation", mOrder.getDeliveryAddress().getPresentation());
            deliveryAddress.put("phoneNumber", mOrder.getDeliveryAddress().getPhone());
            deliveryAddress.put("address", mOrder.getDeliveryAddress().getAddress());

            order.put("customerId", Constant.customer.getId());
            order.put("deliveryAddress", deliveryAddress);
            order.put("deliveryPriceId", mOrder.getDeliveryPrice().getId());
            order.put("totalQuantity", Constant.countProductInCart());
            order.put("totalPrice", mOrder.getTotalPrice());
            order.put("paymentMethodId", mOrder.getPaymentMethod().getId());
            order.put("orderStateId", mOrder.getOrderState().getId());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        VolleyUtil.POST(context, Constant.URL_ORDER, order,
                response -> {
                    try {
                        if (response.getJSONObject("createdOrder") != null) {
                            mOrder.setOrderId(response.getJSONObject("createdOrder").getString("_id"));
                            saveOrderItems(mOrder.getOrderId());
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> Log.e(TAG, error.toString()));
    }

    @Override
    public void saveOrderItems(String orderId) {
        for (final PurchaseItem item : mOrder.getPurchaseList()) {
            final JSONObject orderItem = new JSONObject();
            try {
                JSONObject store = new JSONObject();
                store.put("_id", item.getProduct().getStore().getStoreId());
                store.put("storeName", item.getProduct().getStore().getStoreName());

                JSONObject product = new JSONObject();
                product.put("_id", item.getProduct().getProductId());
                product.put("productName", item.getProduct().getProductName());

                // Get price
                if (item.getProduct().getSaleOff() != null && item.getProduct().getSaleOff().getDiscount() > 0) {
                    int basicPrice = Integer.valueOf(item.getProduct().getPrice());
                    int discount = item.getProduct().getSaleOff().getDiscount();
                    int salePrice = basicPrice - (basicPrice * discount / 100);
                    product.put("price", salePrice);
                } else {
                    product.put("price", Integer.valueOf(item.getProduct().getPrice()));
                }
                product.put("imageURL", item.getProduct().getImageList().get(0).getImageURL());
                product.put("store", store);

                orderItem.put("orderId", orderId);
                orderItem.put("product", product);
                orderItem.put("quantity", item.getQuantity());
                orderItem.put("orderItemStateId", mOrder.getOrderState().getId());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            VolleyUtil.POST(context, Constant.URL_ORDER_ITEM, orderItem,
                    response -> {
                        try {
                            if (response.getJSONObject("createdOrderItem") != null) {
                                // update orderItemId
                                int index = mOrder.getPurchaseList().indexOf(item);
                                String orderItemId = response.getJSONObject("createdOrderItem").getString("_id");
                                Log.d(TAG, "createdOrderItemID: " + orderItemId);
                                mOrder.getPurchaseList().get(index).setId(orderItemId);

                                // Update quantity product
                                int quantity = item.getProduct().getQuantity() - item.getQuantity();
                                updateQuantityProduct(item.getProduct().getProductId(), quantity);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    },
                    error -> Log.e(TAG, error.toString()));
//            JsonObjectRequest orderItemRequest = new JsonObjectRequest(Request.Method.POST, URL_ORDER_ITEM, orderItem,
//                    new Response.Listener<JSONObject>() {
//                        @Override
//                        public void onResponse(JSONObject response) {
//                            try {
//                                if (response.getJSONObject("createdOrderItem") != null) {
//                                    // update orderItemId
//                                    int index = mOrder.getPurchaseList().indexOf(item);
//                                    String orderItemId = response.getJSONObject("createdOrderItem").getString("_id");
//                                    Log.d(TAG, "createdOrderItemID: " + orderItemId);
//                                    mOrder.getPurchaseList().get(index).setId(orderItemId);
//
//                                    // Update quantity product
//                                    int quantity = item.getProduct().getQuantity() - item.getQuantity();
//                                    updateQuantityProduct(item.getProduct().getProductId(), quantity);
//                                }
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    },
//                    new Response.ErrorListener() {
//                        @Override
//                        public void onErrorResponse(VolleyError error) {
//                            Log.e("PaymentMethodPresenter", error.toString());
//                        }
//                    });
//            MySingleton.getInstance(((Activity) mChoosePayemntMethodView).getApplicationContext()).addToRequestQueue(orderItemRequest);
        }
    }

    @Override
    public void updateQuantityProduct(String productId, int quantity) {
        JSONArray updateArr = new JSONArray();
        try {
            JSONObject quantityObj = new JSONObject();
            quantityObj.put("propName", "quantity");
            quantityObj.put("value", quantity);

            updateArr.put(quantityObj);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        VolleyUtil.PATCH(context, Constant.URL_PRODUCT + "/" + productId, updateArr,
                response -> {
                    countSuccess++;
                    if (countSuccess == mOrder.getPurchaseList().size()) {
                        mChoosePayemntMethodView.saveOrderResult(true);
                        mChoosePayemntMethodView.moveToPayment(mOrder, orderStates);
                    }
                    Log.i(TAG, "countSuccess: " + countSuccess);
                },
                error -> {
                    Log.e(TAG, error.toString());
                    mChoosePayemntMethodView.saveOrderResult(false);
                });
//        JsonArrayRequest arrayRequest = new JsonArrayRequest(Request.Method.PATCH, URL_PRODUCT + "/" + productId, updateArr,
//                new Response.Listener<JSONArray>() {
//                    @Override
//                    public void onResponse(JSONArray response) {
//                        countSuccess++;
//                        if (countSuccess == mOrder.getPurchaseList().size()) {
//                            mChoosePayemntMethodView.saveOrderResult(true);
//                            mChoosePayemntMethodView.moveToPayment(mOrder, orderStates);
//                        }
//                        Log.i("PaymentMethodPresenter", "countSuccess: " + countSuccess);
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Log.e("PaymentMethodPresenter", error.toString());
//                        mChoosePayemntMethodView.saveOrderResult(false);
//                    }
//                });
//        MySingleton.getInstance(((Activity) mChoosePayemntMethodView).getApplicationContext()).addToRequestQueue(arrayRequest);
    }
}
