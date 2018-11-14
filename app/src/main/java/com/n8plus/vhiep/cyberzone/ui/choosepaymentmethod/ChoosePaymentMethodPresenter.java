package com.n8plus.vhiep.cyberzone.ui.choosepaymentmethod;

import android.app.Activity;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ChoosePaymentMethodPresenter implements ChoosePaymentMethodContract.Presenter {

    private ChoosePaymentMethodContract.View mChoosePayemntMethodView;
    private List<PaymentMethod> paymentMethods;
    private List<OrderState> orderStates;
    private final String URL_PAYMENT_METHOD = Constant.URL_HOST + "paymentMethods";
    private String URL_ORDER = Constant.URL_HOST + "orders";
    private String URL_ORDER_STATE = Constant.URL_HOST + "orderStates";
    private String URL_ORDER_ITEM = Constant.URL_HOST + "orderItems";
    private String URL_PRODUCT = Constant.URL_HOST + "products";
    private OrderState stateProcessing;
    private OrderState stateWaitForPayment;
    private Order mOrder;
    private Gson gson;
    private int countSuccess = 0;

    public ChoosePaymentMethodPresenter(ChoosePaymentMethodContract.View choosePayemntMethodView) {
        this.mChoosePayemntMethodView = choosePayemntMethodView;
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("M/d/yy hh:mm a");
        gson = gsonBuilder.create();
    }

    @Override
    public void loadPaymentMethod() {
        paymentMethods = new ArrayList<>();
        JsonObjectRequest paymentMethodRequest = new JsonObjectRequest(Request.Method.GET, URL_PAYMENT_METHOD, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            paymentMethods = Arrays.asList(gson.fromJson(String.valueOf(response.getJSONArray("paymentMethods")), PaymentMethod[].class));
                            Log.i("PaymentMethodPresenter", "GET: " + paymentMethods.size() + " paymentMethods");
                            if (paymentMethods.size() > 0) {
                                mChoosePayemntMethodView.setAdapterPaymentMethod(paymentMethods);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("PaymentMethodPresenter", error.toString());
                    }
                });
        MySingleton.getInstance(((Activity) mChoosePayemntMethodView).getApplicationContext()).addToRequestQueue(paymentMethodRequest);
    }

    @Override
    public void loadOrderState() {
        orderStates = new ArrayList<>();
        JsonObjectRequest orderStateRequest = new JsonObjectRequest(Request.Method.GET, URL_ORDER_STATE, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            orderStates = Arrays.asList(gson.fromJson(String.valueOf(response.getJSONArray("orderStates")), OrderState[].class));
                            Log.i("PaymentMethodPresenter", "GET: " + orderStates.size() + " orderStates");
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
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("PaymentMethodPresenter", error.toString());
                    }
                });
        MySingleton.getInstance(((Activity) mChoosePayemntMethodView).getApplicationContext()).addToRequestQueue(orderStateRequest);
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
        saveOrder();
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
        JsonObjectRequest orderRequest = new JsonObjectRequest(Request.Method.POST, URL_ORDER, order,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getJSONObject("createdOrder") != null) {
                                mOrder.setOrderId(response.getJSONObject("createdOrder").getString("_id"));
                                saveOrderItems(mOrder.getOrderId());
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("PaymentMethodPresenter", error.toString());
                    }
                });
        MySingleton.getInstance(((Activity) mChoosePayemntMethodView).getApplicationContext()).addToRequestQueue(orderRequest);
    }

    @Override
    public void saveOrderItems(String orderId) {
        for (final PurchaseItem item : mOrder.getPurchaseList()) {
            JSONObject orderItem = new JSONObject();
            try {
                JSONObject product = new JSONObject();
                product.put("_id", item.getProduct().getProductId());
                product.put("productName", item.getProduct().getProductName());
                product.put("price", Integer.valueOf(item.getProduct().getPrice()));
                product.put("imageURL", item.getProduct().getImageList().get(0).getImageURL());

                orderItem.put("orderId", orderId);
                orderItem.put("product", product);
                orderItem.put("quantity", item.getQuantity());
                orderItem.put("orderItemStateId", mOrder.getOrderState().getId());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            JsonObjectRequest orderItemRequest = new JsonObjectRequest(Request.Method.POST, URL_ORDER_ITEM, orderItem,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            // Update quantity product
                            int quantity = item.getProduct().getQuantity() - item.getQuantity();
                            updateQuantityProduct(item.getProduct().getProductId(), quantity);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("PaymentMethodPresenter", error.toString());
                        }
                    });
            MySingleton.getInstance(((Activity) mChoosePayemntMethodView).getApplicationContext()).addToRequestQueue(orderItemRequest);
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

        JsonArrayRequest arrayRequest = new JsonArrayRequest(Request.Method.PATCH, URL_PRODUCT + "/" + productId, updateArr,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        countSuccess++;
                        if (countSuccess == mOrder.getPurchaseList().size()) {
                            Toast.makeText(((Activity) mChoosePayemntMethodView).getApplicationContext(), "Lưu đơn hàng!", Toast.LENGTH_SHORT).show();
                            mChoosePayemntMethodView.moveToPayment(mOrder, orderStates);
                        }
                        Log.i("PaymentMethodPresenter", "countSuccess: " + countSuccess);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("PaymentMethodPresenter", error.toString());
                        Toast.makeText(((Activity) mChoosePayemntMethodView).getApplicationContext(), "Lỗi khi lưu đơn hàng!", Toast.LENGTH_SHORT).show();
                    }
                });
        MySingleton.getInstance(((Activity) mChoosePayemntMethodView).getApplicationContext()).addToRequestQueue(arrayRequest);
    }
}
