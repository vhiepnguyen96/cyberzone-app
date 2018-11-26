package com.n8plus.vhiep.cyberzone.ui.checkorder;

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
import com.n8plus.vhiep.cyberzone.data.model.Overview;
import com.n8plus.vhiep.cyberzone.data.model.Product;
import com.n8plus.vhiep.cyberzone.data.model.ProductImage;
import com.n8plus.vhiep.cyberzone.data.model.ProductType;
import com.n8plus.vhiep.cyberzone.data.model.PurchaseItem;
import com.n8plus.vhiep.cyberzone.data.model.SaleOff;
import com.n8plus.vhiep.cyberzone.data.model.Specification;
import com.n8plus.vhiep.cyberzone.data.model.Store;
import com.n8plus.vhiep.cyberzone.util.Constant;
import com.n8plus.vhiep.cyberzone.util.MySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class CheckOrderPresenter implements CheckOrderContract.Presenter {
    private static final String TAG = "CheckOrderPresenter";
    private CheckOrderContract.View mCheckOrderView;
    private List<PurchaseItem> mPurchaseItemList;
    private List<Address> addressList;
    private Address addressDefault;
    private String URL_ADDRESS = Constant.URL_HOST + "deliveryAddresses";
    private Gson gson;
    private int mTempPrice = 0, mTotalPrice = 0;
    private DeliveryPrice mDeliveryPrice;

    public CheckOrderPresenter(CheckOrderContract.View mCheckOrderView) {
        this.mCheckOrderView = mCheckOrderView;
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("M/d/yy hh:mm a");
        gson = gsonBuilder.create();
    }

    @Override
    public void loadPurchaseList(List<PurchaseItem> purchaseItems) {
        mPurchaseItemList = purchaseItems;
        mCheckOrderView.setAdapterOrder(purchaseItems);
        mCheckOrderView.setCountProduct(String.valueOf(Constant.countProductInCart()));
    }

    @Override
    public void loadPrice(int tempPrice, DeliveryPrice deliveryPrice) {
        mTempPrice = tempPrice;
        mDeliveryPrice = deliveryPrice;
        DecimalFormat df = new DecimalFormat("#.000");
        mCheckOrderView.setTempPrice(tempPrice >= 1000 ? df.format(Product.convertToPrice(String.valueOf(tempPrice))).replace(",", ".") : String.valueOf(Math.round(tempPrice)));
        int transportFee = Integer.valueOf(deliveryPrice.getTransportFee());
        mCheckOrderView.setDeliveryPrice(transportFee >= 1000 ? df.format(Product.convertToPrice(String.valueOf(transportFee))).replace(",", ".") : String.valueOf(Math.round(transportFee)));
        mTotalPrice = tempPrice + transportFee;
        mCheckOrderView.setTotalPrice(mTotalPrice >= 1000 ? df.format(Product.convertToPrice(String.valueOf(mTotalPrice))).replace(",", ".") : String.valueOf(Math.round(mTotalPrice)));
    }

    @Override
    public void loadDeliveryAddress(Address address) {
        addressDefault = address;
        mCheckOrderView.showLayoutAddress(true);
        mCheckOrderView.setNameCustomer(address.getPresentation());
        mCheckOrderView.setPhoneCustomer(address.getPhone());
        mCheckOrderView.setAddressCustomer(address.getAddress());
    }

    @Override
    public void loadDeliveryAddressDefault(String customerId) {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URL_ADDRESS + "/customer/" + customerId, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    addressList = Arrays.asList(gson.fromJson(String.valueOf(response.getJSONArray("deliveryAddresses")), Address[].class));
                    Log.i("CheckOrderPresenter", "GET: " + addressList.size() + " address");
                    if (addressList.size() > 0) {
                        mCheckOrderView.showLayoutAddress(true);
                        addressDefault = addressList.get(0);
                        mCheckOrderView.setNameCustomer(addressDefault.getPresentation());
                        mCheckOrderView.setPhoneCustomer(addressDefault.getPhone());
                        mCheckOrderView.setAddressCustomer(addressDefault.getAddress());
                    } else {
                        mCheckOrderView.showLayoutAddress(false);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("CheckOrderPresenter", error.toString());
            }
        });
        MySingleton.getInstance(((Activity) mCheckOrderView).getApplicationContext()).addToRequestQueue(request);
    }

    @Override
    public void prepareDataPayment() {
        if (addressDefault != null){
            Order order = new Order(Constant.customer, addressDefault, mDeliveryPrice, Constant.countProductInCart(), String.valueOf(mTotalPrice), mPurchaseItemList);
            mCheckOrderView.moveToChoosePaymentMethod(order);
        } else {
            mCheckOrderView.showAlert("Vui lòng kiểm tra lại địa chỉ giao hàng !");
        }

    }
}
