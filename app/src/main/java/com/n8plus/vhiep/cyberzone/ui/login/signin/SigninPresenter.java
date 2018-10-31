package com.n8plus.vhiep.cyberzone.ui.login.signin;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.n8plus.vhiep.cyberzone.data.model.Account;
import com.n8plus.vhiep.cyberzone.util.Constant;
import com.n8plus.vhiep.cyberzone.util.MySingleton;

import org.json.JSONException;
import org.json.JSONObject;

public class SigninPresenter implements SigninContract.Presenter {
    private SigninContract.View mSigninView;
    private Account mAccount;
    private Gson gson;
    private final String URL_ACCOUNT = Constant.URL_HOST + "accounts";

    public SigninPresenter(SigninContract.View mSigninView) {
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
        JsonObjectRequest checkLoginRequest = new JsonObjectRequest(Request.Method.POST, URL_ACCOUNT + "/checkLogin", account,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getJSONObject("account") != null) {
                                mAccount = gson.fromJson(String.valueOf(response.getJSONObject("account")), Account.class);
                                mSigninView.onSigninDefaultSuccess(mAccount);
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
                        mSigninView.onSigninDefaultFailed();
                    }
                });
        MySingleton.getInstance(((Fragment) mSigninView).getContext().getApplicationContext()).addToRequestQueue(checkLoginRequest);
    }
}
