package com.n8plus.vhiep.cyberzone.ui.manage.myprofile;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.n8plus.vhiep.cyberzone.data.model.Customer;
import com.n8plus.vhiep.cyberzone.data.model.Product;
import com.n8plus.vhiep.cyberzone.ui.manage.mainmanage.MainManageContract;
import com.n8plus.vhiep.cyberzone.util.Constant;
import com.n8plus.vhiep.cyberzone.util.MySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class MyProfilePresenter implements MyProfileContract.Presenter {
    private Customer mCustomer;
    private MyProfileContract.View mProfileView;
    private String URL_CUSTOMER = Constant.URL_HOST + "customers";
    private Gson gson;

    public MyProfilePresenter(MyProfileContract.View mProfileView) {
        this.mProfileView = mProfileView;
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("M/d/yy hh:mm a");
        gson = gsonBuilder.create();
    }

    @Override
    public void loadProfile(String customerId) {
        fetchCustomerProfile(customerId);
    }

    @Override
    public void prepareDataProfile() {
        mProfileView.moveToUpdateProfile(mCustomer);
    }

    @Override
    public void prepareDataPassword() {
        mProfileView.moveToUpdatePassword(mCustomer);
    }

    private void fetchCustomerProfile(String customerId) {
        JsonObjectRequest customerProfileRequest = new JsonObjectRequest(Request.Method.GET, URL_CUSTOMER + "/" + customerId, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    mCustomer = gson.fromJson(String.valueOf(response.getJSONObject("customer")), Customer.class);
                    Log.i("MyProfilePresenter", "Customer: " + mCustomer.getName());
                    mProfileView.setNameCustomer(mCustomer.getName());
                    mProfileView.setGenderCustomer(mCustomer.getGender());
                    mProfileView.setBirthdayCustomer(mCustomer.getBirthday());
                    mProfileView.setPhoneNumberCustomer(mCustomer.getPhoneNumber());
                    mProfileView.setEmailCustomer(mCustomer.getEmail());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("HomePresenter", error.toString());
            }
        });
        MySingleton.getInstance(((Fragment) mProfileView).getContext().getApplicationContext()).addToRequestQueue(customerProfileRequest);
    }
}
