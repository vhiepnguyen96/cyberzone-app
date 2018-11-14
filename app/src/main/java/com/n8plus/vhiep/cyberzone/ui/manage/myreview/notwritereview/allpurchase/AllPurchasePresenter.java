package com.n8plus.vhiep.cyberzone.ui.manage.myreview.notwritereview.allpurchase;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.n8plus.vhiep.cyberzone.R;
import com.n8plus.vhiep.cyberzone.data.model.Address;
import com.n8plus.vhiep.cyberzone.data.model.Customer;
import com.n8plus.vhiep.cyberzone.data.model.DeliveryPrice;
import com.n8plus.vhiep.cyberzone.data.model.Order;
import com.n8plus.vhiep.cyberzone.data.model.OrderState;
import com.n8plus.vhiep.cyberzone.data.model.Overview;
import com.n8plus.vhiep.cyberzone.data.model.PaymentMethod;
import com.n8plus.vhiep.cyberzone.data.model.Product;
import com.n8plus.vhiep.cyberzone.data.model.ProductImage;
import com.n8plus.vhiep.cyberzone.data.model.ProductType;
import com.n8plus.vhiep.cyberzone.data.model.PurchaseItem;
import com.n8plus.vhiep.cyberzone.data.model.SaleOff;
import com.n8plus.vhiep.cyberzone.data.model.Specification;
import com.n8plus.vhiep.cyberzone.data.model.Store;
import com.n8plus.vhiep.cyberzone.ui.manage.myorders.allorder.AllOrderContract;
import com.n8plus.vhiep.cyberzone.util.Constant;
import com.n8plus.vhiep.cyberzone.util.MySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class AllPurchasePresenter implements AllPurchaseContract.Presenter {
    private AllPurchaseContract.View mAllPurchaseView;
    private final String TAG = "AllPurchasePresenter";
    private List<OrderState> mOrderStates;
    private List<Order> mOrderList;
    private List<PurchaseItem> orderItemsNotReviewTemp;
    private List<PurchaseItem> mOrderItemsNotReview;
    private final String URL_ORDER = Constant.URL_HOST + "orders";
    private final String URL_PRODUCT = Constant.URL_HOST + "products";
    private final String URL_ORDER_ITEM = Constant.URL_HOST + "orderItems";
    private final String URL_ORDER_STATE = Constant.URL_HOST + "orderStates";
    private Gson gson;
    private boolean isExists = true;

    public AllPurchasePresenter(AllPurchaseContract.View mAllPurchaseView) {
        this.mAllPurchaseView = mAllPurchaseView;
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("M/d/yy hh:mm a");
        gson = gsonBuilder.create();
    }

    @Override
    public void loadData() {
        loadOrderState();
    }

    @Override
    public void loadOrderState() {
        JsonObjectRequest orderStateRequest = new JsonObjectRequest(Request.Method.GET, URL_ORDER_STATE, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            mOrderStates = Arrays.asList(gson.fromJson(String.valueOf(response.getJSONArray("orderStates")), OrderState[].class));
                            Log.i("AllPurchasePresenter", "GET: " + mOrderStates.size() + " orderStates");
                            for (OrderState state : mOrderStates) {
                                if (state.getStateName().equals("Đã giao hàng")) {
                                    loadOrderPurchased(Constant.customer.getId(), state.getId());
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
                        Log.e("AllPurchasePresenter", error.toString());
                    }
                });
        MySingleton.getInstance(((Fragment) mAllPurchaseView).getContext().getApplicationContext()).addToRequestQueue(orderStateRequest);
    }

    @Override
    public void loadOrderPurchased(String customerId, String orderStateId) {
        mOrderList = new ArrayList<>();
        JSONObject object = new JSONObject();
        try {
            object.put("customerId", customerId);
            object.put("orderStateId", orderStateId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("AllPurchasePresenter", "object: " + object.toString());
        JsonObjectRequest orderPurchasedRequest = new JsonObjectRequest(Request.Method.POST, URL_ORDER + "/getByState", object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            mOrderList = Arrays.asList(gson.fromJson(String.valueOf(response.getJSONArray("orders")), Order[].class));
                            Log.i("AllPurchasePresenter", "GET: " + mOrderList.size() + " mOrderList");
                            if (mOrderList.size() > 0) {
                                for (int i = 0; i < mOrderList.size(); i++) {
                                    loadOrderItems(i);
                                }
                            } else {
                                mAllPurchaseView.setLayoutReviewNone(true);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("AllPurchasePresenter", error.toString());
                    }
                });
        MySingleton.getInstance(((Fragment) mAllPurchaseView).getContext().getApplicationContext()).addToRequestQueue(orderPurchasedRequest);
    }

    @Override
    public void loadOrderItems(final int position) {
        JsonObjectRequest orderItemRequest = new JsonObjectRequest(Request.Method.GET, URL_ORDER_ITEM + "/order/" + mOrderList.get(position).getOrderId(), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            final List<PurchaseItem> purchaseItems = Arrays.asList(gson.fromJson(String.valueOf(response.getJSONArray("orderItems")), PurchaseItem[].class));
                            Log.i("AllPurchasePresenter", "GET: " + purchaseItems.size() + " purchaseItems");

                            // Get order item not review
                            final List<PurchaseItem> orderItemsNotReviewTemp = new ArrayList<>();
                            for (int i = 0; i < purchaseItems.size(); i++) {
                                if (!purchaseItems.get(i).isReview()) {
                                    final int index = i;
                                    JsonObjectRequest productRequest = new JsonObjectRequest(Request.Method.GET, URL_PRODUCT + "/" + purchaseItems.get(i).getProduct().getProductId(), null,
                                            new Response.Listener<JSONObject>() {
                                                @Override
                                                public void onResponse(JSONObject response) {
                                                    // Is not review and valid
                                                    orderItemsNotReviewTemp.add(purchaseItems.get(index));
                                                    // If index is last position
                                                    if (index == purchaseItems.size() - 1) {
                                                        // Set order item not review for order
                                                        mOrderList.get(position).setPurchaseList(orderItemsNotReviewTemp);
                                                        // Get list order item not review
                                                        setDataAdapterReview(mOrderList);
                                                        // Load store of product in order item
                                                        for (int i = 0; i < mOrderList.get(position).getPurchaseList().size(); i++) {
                                                            final int itemPosition = i;
                                                            String productId = mOrderList.get(position).getPurchaseList().get(itemPosition).getProduct().getProductId();
                                                            Log.d(TAG, "productId: " + productId);
                                                            JsonObjectRequest storeRequest = new JsonObjectRequest(Request.Method.GET, URL_PRODUCT + "/getStore/" + productId, null,
                                                                    new Response.Listener<JSONObject>() {
                                                                        @Override
                                                                        public void onResponse(JSONObject response) {
                                                                            try {
                                                                                Store store = gson.fromJson(String.valueOf(response.getJSONObject("store")), Store.class);
                                                                                if (store != null) {
                                                                                    mOrderList.get(position).getPurchaseList().get(itemPosition).getProduct().setStore(store);
                                                                                    mAllPurchaseView.setNotifyDataSetChanged();
                                                                                }
                                                                            } catch (JSONException e) {
                                                                                e.printStackTrace();
                                                                            }
                                                                        }
                                                                    },
                                                                    new Response.ErrorListener() {
                                                                        @Override
                                                                        public void onErrorResponse(VolleyError error) {
                                                                            Log.e("AllPurchasePresenter", error.toString());
                                                                        }
                                                                    });
                                                            MySingleton.getInstance(((Fragment) mAllPurchaseView).getContext().getApplicationContext()).addToRequestQueue(storeRequest);
                                                        }
                                                    }
                                                }
                                            },
                                            new Response.ErrorListener() {
                                                @Override
                                                public void onErrorResponse(VolleyError error) {
                                                    Log.e("AllPurchasePresenter", error.toString());
                                                }
                                            });
                                    MySingleton.getInstance(((Fragment) mAllPurchaseView).getContext().getApplicationContext()).addToRequestQueue(productRequest);
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
                        Log.e("AllPurchasePresenter", error.toString());
                    }
                });
        MySingleton.getInstance(((Fragment) mAllPurchaseView).getContext().getApplicationContext()).addToRequestQueue(orderItemRequest);
    }

    @Override
    public boolean checkProductIsExists(PurchaseItem orderItem) {
        JsonObjectRequest productRequest = new JsonObjectRequest(Request.Method.GET, URL_PRODUCT + "/" + orderItem.getProduct().getProductId(), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Product product = gson.fromJson(String.valueOf(response.getJSONObject("product")), Product.class);

                            isExists = product != null ? true : false;
                            Log.d(TAG, "okne: " + isExists);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        isExists = false;
                        Log.d(TAG, "loine: " + isExists);
                        Log.e("AllPurchasePresenter", error.toString());
                    }
                });
        MySingleton.getInstance(((Fragment) mAllPurchaseView).getContext().getApplicationContext()).addToRequestQueue(productRequest);
        Log.d(TAG, "returnne: " + isExists);
        return isExists;
    }

    @Override
    public void setDataAdapterReview(List<Order> orderList) {
        mOrderItemsNotReview = new ArrayList<>();
        List<Date> datePurchaseList = new ArrayList<>();
        for (int i = 0; i < orderList.size(); i++) {
            if (orderList.get(i).getPurchaseList() != null && orderList.get(i).getPurchaseList().size() > 0) {
                for (int j = 0; j < orderList.get(i).getPurchaseList().size(); j++) {
                    mOrderItemsNotReview.add(orderList.get(i).getPurchaseList().get(j));
                    datePurchaseList.add(orderList.get(i).getPurchaseDate());
                }
            }
        }
        mAllPurchaseView.setLayoutReviewNone(mOrderItemsNotReview.size() > 0 ? false : true);
        mAllPurchaseView.setAdapterAllPurchase(mOrderItemsNotReview, datePurchaseList);
    }

    @Override
    public void prepareDataWriteReview(int position) {
        mAllPurchaseView.moveToWriteReview(mOrderItemsNotReview.get(position));
    }
}
