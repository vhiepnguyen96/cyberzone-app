package com.n8plus.vhiep.cyberzone.ui.manage.myprofile;

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
import com.n8plus.vhiep.cyberzone.data.model.Account;
import com.n8plus.vhiep.cyberzone.data.model.Customer;
import com.n8plus.vhiep.cyberzone.data.model.Product;
import com.n8plus.vhiep.cyberzone.ui.manage.mainmanage.MainManageContract;
import com.n8plus.vhiep.cyberzone.util.Constant;
import com.n8plus.vhiep.cyberzone.util.MySingleton;
import com.n8plus.vhiep.cyberzone.util.VolleyUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class MyProfilePresenter implements MyProfileContract.Presenter {
    private static final String TAG = "MyProfilePresenter";
    private Customer mCustomer;
    private Context context;
    private MyProfileContract.View mProfileView;
    private Gson gson;

    public MyProfilePresenter(@NonNull final Context context, @NonNull final MyProfileContract.View mProfileView) {
        this.context = context;
        this.mProfileView = mProfileView;
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("M/d/yy hh:mm a");
        gson = gsonBuilder.create();
    }

    @Override
    public void loadProfile(Customer customer) {
        mCustomer = customer;
        mProfileView.setNameCustomer(mCustomer.getName());
        mProfileView.setGenderCustomer(mCustomer.getGender());
        mProfileView.setBirthdayCustomer(mCustomer.getBirthday());
        mProfileView.setPhoneNumberCustomer(mCustomer.getPhoneNumber());
        mProfileView.setEmailCustomer(mCustomer.getEmail());

        checkAccount(mCustomer.getAccount().getId());
    }

    @Override
    public void checkAccount(String accountId) {
        VolleyUtil.GET(context, Constant.URL_ACCOUNT + "/" + accountId,
                response -> {
                    try {
                        Account account = gson.fromJson(String.valueOf(response.getJSONObject("account")), Account.class);
                        Log.i(TAG, "Account: " + account.getUsername());
                        if (account != null) {
                            mCustomer.getAccount().setUsername(account.getUsername());
                            mCustomer.getAccount().setPassword(account.getPassword());

                            mProfileView.setLayoutUpdatePassword(true);
                        } else {
                            mProfileView.setLayoutUpdatePassword(false);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    Log.e(TAG, error.toString());
                    mProfileView.setLayoutUpdatePassword(false);
                });
//        JsonObjectRequest customerProfileRequest = new JsonObjectRequest(Request.Method.GET, Constant.URL_ACCOUNT + "/" + accountId, null,
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        try {
//                            Account account = gson.fromJson(String.valueOf(response.getJSONObject("account")), Account.class);
//                            Log.i("MyProfilePresenter", "Account: " + account.getUsername());
//                            mProfileView.setLayoutUpdatePassword(account != null ? true : false);
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Log.e("HomePresenter", error.toString());
//                        mProfileView.setLayoutUpdatePassword(false);
//                    }
//                });
//        MySingleton.getInstance(((Fragment) mProfileView).getContext().getApplicationContext()).addToRequestQueue(customerProfileRequest);
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
        VolleyUtil.GET(context, Constant.URL_CUSTOMER + "/" + customerId,
                response -> {
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
                },
                error -> Log.e(TAG, error.toString()));
//        JsonObjectRequest customerProfileRequest = new JsonObjectRequest(Request.Method.GET, Constant.URL_CUSTOMER + "/" + customerId, null, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//                try {
//                    mCustomer = gson.fromJson(String.valueOf(response.getJSONObject("customer")), Customer.class);
//                    Log.i("MyProfilePresenter", "Customer: " + mCustomer.getName());
//                    mProfileView.setNameCustomer(mCustomer.getName());
//                    mProfileView.setGenderCustomer(mCustomer.getGender());
//                    mProfileView.setBirthdayCustomer(mCustomer.getBirthday());
//                    mProfileView.setPhoneNumberCustomer(mCustomer.getPhoneNumber());
//                    mProfileView.setEmailCustomer(mCustomer.getEmail());
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.e("HomePresenter", error.toString());
//            }
//        });
//        MySingleton.getInstance(((Fragment) mProfileView).getContext().getApplicationContext()).addToRequestQueue(customerProfileRequest);
    }
}
