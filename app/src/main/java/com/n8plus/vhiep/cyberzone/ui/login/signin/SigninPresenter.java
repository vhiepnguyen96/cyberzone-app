package com.n8plus.vhiep.cyberzone.ui.login.signin;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.n8plus.vhiep.cyberzone.data.model.Account;
import com.n8plus.vhiep.cyberzone.data.model.Customer;
import com.n8plus.vhiep.cyberzone.util.Constant;
import com.n8plus.vhiep.cyberzone.util.MySingleton;
import com.n8plus.vhiep.cyberzone.util.VolleyUtil;

import org.json.JSONException;
import org.json.JSONObject;

public class SigninPresenter implements SigninContract.Presenter {
    private static final String TAG = "SigninPresenter";
    private Context context;
    private SigninContract.View mSigninView;
    private Account mAccount;
    private Gson gson;

    public SigninPresenter(@NonNull final Context context, @NonNull final SigninContract.View mSigninView) {
        this.context = context;
        this.mSigninView = mSigninView;
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("M/d/yy hh:mm a");
        gson = gsonBuilder.create();
    }

    @Override
    public void checkLogin(String username, String password) {
        JSONObject account = new JSONObject();
        try {
            account.put("username", username);
            account.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        VolleyUtil.POST(context, Constant.URL_ACCOUNT + "/checkLogin", account,
                response -> {
                    try {
                        if (response.getJSONObject("account") != null) {
                            mAccount = gson.fromJson(String.valueOf(response.getJSONObject("account")), Account.class);
                            mSigninView.onSigninDefaultSuccess(mAccount);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    Log.e(TAG, error.toString());
                    mSigninView.onSigninDefaultFailed();
                });
//        JsonObjectRequest checkLoginRequest = new JsonObjectRequest(Request.Method.POST, URL_ACCOUNT + "/checkLogin", account,
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        try {
//                            if (response.getJSONObject("account") != null) {
//                                mAccount = gson.fromJson(String.valueOf(response.getJSONObject("account")), Account.class);
//                                mSigninView.onSigninDefaultSuccess(mAccount);
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Log.e("PaymentMethodPresenter", error.toString());
//                        mSigninView.onSigninDefaultFailed();
//                    }
//                });
//        MySingleton.getInstance(((Fragment) mSigninView).getContext().getApplicationContext()).addToRequestQueue(checkLoginRequest);
    }

    @Override
    public void signIn(final String accountId) {
        VolleyUtil.GET(context, Constant.URL_CUSTOMER + "/account/" + accountId,
                response -> {
                    try {
                        Customer customer = gson.fromJson(String.valueOf(response.getJSONObject("customer")), Customer.class);
                        boolean isExists = customer != null ? true : false;
                        if (isExists) {
                            mSigninView.onSigninSuccess(accountId);
                        } else {
                            mSigninView.showAlertAccountNotRegister();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    Log.e(TAG, error.toString());
                    mSigninView.showAlertAccountNotRegister();
                });
//        JsonObjectRequest checkAccountRequest = new JsonObjectRequest(Request.Method.GET, URL_CUSTOMER + "/account/" + accountId, null,
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        Log.d(TAG, response.toString());
//                        try {
//                            Customer customer = gson.fromJson(String.valueOf(response.getJSONObject("customer")), Customer.class);
//                            boolean isExists = customer != null ? true : false;
//                            if (isExists) {
//                                mSigninView.onSigninSuccess(accountId);
//                            } else {
//                                mSigninView.showAlertAccountNotRegister();
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Log.e("SignupPresenter", error.toString());
//                        mSigninView.showAlertAccountNotRegister();
//                    }
//                });
//
//        MySingleton.getInstance(((Fragment) mSigninView).getContext().getApplicationContext()).addToRequestQueue(checkAccountRequest);
    }
}
