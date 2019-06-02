package com.n8plus.vhiep.cyberzone.ui.manage.myreview.notwritereview.allpurchase;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
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
import com.n8plus.vhiep.cyberzone.util.VolleyUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static com.n8plus.vhiep.cyberzone.util.Constant.URL_ORDER_STATE;

public class AllPurchasePresenter implements AllPurchaseContract.Presenter {
    private Context context;
    private AllPurchaseContract.View mAllPurchaseView;
    private final String TAG = "AllPurchasePresenter";
    private List<OrderState> mOrderStates;
    private List<Order> mOrderList;
    private List<PurchaseItem> mOrderItemsNotReview;
    private Gson gson;
    private boolean isExists = true;

    public AllPurchasePresenter(@NonNull final Context context, @NonNull final AllPurchaseContract.View mAllPurchaseView) {
        this.context = context;
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
        VolleyUtil.GET(context, Constant.URL_ORDER_STATE,
                response -> {
                    try {
                        mOrderStates = Arrays.asList(gson.fromJson(String.valueOf(response.getJSONArray("orderStates")), OrderState[].class));
                        Log.i(TAG, "GET: " + mOrderStates.size() + " orderStates");
                        for (OrderState state : mOrderStates) {
                            if (state.getStateName().equals("Đã giao hàng")) {
                                loadOrderPurchased(Constant.customer.getId(), state.getId());
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> Log.e(TAG, error.toString()));
//        JsonObjectRequest orderStateRequest = new JsonObjectRequest(Request.Method.GET, URL_ORDER_STATE, null,
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        try {
//                            mOrderStates = Arrays.asList(gson.fromJson(String.valueOf(response.getJSONArray("orderStates")), OrderState[].class));
//                            Log.i("AllPurchasePresenter", "GET: " + mOrderStates.size() + " orderStates");
//                            for (OrderState state : mOrderStates) {
//                                if (state.getStateName().equals("Đã giao hàng")) {
//                                    loadOrderPurchased(Constant.customer.getId(), state.getId());
//                                }
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Log.e("AllPurchasePresenter", error.toString());
//                    }
//                });
//        MySingleton.getInstance(((Fragment) mAllPurchaseView).getContext().getApplicationContext()).addToRequestQueue(orderStateRequest);
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
        Log.i(TAG, "object: " + object.toString());

        VolleyUtil.POST(context, Constant.URL_ORDER + "/getByState", object,
                response -> {
                    try {
                        mOrderList = Arrays.asList(gson.fromJson(String.valueOf(response.getJSONArray("orders")), Order[].class));
                        Log.i(TAG, "GET: " + mOrderList.size() + " mOrderList");
                        if (mOrderList.size() > 0) {
                            for (int i = 0; i < mOrderList.size(); i++) {
                                loadOrderItems(i);
                            }
                        } else {
                            mAllPurchaseView.setLayoutNone(true);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> Log.e(TAG, error.toString()));
    }

    @Override
    public void loadOrderItems(final int position) {
        mAllPurchaseView.setLayoutLoading(true);
        VolleyUtil.GET(context, Constant.URL_ORDER_ITEM + "/order/" + mOrderList.get(position).getOrderId(),
                response -> {
                    try {
                        final List<PurchaseItem> purchaseItems = Arrays.asList(gson.fromJson(String.valueOf(response.getJSONArray("orderItems")), PurchaseItem[].class));
                        Log.i(TAG, "GET: " + purchaseItems.size() + " purchaseItems at " + position);

                        if (!purchaseItems.isEmpty()) {
                            // Get order item not review
                            final List<PurchaseItem> notReviewTemp = new ArrayList<>();

                            int countReview = 0;
                            for (int i = 0; i < purchaseItems.size(); i++) {

                                if (!purchaseItems.get(i).isReview()) {
                                    final int index = i;
                                    // Get product
                                    VolleyUtil.GET(context, Constant.URL_PRODUCT + "/" + purchaseItems.get(i).getProduct().getProductId(),
                                            response1 -> {
                                                try {
                                                    Product product = gson.fromJson(String.valueOf(response1.getJSONObject("product")), Product.class);
                                                    if (product != null) {
                                                        // Is not review and valid
                                                        notReviewTemp.add(purchaseItems.get(index));

                                                        // Last i
                                                        if (index == purchaseItems.size() - 1) {
                                                            // Set order item not review for order
                                                            mOrderList.get(position).setPurchaseList(notReviewTemp);
                                                            // Set data not review
                                                            setDataAdapterReview(mOrderList);
                                                            // Load store of product in order item
                                                            for (int j = 0; j < mOrderList.get(position).getPurchaseList().size(); j++) {
                                                                final int itemPosition = j;
                                                                String productId = mOrderList.get(position).getPurchaseList().get(itemPosition).getProduct().getProductId();
                                                                Log.d(TAG, "productId: " + productId);
                                                                VolleyUtil.GET(context, Constant.URL_PRODUCT + "/getStore/" + productId,
                                                                        response2 -> {
                                                                            try {
                                                                                Store store = gson.fromJson(String.valueOf(response2.getJSONObject("store")), Store.class);
                                                                                if (store != null) {
                                                                                    mOrderList.get(position).getPurchaseList().get(itemPosition).getProduct().setStore(store);
                                                                                    mAllPurchaseView.setNotifyDataSetChanged();
                                                                                }
                                                                            } catch (JSONException e) {
                                                                                e.printStackTrace();
                                                                            }
                                                                        },
                                                                        error -> Log.e(TAG, error.toString()));
                                                            }
                                                        }
                                                    }
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }

                                            },
                                            error -> {
                                                Log.e(TAG, error.toString());
                                                mAllPurchaseView.setLayoutNone(true);
                                                mAllPurchaseView.setLayoutLoading(false);
                                            });
                                } else {
                                    countReview++;
                                    if (i == purchaseItems.size() - 1){
                                        mAllPurchaseView.setLayoutNone(countReview == purchaseItems.size() ? true : false);
                                        mAllPurchaseView.setLayoutLoading(false);
                                    }
                                }
                            }
                        } else {
                            mAllPurchaseView.setLayoutNone(true);
                            mAllPurchaseView.setLayoutLoading(false);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> Log.e(TAG, error.toString()));
    }

    @Override
    public boolean checkProductIsExists(PurchaseItem orderItem) {
        VolleyUtil.GET(context, Constant.URL_PRODUCT + "/" + orderItem.getProduct().getProductId(),
                response -> {
                    try {
                        Product product = gson.fromJson(String.valueOf(response.getJSONObject("product")), Product.class);
                        isExists = product != null ? true : false;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    isExists = false;
                    Log.e(TAG, error.toString());
                });
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
        Log.d(TAG, "mOrderItemsNotReview: " + mOrderItemsNotReview.size());
        mAllPurchaseView.setLayoutNone(mOrderItemsNotReview.size() > 0 ? false : true);
        mAllPurchaseView.setLayoutLoading(false);
        mAllPurchaseView.setAdapterAllPurchase(mOrderItemsNotReview, datePurchaseList);
    }

    @Override
    public void prepareDataWriteReview(int position) {
        mAllPurchaseView.moveToWriteReview(mOrderItemsNotReview.get(position));
    }
}
