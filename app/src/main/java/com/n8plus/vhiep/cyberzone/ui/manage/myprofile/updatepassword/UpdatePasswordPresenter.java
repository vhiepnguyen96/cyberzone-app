package com.n8plus.vhiep.cyberzone.ui.manage.myprofile.updatepassword;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.n8plus.vhiep.cyberzone.data.model.Account;
import com.n8plus.vhiep.cyberzone.data.model.Customer;
import com.n8plus.vhiep.cyberzone.data.model.Product;
import com.n8plus.vhiep.cyberzone.util.Constant;
import com.n8plus.vhiep.cyberzone.util.MySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class UpdatePasswordPresenter implements UpdatePasswordContract.Presenter {
    private UpdatePasswordContract.View mUpdatePasswordView;
    private Customer mCustomer;
    private String URL_ACCOUNT = Constant.URL_HOST + "accounts";
    private Gson gson;

    public UpdatePasswordPresenter(UpdatePasswordContract.View mUpdatePasswordView) {
        this.mUpdatePasswordView = mUpdatePasswordView;
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("M/d/yy hh:mm a");
        gson = gsonBuilder.create();
    }

    @Override
    public void setCustomerProfile(Customer customer) {
        mCustomer = customer;
        fetchAccount();
    }

    @Override
    public void updatePassword(String oldPass, String newPass, String retypeNewPass) {
        checkIsValid(oldPass, newPass, retypeNewPass);
    }

    public void checkIsValid(String oldPass, String newPass, String retypeNewPass) {
        if (!mCustomer.getAccount().getPassword().equals(oldPass)) {
            mUpdatePasswordView.setAlert("Mật khẩu cũ không hợp lệ!");
        } else if (!newPass.equals(retypeNewPass)) {
            mUpdatePasswordView.setAlert("Mật khẩu mới không trùng khớp!");
        } else {
            updatePassword(newPass);
        }
    }

    public void fetchAccount() {
        JsonObjectRequest accountRequest = new JsonObjectRequest(Request.Method.GET, URL_ACCOUNT + "/" + mCustomer.getAccount().getUsername(), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    mCustomer.setAccount(gson.fromJson(String.valueOf(response.getJSONObject("account")), Account.class));
                    Log.i("UpdatePasswordPresenter", "GET: account: "+mCustomer.getAccount().getUsername()+", pass: "+mCustomer.getAccount().getPassword());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("ProductPresenter", error.toString());
            }
        });
        MySingleton.getInstance(((Fragment) mUpdatePasswordView).getContext().getApplicationContext()).addToRequestQueue(accountRequest);
    }

    public void updatePassword(String newPass) {
        final JSONObject passwordObject = new JSONObject();
        try {
            passwordObject.put("propName", "password");
            passwordObject.put("value", newPass);
        } catch (JSONException e) {
            Log.e("Error", e.toString());
        }

        JSONArray updateOps = new JSONArray();
        updateOps.put(passwordObject);

        JsonArrayRequest updateRequest = new JsonArrayRequest(Request.Method.PATCH, URL_ACCOUNT + "/" + mCustomer.getAccount().getUsername(), updateOps,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        mUpdatePasswordView.setAlert("Cập nhật mật khẩu thành công!");
                        mUpdatePasswordView.resetEditText();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("UpdateProfilePresenter", error.toString());
                        mUpdatePasswordView.setAlert("Cập nhật mật khẩu thất bại!");
                    }
                });

        MySingleton.getInstance(((Fragment) mUpdatePasswordView).getContext().getApplicationContext()).addToRequestQueue(updateRequest);
    }
}
